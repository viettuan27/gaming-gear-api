package com.tuanviet.gaminggear.service.impl;

import com.tuanviet.gaminggear.dto.request.CreateOrderRequest;
import com.tuanviet.gaminggear.dto.request.UpdateOrderStatusRequest;
import com.tuanviet.gaminggear.dto.response.OrderResponse;
import com.tuanviet.gaminggear.dto.response.OrderSummaryResponse;
import com.tuanviet.gaminggear.dto.response.PageResponse;
import com.tuanviet.gaminggear.entity.cart.Cart;
import com.tuanviet.gaminggear.entity.cart.CartItem;
import com.tuanviet.gaminggear.entity.catalog.Product;
import com.tuanviet.gaminggear.entity.catalog.ProductVariant;
import com.tuanviet.gaminggear.entity.order.Order;
import com.tuanviet.gaminggear.entity.order.OrderItem;
import com.tuanviet.gaminggear.entity.order.OrderStatus;
import com.tuanviet.gaminggear.entity.order.PaymentMethod;
import com.tuanviet.gaminggear.exception.BadRequestException;
import com.tuanviet.gaminggear.exception.ResourceNotFoundException;
import com.tuanviet.gaminggear.mapper.OrderMapper;
import com.tuanviet.gaminggear.repository.CartRepository;
import com.tuanviet.gaminggear.repository.OrderRepository;
import com.tuanviet.gaminggear.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    @CacheEvict(cacheNames = "product-detail", allEntries = true)
    public OrderResponse createOrder(Long userId, CreateOrderRequest request) {
        Cart cart = getCartForCheckout(userId);

        validateCartItem(cart);

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setRecipientName(request.recipientName().trim());
        order.setRecipientPhone(request.recipientPhone().trim());
        order.setShippingAddress(request.shippingAddress().trim());
        order.setNote(request.note() == null ? null : request.note().trim());
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(PaymentMethod.COD);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for(CartItem cartItem: cart.getCartItems()){
            OrderItem orderItem = createOrderItem(cartItem);
            order.addOrderItem(orderItem);

            totalAmount = totalAmount.add(orderItem.getLineTotal());

            ProductVariant variant = cartItem.getProductVariant();
            variant.setStockQuantity(
                    variant.getStockQuantity() - cartItem.getQuantity()
            );
        }

        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        cart.getCartItems().clear();

        return orderMapper.toResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<OrderSummaryResponse> getMyOrders(Long userId, int page, int size, String sortDirection) {
        Pageable pageable = createPageable(page, size, sortDirection);

        Page<Order> orderPage = orderRepository.findByUserId(userId,pageable);

        return toPageResponse(orderPage);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderDetails(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId,userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"));

        return orderMapper.toResponse(order);
    }

    @Override
    @CacheEvict(cacheNames = "product-detail", allEntries = true)
    public OrderResponse cancelOrder(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId,userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"));
        if (order.getStatus() != OrderStatus.PENDING){
            throw new BadRequestException("Chỉ có thể hủy đơn hàng đang chờ xác nhận");
        }
        order.setStatus(OrderStatus.CANCELLED);
        restoreStock(order);
        return orderMapper.toResponse(order);
    }

    @Override
    public PageResponse<OrderSummaryResponse> getAllOrders(OrderStatus status, int page, int size, String sortDirection) {
        Pageable pageable = createPageable(page, size, sortDirection);

        Page<Order> orderPage = status == null
                ? orderRepository.findAll(pageable)
                : orderRepository.findByStatus(status,pageable);
        return toPageResponse(orderPage);
    }

    @Override
    @CacheEvict(cacheNames = "product-detail", allEntries = true)
    public OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"));
        OrderStatus currentStatus = order.getStatus();
        OrderStatus newStatus = request.status();

        if(!isValidStatusTransition(currentStatus,newStatus)){
            throw new BadRequestException("Không thể chuyển trạng thái đơn hàng từ " + currentStatus + " sang " + newStatus);
        }
        if (newStatus == OrderStatus.CANCELLED){
            restoreStock(order);
        }
        order.setStatus(newStatus);
        return orderMapper.toResponse(order);
    }


    private Cart getCartForCheckout(Long userId){
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException("Giỏ hàng đang trống"));
        if(cart.getCartItems().isEmpty()){
            throw new BadRequestException("Giỏ hàng đang trống");
        }
        return cart;
    }

    private void validateCartItem(Cart cart){
        for(CartItem cartItem: cart.getCartItems()){
            ProductVariant variant = cartItem.getProductVariant();

            validateProductVariant(variant);
            validateStock(variant,cartItem.getQuantity());
        }
    }

    private void validateProductVariant(ProductVariant variant){
        Product product = variant.getProduct();
        if(!variant.isActive()
                || !product.isActive()
                || !product.getCategory().isActive()
                || !product.getBrand().isActive())
        {
            throw new BadRequestException("Sản phẩm "+ product.getName() + " hiện không khả dụng");
        }
    }

    private void validateStock(ProductVariant variant, int quantity){
        if(variant.getStockQuantity() == 0){
            throw new BadRequestException("Sản phẩm đang hết hàng");
        }
        if(quantity > variant.getStockQuantity()){
            throw new BadRequestException("Số lượng còn lại không đủ");
        }
    }


    private OrderItem createOrderItem(CartItem cartItem) {
        ProductVariant variant = cartItem.getProductVariant();
        Product product = variant.getProduct();

        BigDecimal lineTotal = variant.getPrice()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        OrderItem orderItem = new OrderItem();
        orderItem.setVariant(variant);
        orderItem.setProductName(product.getName());
        orderItem.setVariantName(variant.getName());
        orderItem.setSku(variant.getSku());
        orderItem.setUnitPrice(variant.getPrice());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setLineTotal(lineTotal);

        return orderItem;

    }

    private Pageable createPageable(int page, int size, String sortDirection){
        Sort sort = "asc".equalsIgnoreCase(sortDirection)
                ? Sort.by("createdAt").ascending()
                : Sort.by("createdAt").descending();
        return PageRequest.of(page,size,sort);
    }

    private PageResponse<OrderSummaryResponse> toPageResponse(Page<Order> orderPage){
        Page<OrderSummaryResponse> responsePage = orderPage
                .map(orderMapper::toSummaryResponse);

        return new PageResponse<>(
                responsePage.getContent(),
                responsePage.getNumber(),
                responsePage.getSize(),
                responsePage.getTotalElements(),
                responsePage.getTotalPages(),
                responsePage.isLast()
        );
    }

    private void restoreStock(Order order) {
        for(OrderItem orderItem : order.getOrderItems()){
            ProductVariant variant = orderItem.getVariant();
            variant.setStockQuantity(variant.getStockQuantity() + orderItem.getQuantity());
        }
    }

    private boolean isValidStatusTransition(
            OrderStatus currentStatus,
            OrderStatus newStatus
    ) {
        return switch (currentStatus) {
            case PENDING -> newStatus == OrderStatus.CONFIRMED
                    || newStatus == OrderStatus.CANCELLED;
            case CONFIRMED -> newStatus == OrderStatus.SHIPPING
                    || newStatus == OrderStatus.CANCELLED;
            case SHIPPING -> newStatus == OrderStatus.DELIVERED;
            case DELIVERED, CANCELLED -> false;
        };
    }

}
