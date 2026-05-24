package com.example.watertracker

/**
 * Task 6 (Modul 12): Utility object for hydration-related logic.
 * Separated into a standalone object to enable easy unit testing.
 */
object HydrationUtil {

    /**
     * Filters a list of TierUser and returns only users who completed
     * all 7 days (top achievers with "Hydro Archon" badge).
     *
     * @param users The full list of TierUser objects.
     * @return A filtered list containing only users where daysCompliant == 7.
     */
    fun filterTopAchievers(users: List<TierUser>): List<TierUser> {
        return users.filter { it.daysCompliant == 7 }
    }
}
