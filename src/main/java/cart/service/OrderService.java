package cart.service;

import static java.util.stream.Collectors.toList;

import cart.domain.coupon.Coupon;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.dto.order.OrderResponse;
import cart.dto.order.OrderSaveRequest;
import cart.exception.order.OrderNotFoundException;
import cart.repository.CartItemRepository;
import cart.repository.CouponRepository;
import cart.repository.OrderRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final CouponRepository couponRepository;

    public OrderService(
            final OrderRepository orderRepository,
            final CartItemRepository cartItemRepository,
            final CouponRepository couponRepository
    ) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.couponRepository = couponRepository;
    }

    public Long save(final OrderSaveRequest request, final Long memberId) {
        final List<Long> orderItemIds = request.getOrderItemIds();
        final List<OrderItem> orderItems = cartItemRepository.findAllByMemberId(memberId).stream()
                .filter(cartItem -> orderItemIds.contains(cartItem.getId()))
                .map(cartItem -> new OrderItem(
                        cartItem.getProductName(),
                        cartItem.getProductImageUrl(),
                        cartItem.getProductPrice(),
                        cartItem.getQuantity()
                ))
                .collect(toList());

        final Coupon coupon = couponRepository.findById(request.getCouponId())
                .orElse(Coupon.EMPTY);

        final Order order = Order.of(coupon, memberId, orderItems);
        order.useCoupon();
        final Order saveOrder = orderRepository.save(order);

        cartItemRepository.deleteByIds(request.getOrderItemIds());
        couponRepository.update(order.getCoupon());
        return saveOrder.getId();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll(final Long memberId) {
        return orderRepository.findAllByMemberId(memberId).stream()
                .map(OrderResponse::from)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public OrderResponse findById(final Long id, final Long memberId) {
        final Order order = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);

        order.checkOwner(memberId);

        return OrderResponse.from(order);
    }
}
