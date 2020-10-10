package order.web;

import order.DeliveryInformation;
import order.Order;
import order.OrderLineItem;
import order.common.Address;
import order.common.MenuItem;
import order.common.Money;
import order.common.Restaurant;
import order.events.OrderCreatedEvent;
import order.events.OrderDomainEvent;
import order.events.ResultWithDomainEvents;
import order.infra.OrderRepo;
import orderservice.message.EventPublisher;
import orderservice.message.OrderEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RequestMapping("/home")
@RestController
public class ApiController {

    @Autowired
    OrderRepo orderRepo;
    @Autowired
    EventPublisher<OrderCreatedEvent,String,String> publisher;

    @Transactional
    @GetMapping
    public String getOrderName(){

        MenuItem menuItem = new MenuItem("ICE","ICE_CREAM",new Money(3000));
        Restaurant restaurant = new Restaurant(1L, "SUPER_MARKET", Collections.singletonList(menuItem));
        Address addr  = new Address("saemal-ro 93", "107-2106","seoul","guro-gu","08288");
        DeliveryInformation deliveryInformation = new DeliveryInformation(LocalDateTime.now(),addr);

        List<OrderLineItem> orderLineItems = Collections.singletonList(new OrderLineItem("ICE_CREAM", "JINHAN", new Money(3000), 3));
        ResultWithDomainEvents<Order, OrderDomainEvent> orderWithEvents = Order.createOrder(123L,restaurant,deliveryInformation,orderLineItems);
        orderRepo.save(orderWithEvents.result);

        publisher.process("TestMessage");

        return "test";
    }

}
