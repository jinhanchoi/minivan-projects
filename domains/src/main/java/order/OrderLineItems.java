package order;

import order.common.Money;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @ElementCollection
    @CollectionTable(name = "order_line_items", joinColumns = @JoinColumn(name="order_id"))
    private List<OrderLineItem> lineItems;

    private OrderLineItems() {
    }
    public OrderLineItems(List<OrderLineItem> lineItems) {
        this.lineItems = lineItems;
    }
    Money orderTotal() {
        return lineItems.stream().map(OrderLineItem::getTotal).reduce(Money.ZERO, Money::add);
    }
}
