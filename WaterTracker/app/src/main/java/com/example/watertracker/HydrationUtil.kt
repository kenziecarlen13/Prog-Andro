package com.example.watertracker

/**
 * Task 6 (Modul 12): Utility object for hydration-related logic.
 */
object HydrationUtil {

    /**
     * Filters a list of TierUser and returns only top achievers.
     */
    fun filterTopAchievers(users: List<TierUser>): List<TierUser> {
        return users.filter { it.daysCompliant == 7 }
    }

    /**
     * Calculates the hydration percentage based on current intake and goal.
     * Returns a value between 0 and 100.
     */
    fun calculateHydrationPercentage(currentIntake: Int, dailyGoal: Int): Int {
        if (dailyGoal <= 0) return 0
        val percentage = (currentIntake.toFloat() / dailyGoal.toFloat() * 100).toInt()
        return percentage.coerceIn(0, 100)
    }

    /**
     * Calculates the remaining water needed to reach the goal in ml.
     * Returns 0 if goal is reached or exceeded.
     */
    fun calculateRemainingWater(currentIntake: Int, dailyGoal: Int): Int {
        val remaining = dailyGoal - currentIntake
        return remaining.coerceAtLeast(0)
    }
}
