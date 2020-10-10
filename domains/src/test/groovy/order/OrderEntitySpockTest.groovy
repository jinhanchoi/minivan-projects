package order


import order.common.Address
import order.common.MenuItem
import order.common.Money
import order.common.Restaurant
import order.events.OrderAuthorized
import order.events.OrderCreatedEvent
import order.events.OrderDomainEvent
import order.events.ResultWithDomainEvents
import spock.lang.Specification

import java.time.LocalDateTime

class OrderEntitySpockTest extends Specification{
    def 'Creating a Order'() {
        given:
            Address addr  = new Address("saemal-ro 93", "107-2106","seoul","guro-gu","08288")
            DeliveryInformation deliveryInformation = new DeliveryInformation(LocalDateTime.now(),addr)
            List<OrderLineItem> orderLineItems = [new OrderLineItem("ICE_CREAM", "JINHAN", new Money(3000), 3)]
            Order order = new Order(200L, 999L,  deliveryInformation, orderLineItems)
        expect: 'Approved state must shown'
            List<OrderDomainEvent> events = order.noteApproved()
            order.getState() == OrderState.APPROVED.name()
            assert events.get(0) instanceof OrderAuthorized
    }

    def 'Create Order With events'(){
        given:
            MenuItem menuItem = new MenuItem("ICE","ICE_CREAM",new Money(3000))
            Restaurant restaurant = new Restaurant(1L, "SUPER_MARKET", [menuItem])
            Address addr  = new Address("saemal-ro 93", "107-2106","seoul","guro-gu","08288")
            DeliveryInformation deliveryInformation = new DeliveryInformation(LocalDateTime.now(),addr)
            List<OrderLineItem> orderLineItems = [new OrderLineItem("ICE_CREAM", "JINHAN", new Money(3000), 3)]
        expect: 'Order Approved Pending expected'
            ResultWithDomainEvents<Order, OrderDomainEvent> orderWithEvents = Order.createOrder(123L,restaurant,deliveryInformation,orderLineItems)
            orderWithEvents.result.getState() == OrderState.APPROVAL_PENDING.name()
            assert orderWithEvents.events.get(0) instanceof OrderCreatedEvent
    }
}
