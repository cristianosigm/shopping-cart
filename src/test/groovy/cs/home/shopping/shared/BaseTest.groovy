package cs.home.shopping.shared

import cs.home.shopping.model.entity.*
import spock.lang.Specification

abstract class BaseTest extends Specification {

    def cartVIP = Cart.builder()
            .id(1)
            .customerId(1)
            .items(Arrays.asList(CartItem.builder()
                    .id(1)
                    .product(shirt)
                    .quantity(1)
                    .build(),
                    CartItem.builder()
                            .id(1)
                            .product(jeans)
                            .quantity(1)
                            .build()))
            .customerIsVIP(Boolean.TRUE)
            .build()

    def orderItemsOne = Arrays.asList(
            OrderItem.builder()
                    .id(1)
                    .orderId(1)
                    .product(jeans)
                    .quantity(1)
                    .build()
    )

    def orderItemsTwo = Arrays.asList(
            OrderItem.builder()
                    .id(1)
                    .orderId(1)
                    .product(jeans)
                    .quantity(1)
                    .build(),
            OrderItem.builder()
                    .id(1)
                    .orderId(1)
                    .product(shirt)
                    .quantity(3)
                    .build()
    )

    def customerVIP = Customer.builder()
            .id(1)
            .name("Mocked Customer VIP")
            .isVIP(true)
            .build()

    def customerRegular = Customer.builder()
            .id(2)
            .name("Mocked Customer Regular")
            .isVIP(false)
            .build()

    def shirt = Product.builder()
            .id(1)
            .name("T-Shirt")
            .description("A nice t-shirt")
            .price(BigDecimal.valueOf(35.99))
            .build()

    def jeans = Product.builder()
            .id(2)
            .name("Jeans")
            .description("A nice jeans")
            .price(BigDecimal.valueOf(65.50))
            .build()

    def dress = Product.builder()
            .id(3)
            .name("Dress")
            .description("A nice dress!")
            .price(BigDecimal.valueOf(80.75))
            .build()

    def promotionVIP = Promotion.builder()
            .id(1)
            .name("VIP Discount")
            .description("15% off for VIPs")
            .discountPercent(BigDecimal.valueOf(15))
            .requiresVIP(Boolean.TRUE)
            .build()

    def promotionItems = Promotion.builder()
            .id(2)
            .name("Get 3 pay 2")
            .description("One item free when buying 3 or more")
            .minimumQuantity(3)
            .freeItems(1)
            .build()

    def promotionInactive = Promotion.builder()
            .id(3)
            .name("Inactive")
            .description("Old promotion no longer active")
            .minimumQuantity(3)
            .freeItems(1)
            .active(Boolean.FALSE)
            .build()

    protected generateItems(int numberOfDresses, int numberOfJeans, int numberOfShirts) {
        final List<CartItem> items = new ArrayList<>()
        if (numberOfDresses > 0) {
            items.add(CartItem.builder()
                    .id(100)
                    .product(dress)
                    .quantity(numberOfDresses)
                    .build())
        }
        if (numberOfJeans > 0) {
            items.add(CartItem.builder()
                    .id(200)
                    .product(jeans)
                    .quantity(numberOfJeans)
                    .build())
        }
        if (numberOfShirts > 0) {
            items.add(CartItem.builder()
                    .id(300)
                    .product(shirt)
                    .quantity(numberOfShirts)
                    .build())
        }
        return items
    }

    protected generatePromotions() {
        return Arrays.asList(
                Promotion.builder()
                        .id(1)
                        .name("VIP Promotion")
                        .description("VIP promotion - 15% off")
                        .requiresVIP(Boolean.TRUE)
                        .discountPercent(BigDecimal.valueOf(15))
                        .build(),

                Promotion.builder()
                        .id(2L)
                        .name("Get 3 for the price of 2")
                        .description("When purchasing 3 or more items, one is free.")
                        .minimumQuantity(3)
                        .build())
    }

}
