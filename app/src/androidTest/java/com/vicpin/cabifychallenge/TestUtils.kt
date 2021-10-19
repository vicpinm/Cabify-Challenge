package com.vicpin.cabifychallenge

import android.app.Application
import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.orchestrator.junit.BundleJUnitUtils.getDescription
import com.vicpin.kpresenteradapter.PresenterAdapter
import org.hamcrest.Description
import android.R
import androidx.test.espresso.UiController
import org.hamcrest.Matcher
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition


fun getApplication() = ApplicationProvider.getApplicationContext() as Application
fun hasAdapterSize(size: Int) = AdapterCountMarcher(size)
fun ViewInteraction.scrollTo(position: Int) = perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position))
fun onViewId(@IdRes id: Int) = Espresso.onView(ViewMatchers.withId(id))
fun clickOnView(id: Int) = ClickOnViewId(id)
fun ViewInteraction.clickAtPosition(position: Int, viewId: Int) = perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(position, clickOnView(viewId)))

class AdapterCountMarcher(val size: Int): BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {

    var currentItemsSize = 0

    override fun describeTo(description: Description) {
        description.appendText("adapter expected items count: $size, found $currentItemsSize")
    }

    override fun matchesSafely(recycler: RecyclerView): Boolean {
        this.currentItemsSize = recycler.adapter?.itemCount ?: -1
        return (recycler.adapter as? PresenterAdapter<*>)?.getData()?.size == size
    }
}

class ClickOnViewId(val viewId: Int): ViewAction {
    var click = click()

    override fun getConstraints(): Matcher<View> {
        return click.constraints
    }

    override fun getDescription(): String {
        return " click on view"
    }

    override fun perform(uiController: UiController, view: View) {
        click.perform(uiController, view.findViewById<View>(viewId))
    }
}