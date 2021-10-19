package com.vicpin.cabifychallenge



import androidx.test.core.app.launchActivity
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vicpin.cabifychallenge.activity.MainActivity
import com.vicpin.cabifychallenge.domain.repository.ICheckoutRepository
import com.vicpin.cabifychallenge.domain.model.Item
import org.hamcrest.Matchers.containsString
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.test.KoinTest

/**
 * IMPORTANT: Disable animations on the device before running test
 */
@RunWith(AndroidJUnit4::class)
class CheckoutFragmentTest: KoinTest {

    //Test items
    private val VOUCHER =
        Item(code = "VOUCHER", name = "VOUCHER name", price = 5.0)
    private val TSHIRT =
        Item(code = "TSHIRT", name = "TSHIRT name", price = 20.0)
    private val MUG = Item(code = "MUG", name = "MUG name", price = 7.5)


    @Test
    fun testShoppingListIsEmptyAtFirst() {
        //Given some items
        createFakeRepoWith(listOf(VOUCHER, TSHIRT, MUG))

        //When MainActivity is launched
        launchActivity<MainActivity>()

        //Shopping list is empty
        onViewId(R.id.shoppingList).check(matches(hasAdapterSize(0)))
    }

    @Test
    fun testItemsAreAddedToShoppingListWhenAddButtonIsClicked() {
        //Given some items
        val items = listOf(VOUCHER, TSHIRT, MUG)
        createFakeRepoWith(items)

        //When MainActivity is launched
        launchActivity<MainActivity>()

        //And click on each add button
        items.forEachIndexed { index, _ ->
            onViewId(R.id.recycler).scrollTo(index)
            onViewId(R.id.recycler).clickAtPosition(index, R.id.addButton)
        }

        //Then: shopping list has correct size
        onViewId(R.id.shoppingList).check(matches(hasAdapterSize(items.size)))
    }

    @Test
    fun testItemsAreDeletedFromShoppingListWhenRemoveButtonIsClicked() {
        //Given some items
        val items = listOf(VOUCHER, TSHIRT, MUG)
        createFakeRepoWith(items)

        //When MainActivity is launched
        launchActivity<MainActivity>()

        //click on each add button
        items.forEachIndexed { index, _ ->
            onViewId(R.id.recycler).clickAtPosition(index, R.id.addButton)
        }

        //click on each remove button
        items.forEachIndexed { index, _ ->
            onViewId(R.id.shoppingList).clickAtPosition(0, R.id.removeButton)
        }

        //Then: shopping list is empty
        onViewId(R.id.shoppingList).check(matches(hasAdapterSize(0)))
    }


    @Test
    fun testTotalWithNoDiscounts() {
        //Given some items
        val items = listOf(VOUCHER, TSHIRT, MUG)
        createFakeRepoWith(items)

        //When MainActivity is launched
        launchActivity<MainActivity>()

        //click on each add button
        items.forEachIndexed { index, _ ->
            onViewId(R.id.recycler).clickAtPosition(index, R.id.addButton)
        }

        //Then: total is 32.5
        onViewId(R.id.total).check(matches(withText(containsString("32.5"))))
    }

    @Test
    fun test2x1DiscountOnVoucher() {
        //Given some items
        val items = listOf(VOUCHER, TSHIRT, MUG)
        createFakeRepoWith(items)

        //When MainActivity is launched
        launchActivity<MainActivity>()

        //click on VOUCHER, TSHIRT and VOUCHER
        onViewId(R.id.recycler).apply {
            clickAtPosition(items.indexOf(VOUCHER), R.id.addButton)
            clickAtPosition(items.indexOf(TSHIRT), R.id.addButton)
            clickAtPosition(items.indexOf(VOUCHER), R.id.addButton)
        }

        //Then: total is 25.0
        onViewId(R.id.total).check(matches(withText(containsString("25.0"))))
    }

    @Test
    fun testBulkDiscountOnTShirt() {
        //Given some items
        val items = listOf(VOUCHER, TSHIRT, MUG)
        createFakeRepoWith(items)

        //When MainActivity is launched
        launchActivity<MainActivity>()

        //click on TSHIRT, TSHIRT TSHIRT, VOUCHER and TSHIRT
        onViewId(R.id.recycler).apply {
            clickAtPosition(items.indexOf(TSHIRT), R.id.addButton)
            onViewId(R.id.header).perform(click())
            clickAtPosition(items.indexOf(TSHIRT), R.id.addButton)
            clickAtPosition(items.indexOf(TSHIRT), R.id.addButton)
            clickAtPosition(items.indexOf(VOUCHER), R.id.addButton)
            clickAtPosition(items.indexOf(TSHIRT), R.id.addButton)
        }

        //Then: total is 81.0
        onViewId(R.id.total).check(matches(withText(containsString("81.0"))))
    }

    @Test
    fun testBothDiscounts() {
        //Given some items
        val items = listOf(VOUCHER, TSHIRT, MUG)
        createFakeRepoWith(items)

        //When MainActivity is launched
        launchActivity<MainActivity>()

        //click on the following items
        onViewId(R.id.recycler).apply {
            clickAtPosition(items.indexOf(VOUCHER), R.id.addButton)
            onViewId(R.id.header).perform(click())
            clickAtPosition(items.indexOf(TSHIRT), R.id.addButton)
            clickAtPosition(items.indexOf(VOUCHER), R.id.addButton)
            clickAtPosition(items.indexOf(VOUCHER), R.id.addButton)
            clickAtPosition(items.indexOf(MUG), R.id.addButton)
            clickAtPosition(items.indexOf(TSHIRT), R.id.addButton)
            clickAtPosition(items.indexOf(TSHIRT), R.id.addButton)
        }

        //Then: total is 74.5
        onViewId(R.id.total).check(matches(withText(containsString("74.5"))))
    }

    fun createFakeRepoWith(items: List<Item>) {
        loadKoinModules(listOf(module(override = true) {
            single<ICheckoutRepository> {
                object: ICheckoutRepository {
                    override suspend fun getData(): List<Item> {
                        return items
                    }
                }
            }
        }))
    }
}
