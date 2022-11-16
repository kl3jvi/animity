package com.kl3jvi.benchmark

import androidx.benchmark.macro.ExperimentalBaselineProfilesApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.uiautomator.By
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalBaselineProfilesApi::class)
class BaselineProfileGenerator {

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generate() = baselineProfileRule.collectBaselineProfile(
        packageName = "com.kl3jvi.animity",
        profileBlock = {
            pressHome()
            startActivityAndWait()

            device.findObject(By.text("Home"))
            device.findObject(By.text("Explore"))
            device.findObject(By.text("Favorites"))
            device.findObject(By.text("Profile"))
        }
    )

}