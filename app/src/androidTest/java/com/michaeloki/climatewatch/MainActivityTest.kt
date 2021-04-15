package com.michaeloki.climatewatch

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import org.hamcrest.Matchers.allOf
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION"
        )

    @Test
    fun mainActivityTest2() {
        val linearLayout = onView(
            allOf(
                withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java)),
                isDisplayed()
            )
        )
        linearLayout.check(matches(isDisplayed()))

        val frameLayout = onView(
            allOf(
                withId(R.id.action_bar_container),
                withParent(
                    allOf(
                        withId(R.id.decor_content_parent),
                        withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        frameLayout.check(matches(isDisplayed()))

        val viewGroup = onView(
            allOf(
                withId(R.id.action_bar),
                withParent(
                    allOf(
                        withId(R.id.action_bar_container),
                        withParent(withId(R.id.decor_content_parent))
                    )
                ),
                isDisplayed()
            )
        )
        viewGroup.check(matches(isDisplayed()))

        val textView = onView(
            allOf(
                withText("ClimateWatch"),
                withParent(
                    allOf(
                        withId(R.id.action_bar),
                        withParent(withId(R.id.action_bar_container))
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("ClimateWatch")))

        val frameLayout2 = onView(
            allOf(
                withId(android.R.id.content),
                withParent(
                    allOf(
                        withId(R.id.decor_content_parent),
                        withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        frameLayout2.check(matches(isDisplayed()))

        val viewGroup2 = onView(
            allOf(
                withParent(
                    allOf(
                        withId(android.R.id.content),
                        withParent(withId(R.id.decor_content_parent))
                    )
                ),
                isDisplayed()
            )
        )
        viewGroup2.check(matches(isDisplayed()))

        val cardView = onView(
            allOf(
                withId(R.id.weatherCard),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        cardView.check(matches(isDisplayed()))

        val linearLayout2 = onView(
            allOf(
                withParent(
                    allOf(
                        withId(R.id.weatherCard),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        linearLayout2.check(matches(isDisplayed()))

        val textView2 = onView(
            allOf(
                withId(R.id.currentCity), isDisplayed(),
                withParent(withParent(withId(R.id.weatherCard))),
                isDisplayed()
            )
        )
        textView2.check(matches(isDisplayed()))

        val recyclerView = onView(
            allOf(
                withId(R.id.list_locations),
                withParent(withParent(withId(R.id.weatherCard))),
                isDisplayed()
            )
        )
        recyclerView.check(matches(isDisplayed()))

        val imageView = onView(
            allOf(
                withId(R.id.weatherIcon),
                withParent(withParent(withId(R.id.list_locations))),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))

        val textView3 = onView(
            allOf(
                withId(R.id.city_item), isEnabled(),

                withParent(withParent(withId(R.id.list_locations))),
                isDisplayed()
            )
        )
        textView3.check(matches(
        (isDisplayed())))

        val textView4 = onView(
            allOf(
                withId(R.id.maxTemp), isEnabled(),
                withParent(withParent(withId(R.id.list_locations))),
                isDisplayed()
            )
        )
        textView4.check(matches(
            (isDisplayed())
        ))

        val button = onView(
            allOf(
                withId(R.id.refreshButton), withText("REFRESH"),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val button2 = onView(
            allOf(
                withId(R.id.refreshButton), withText("REFRESH"),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        button2.check(matches(isDisplayed()))
    }
}
