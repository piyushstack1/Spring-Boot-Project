package airbnb.example.airbnb.strategy;

import airbnb.example.airbnb.entity.Inventory;

import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal calculatePrice(Inventory inventory);
}
