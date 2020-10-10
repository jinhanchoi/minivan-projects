package order.events;

import order.OrderLineItem;
import order.common.Money;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class OrderDetails {

    private List<OrderLineItem> lineItems;
    private Money orderTotal;

    private long restaurantId;
    private long customerId;

    private OrderDetails() {
    }

    public Money getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(Money orderTotal) {
        this.orderTotal = orderTotal;
    }

    public OrderDetails(long customerId, long restaurantId, List<OrderLineItem> lineItems, Money orderTotal) {
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.lineItems = lineItems;
        this.orderTotal = orderTotal;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public List<OrderLineItem> getLineItems() {
        return lineItems;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public long getCustomerId() {
        return customerId;
    }


    public void setLineItems(List<OrderLineItem> lineItems) {
        this.lineItems = lineItems;
    }


    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }


}
