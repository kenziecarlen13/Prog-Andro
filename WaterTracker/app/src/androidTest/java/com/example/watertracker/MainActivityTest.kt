package com.example.watertracker

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testInitialState_homeFragmentIsDisplayed() {
        // Cek apakah elemen utama HomeFragment muncul saat start
        onView(withId(R.id.progressCircleIndicator)).check(matches(isDisplayed()))
        onView(withId(R.id.fabAddWater)).check(matches(isDisplayed()))
        onView(withId(R.id.textProgress)).check(matches(withText(containsString("ml"))))
    }

    @Test
    fun testNavigation_toReminderFragment() {
        // Klik menu Reminder di Bottom Navigation
        onView(withId(R.id.nav_reminder)).perform(click())

        // Pastikan view di ReminderFragment muncul
        onView(withId(R.id.btnSetReminder)).check(matches(isDisplayed()))
        onView(withId(R.id.timePicker)).check(matches(isDisplayed()))
    }

    @Test
    fun testNavigation_toProfileFragment() {
        // Klik menu Profile di Bottom Navigation
        onView(withId(R.id.nav_profile)).perform(click())

        // Pastikan view di ProfileFragment muncul
        onView(withId(R.id.btnOpenWeb)).check(matches(isDisplayed()))
        onView(withId(R.id.btnExit)).check(matches(isDisplayed()))
        onView(withText("Settings")).check(matches(isDisplayed()))
    }

    @Test
    fun testAddWaterInteraction() {
        // Simulasikan klik tombol tambah air di HomeFragment
        onView(withId(R.id.fabAddWater)).perform(click())

        // Verifikasi bahwa teks progres masih ditampilkan (integrasi UI sederhana)
        onView(withId(R.id.textProgress)).check(matches(isDisplayed()))
    }
}
