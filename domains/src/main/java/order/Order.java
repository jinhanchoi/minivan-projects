package order;


import order.common.Money;
import order.common.Restaurant;
import order.events.*;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

import static order.OrderState.APPROVAL_PENDING;
import static order.OrderState.APPROVED;


@Entity
@Table(name = "orders")
@Access(AccessType.FIELD)
public class Order {

    public static ResultWithDomainEvents<Order, OrderDomainEvent> createOrder(long customerId, Restaurant restaurant, DeliveryInformation deliveryInformation, List<OrderLineItem> orderLineItems){
        Order order = new Order(customerId, restaurant.getId(), deliveryInformation, orderLineItems);
        List<OrderDomainEvent> events = Collections.singletonList(new OrderCreatedEvent(
                new OrderDetails(customerId,restaurant.getId(),orderLineItems,order.getOrderTotal()),
                deliveryInformation.getDeliveryAddress(),
                restaurant.getName()
        ));
        return new ResultWithDomainEvents<Order, OrderDomainEvent>(order,events);
    }
    private Order(){
    }

    private Order(long customerId, long restaurantId, DeliveryInformation deliveryInformation, List<OrderLineItem> orderLineItems) {
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.deliveryInformation = deliveryInformation;
        this.orderLineItems = new OrderLineItems(orderLineItems);
        this.state = APPROVAL_PENDING.name();
    }

    @Id
    @GeneratedValue
    private long orderId;

    @Version
    private long version;

    @Column(name = "order_state")
    private String state;

    private long customerId;
    private long restaurantId;

    @Embedded
    private OrderLineItems orderLineItems;

    @Embedded
    private DeliveryInformation deliveryInformation;

    @Embedded
    private PaymentInformation paymentInformation;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(OrderLineItems orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public DeliveryInformation getDeliveryInformation() {
        return deliveryInformation;
    }

    public void setDeliveryInformation(DeliveryInformation deliveryInformation) {
        this.deliveryInformation = deliveryInformation;
    }

    public PaymentInformation getPaymentInformation() {
        return paymentInformation;
    }

    public void setPaymentInformation(PaymentInformation paymentInformation) {
        this.paymentInformation = paymentInformation;
    }
    public Money getOrderTotal() {
        return orderLineItems.orderTotal();
    }

    public List<OrderDomainEvent> noteApproved(){
        switch (OrderState.valueOf(state)){
            case APPROVAL_PENDING:
                System.out.println(state);
                this.state = APPROVED.name();
                System.out.println(state);
                return Collections.singletonList(new OrderAuthorized());
            default:
                throw new UnsupportedStateTransitionException(OrderState.valueOf(state));
        }
    }
}
