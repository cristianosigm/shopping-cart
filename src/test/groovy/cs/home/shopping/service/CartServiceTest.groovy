package cs.home.shopping.service

import cs.home.shopping.model.entity.Customer
import spock.lang.Specification

class CartServiceTest extends Specification {

    def customerVIP = Customer.builder()
            .id(1L)
            .name("Mocked Customer VIP")
            .isVIP(true)
            .build()
    def customerRegular = Customer.builder()
            .id(2L)
            .name("Mocked Customer Regular")
            .isVIP(false)
            .build()

    def "when adding #qty items of Product #productId then result should be #result "() {
        expect:
        1 == 1

        where:
        productId | qty | result
        1         | 1   | true
        2         | 3   | true
    }

    def "when removing #qty items of Product #productId then result should be #result "() {
        given:
        // declare the amount of products here

        expect:
        1 == 1

        where:
        productId | qty | result
        1         | 1   | true
        2         | 3   | true
    }

    def "when Products has #qty items and Customer is not VIP then Discount should be #discount"() {
        expect:
        qty > 0
        !customerRegular.isVIP
        discount < 101

        where:
        qty | discount
        1   | 0
        2   | 0
        3   | 100
    }

    def "when adding a product that was already added then increase quantity"() {
        expect:
        1 == 1
    }

}
