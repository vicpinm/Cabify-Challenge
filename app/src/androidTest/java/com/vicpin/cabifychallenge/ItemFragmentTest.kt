package com.vicpin.cabifychallenge



import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vicpin.cabifychallenge.activity.MainActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.vicpin.cabifychallenge.domain.repository.ICheckoutRepository
import com.vicpin.cabifychallenge.domain.model.Item
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import org.junit.runner.RunWith

import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.test.KoinTest

/**
 * IMPORTANT: Disable animations on the device before running test
 */
@RunWith(AndroidJUnit4::class)
class ItemFragmentTest: KoinTest {

    //Test items
    private val VOUCHER =
        Item(code = "VOUCHER", name = "VOUCHER name", price = 5.0)
    private val TSHIRT =
        Item(code = "TSHIRT", name = "TSHIRT name", price = 20.0)
    private val MUG = Item(code = "MUG", name = "MUG name", price = 7.5)

    @Test
    fun testProgressIsHiddenWhenDataIsLoaded() {
        //Given no items
        createFakeRepoWith(listOf())

        //When MainActivity is launched
        launchActivity<MainActivity>()

        //ProgressBar is not displayed when data is loaded
        onViewId(R.id.progressBar).check(matches(not(isDisplayed())))
    }


    @Test
    fun testLongItemCollectionIsLoadedWhenReturnedFromRepo() {
        //Given some items
        val collectionSize = 10000
        createFakeRepoWith(getFakeItems(collectionSize))

        //When MainActivity is launched
        launchActivity<MainActivity>()

        //ProgressBar is displayed
        onViewId(R.id.recycler).check(matches(hasAdapterSize(collectionSize)))
    }

    @Test
    fun testRowsAreDisplayedCorrectInfo() {
        //Given some items
        val collectionSize = 100
        val items = getFakeItems(collectionSize)
        createFakeRepoWith(items)

        //When MainActivity is launched
        launchActivity<MainActivity>()

        //Then: each row shows correct text
        items.forEachIndexed { index, item ->
            onViewId(R.id.recycler).scrollTo(index)
            onView(ViewMatchers.withText(item.name)).check(matches(isDisplayed()))
            onView(ViewMatchers.withText(item.price.toString())).check(matches(isDisplayed()))
        }
    }


    fun getFakeItems(size: Int) = List(size) {
        Item(
            "Item code $it",
            "Description $it",
            it.toDouble()
        )
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
