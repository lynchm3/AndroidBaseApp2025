package com.marklynch.steamdeck.ui

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.marklynch.steamdeck.ui.main.MainActivity
import com.marklynch.steamdeck.ui.main.MainScreen
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun myTest() {
        activityRule.scenario.onActivity { activity ->
            activity.setContent {
                MainScreen()
            }
        }

        composeTestRule.onNodeWithText("Edit").assertExists()
    }
}