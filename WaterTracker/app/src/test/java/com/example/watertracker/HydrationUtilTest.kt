package com.example.watertracker

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HydrationUtilTest {

    @Test
    fun filterTopAchievers_correctFiltering() {
        val allUsers = listOf(
            TierUser(id = 1, name = "Kenzie",  daysCompliant = 7, profileImageUri = null, isMe = true),
            TierUser(id = 2, name = "Budi",    daysCompliant = 6, profileImageUri = null, isMe = false),
            TierUser(id = 3, name = "Andi",    daysCompliant = 7, profileImageUri = null, isMe = false)
        )
        val result = HydrationUtil.filterTopAchievers(allUsers)
        assertEquals(2, result.size)
        assertTrue(result.all { it.daysCompliant == 7 })
    }

    @Test
    fun filterTopAchievers_emptyList_returnsEmpty() {
        val result = HydrationUtil.filterTopAchievers(emptyList())
        assertTrue(result.isEmpty())
    }

    @Test
    fun filterTopAchievers_noMatches_returnsEmpty() {
        val users = listOf(
            TierUser(id = 1, name = "User1", daysCompliant = 0, profileImageUri = null, isMe = false),
            TierUser(id = 2, name = "User2", daysCompliant = 5, profileImageUri = null, isMe = false)
        )
        val result = HydrationUtil.filterTopAchievers(users)
        assertTrue(result.isEmpty())
    }

    @Test
    fun filterTopAchievers_allMatch_returnsAll() {
        val users = listOf(
            TierUser(id = 1, name = "User1", daysCompliant = 7, profileImageUri = null, isMe = false),
            TierUser(id = 2, name = "User2", daysCompliant = 7, profileImageUri = null, isMe = false)
        )
        val result = HydrationUtil.filterTopAchievers(users)
        assertEquals(2, result.size)
    }


    @Test
    fun calculateHydrationPercentage_normalCase() {
        assertEquals(50, HydrationUtil.calculateHydrationPercentage(1000, 2000))
    }

    @Test
    fun calculateHydrationPercentage_exactGoal_returns100() {
        assertEquals(100, HydrationUtil.calculateHydrationPercentage(2000, 2000))
    }

    @Test
    fun calculateHydrationPercentage_exceedsGoal_capsAt100() {
        assertEquals(100, HydrationUtil.calculateHydrationPercentage(3000, 2000))
    }

    @Test
    fun calculateHydrationPercentage_zeroGoal_returnsZero() {
        assertEquals(0, HydrationUtil.calculateHydrationPercentage(1000, 0))
    }

    @Test
    fun calculateHydrationPercentage_negativeIntake_returnsZero() {
        assertEquals(0, HydrationUtil.calculateHydrationPercentage(-500, 2000))
    }

    @Test
    fun calculateHydrationPercentage_negativeGoal_returnsZero() {
        assertEquals(0, HydrationUtil.calculateHydrationPercentage(1000, -2000))
    }

    @Test
    fun calculateHydrationPercentage_roundingCheck() {
        // 100/300 = 33.33... dibulatkan ke bawah menjadi 33
        assertEquals(33, HydrationUtil.calculateHydrationPercentage(100, 300))
    }

    // --- Pengujian calculateRemainingWater ---

    @Test
    fun calculateRemainingWater_normalCase() {
        assertEquals(800, HydrationUtil.calculateRemainingWater(1200, 2000))
    }

    @Test
    fun calculateRemainingWater_goalReached_returnsZero() {
        assertEquals(0, HydrationUtil.calculateRemainingWater(2000, 2000))
    }

    @Test
    fun calculateRemainingWater_goalExceeded_returnsZero() {
        assertEquals(0, HydrationUtil.calculateRemainingWater(2500, 2000))
    }

    @Test
    fun calculateRemainingWater_zeroGoal_returnsZero() {
        assertEquals(0, HydrationUtil.calculateRemainingWater(100, 0))
    }
}
