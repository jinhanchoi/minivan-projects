package order.config

import order.events.OrderCreatedEvent
import orderservice.message.EventPublisher
import orderservice.message.OrderEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HandlerConfigure {
    @Bean
    fun orderDomainHandler() : EventPublisher<OrderCreatedEvent, String, String> = OrderEventPublisher()
}