package com.example.otusproject_ermoshina.espresso

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

object HelperRecyclerView {
    fun clickChildViewWithId(id: Int): ViewAction {
        return object : ViewAction {

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun perform(uiController: UiController?, view: View?) {
                val v: View = view!!.findViewById(id)
                v.performClick()
            }

        }
    }

    fun atPosition(position: Int, matcher: Matcher<View>): Matcher<View> =
    AtPosition(position, matcher)

    private class AtPosition(
        private val position: Int,
        private val matcher: Matcher<View>,
    ) : TypeSafeMatcher<View>() {

        override fun describeTo(description: Description) {
            description.appendText("recyclerView with the specified ID and at position: $position")
        }

        override fun matchesSafely(item: View?): Boolean {
            if (item !is RecyclerView) return false
            val viewHolder = item.findViewHolderForAdapterPosition(position)
                ?: return false
            return matcher.matches(viewHolder.itemView)
        }

    }
}
