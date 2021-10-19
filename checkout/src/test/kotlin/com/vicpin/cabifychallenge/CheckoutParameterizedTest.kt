package com.vicpin.cabifychallenge

import com.google.common.truth.Truth.assertWithMessage
import com.vicpin.cabifychallenge.checkout.Checkout
import com.vicpin.cabifychallenge.model.Discount
import com.vicpin.cabifychallenge.model.ItemPrice
import com.vicpin.cabifychallenge.strategy.DiscountStrategy2x1
import com.vicpin.cabifychallenge.strategy.DiscountStrategyBulk
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized


@RunWith(Parameterized::class)
class CheckoutParameterizedTest {

    /**
     * Parameterized test inputs
     */
    companion object {

        //Test items
        private val VOUCHER = object: ItemPrice { override val code = "VOUCHER"; override val price = 5.0 }
        private val TSHIRT = object: ItemPrice { override val code = "TSHIRT"; override val price = 20.0 }
        private val MUG = object: ItemPrice { override val code = "MUG"; override val price = 7.5 }

        //Discounts
        private val voucher2x1Discount = listOf(Discount<ItemPrice>(VOUCHER, DiscountStrategy2x1()))
        private val tShirtBulkDiscount = listOf(Discount<ItemPrice>(TSHIRT, DiscountStrategyBulk(minAmount = 3)))
        private val allDiscounts = voucher2x1Discount + tShirtBulkDiscount

        @Parameterized.Parameters
        @JvmStatic
        fun data(): Array<Any> {
            return arrayOf(

                /**
                 * No discounts applied
                 */
                TestParams(listOf(VOUCHER), total = 5.0),
                TestParams(listOf(TSHIRT), total = 20.0),
                TestParams(listOf(MUG), total = 7.5),
                TestParams(listOf(VOUCHER, TSHIRT, MUG), total = 32.5),
                TestParams(listOf(VOUCHER, TSHIRT, VOUCHER), total = 30.0),
                TestParams(listOf(TSHIRT, TSHIRT, TSHIRT, VOUCHER, TSHIRT), total = 85.0),
                TestParams(listOf(VOUCHER, TSHIRT, VOUCHER, VOUCHER, MUG, TSHIRT, TSHIRT), total = 82.5),
                TestParams(listOf(VOUCHER, VOUCHER, VOUCHER), total = 15.0),
                TestParams(listOf(VOUCHER, VOUCHER, VOUCHER, VOUCHER), total = 20.0),
                TestParams(listOf(TSHIRT, TSHIRT, TSHIRT), total = 60.0),
                TestParams(listOf(TSHIRT, TSHIRT, TSHIRT, TSHIRT), total = 80.0),

                /**
                 * Only 2x1 discount applied on VOUCHER
                 */
                TestParams(listOf(VOUCHER), voucher2x1Discount, total = 5.0),
                TestParams(listOf(TSHIRT), voucher2x1Discount, total = 20.0),
                TestParams(listOf(MUG), voucher2x1Discount, total = 7.5),
                TestParams(listOf(VOUCHER, TSHIRT, MUG), voucher2x1Discount, total = 32.5),
                TestParams(listOf(VOUCHER, TSHIRT, VOUCHER), voucher2x1Discount, total = 25.0),
                TestParams(listOf(TSHIRT, TSHIRT, TSHIRT, VOUCHER, TSHIRT), voucher2x1Discount, total = 85.0),
                TestParams(listOf(VOUCHER, TSHIRT, VOUCHER, VOUCHER, MUG, TSHIRT, TSHIRT), voucher2x1Discount, total = 77.5),
                TestParams(listOf(VOUCHER, VOUCHER, VOUCHER), voucher2x1Discount, total = 10.0),
                TestParams(listOf(VOUCHER, VOUCHER, VOUCHER, VOUCHER), voucher2x1Discount, total = 10.0),
                TestParams(listOf(TSHIRT, TSHIRT, TSHIRT), voucher2x1Discount, total = 60.0),
                TestParams(listOf(TSHIRT, TSHIRT, TSHIRT, TSHIRT), voucher2x1Discount, total = 80.0),

                /**
                 * Only Bulk discount applied on TSHIRT if there are 3 or more tshirts
                 */
                TestParams(listOf(VOUCHER), total = 5.0),
                TestParams(listOf(TSHIRT), total = 20.0),
                TestParams(listOf(MUG), total = 7.5),
                TestParams(listOf(VOUCHER, TSHIRT, MUG), tShirtBulkDiscount, total = 32.5),
                TestParams(listOf(VOUCHER, TSHIRT, VOUCHER), tShirtBulkDiscount, total = 30.0),
                TestParams(listOf(TSHIRT, TSHIRT, TSHIRT, VOUCHER, TSHIRT), tShirtBulkDiscount, total = 81.0),
                TestParams(listOf(VOUCHER, TSHIRT, VOUCHER, VOUCHER, MUG, TSHIRT, TSHIRT), tShirtBulkDiscount, total = 79.5),
                TestParams(listOf(VOUCHER, VOUCHER, VOUCHER), tShirtBulkDiscount, total = 15.0),
                TestParams(listOf(VOUCHER, VOUCHER, VOUCHER, VOUCHER), tShirtBulkDiscount, total = 20.0),
                TestParams(listOf(TSHIRT, TSHIRT, TSHIRT), tShirtBulkDiscount, total = 57.0),
                TestParams(listOf(TSHIRT, TSHIRT, TSHIRT, TSHIRT), tShirtBulkDiscount, total = 76.0),

                /**
                 * Both discounts applied
                 */
                TestParams(listOf(VOUCHER), allDiscounts, total = 5.0),
                TestParams(listOf(TSHIRT), allDiscounts, total = 20.0),
                TestParams(listOf(MUG), allDiscounts, total = 7.5),
                TestParams(listOf(VOUCHER, TSHIRT, MUG), allDiscounts, total = 32.5),
                TestParams(listOf(VOUCHER, TSHIRT, VOUCHER), allDiscounts, total = 25.0),
                TestParams(listOf(TSHIRT, TSHIRT, TSHIRT, VOUCHER, TSHIRT), allDiscounts, total = 81.0),
                TestParams(listOf(VOUCHER, TSHIRT, VOUCHER, VOUCHER, MUG, TSHIRT, TSHIRT), allDiscounts, total = 74.5),
                TestParams(listOf(VOUCHER, VOUCHER, VOUCHER), allDiscounts, total = 10.0),
                TestParams(listOf(VOUCHER, VOUCHER, VOUCHER, VOUCHER), allDiscounts, total = 10.0),
                TestParams(listOf(TSHIRT, TSHIRT, TSHIRT), allDiscounts, total = 57.0),
                TestParams(listOf(TSHIRT, TSHIRT, TSHIRT, TSHIRT), allDiscounts, total = 76.0))

        }
    }


    @Parameterized.Parameter
    lateinit var inputParams: TestParams

    private lateinit var checkout: Checkout<ItemPrice>

    @Before
    fun init() {
        checkout = Checkout()
    }

    @Test
    fun given_listOfItems_then_totalIsCorrect() {
        //Given some items and discounts
        checkout.discounts = inputParams.discounts
        checkout.items = inputParams.items

        //Then total is correct
        assertWithMessage("Input params: $inputParams").that(checkout.getTotal()).isEqualTo(inputParams.total)
    }

}

data class TestParams(val items: List<ItemPrice>, val discounts: List<Discount<ItemPrice>> = listOf(), val total: Double)
