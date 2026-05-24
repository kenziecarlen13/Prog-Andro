package com.example.watertracker

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Task 6 (Modul 12): Local Unit Tests for HydrationUtil.
 *
 * Run with: ./gradlew test
 */
class HydrationUtilTest {

    @Test
    fun filterTopAchievers_correctFiltering() {
        // Arrange: Create a dummy list with a mix of daysCompliant values
        val allUsers = listOf(
            TierUser(id = 1, name = "Kenzie",  daysCompliant = 7, profileImageUri = null, isMe = true),
            TierUser(id = 2, name = "Budi",    daysCompliant = 6, profileImageUri = null, isMe = false),
            TierUser(id = 3, name = "Andi",    daysCompliant = 7, profileImageUri = null, isMe = false),
            TierUser(id = 4, name = "Siti",    daysCompliant = 2, profileImageUri = null, isMe = false),
            TierUser(id = 5, name = "Dewi",    daysCompliant = 0, profileImageUri = null, isMe = false),
            TierUser(id = 6, name = "Reza",    daysCompliant = 7, profileImageUri = null, isMe = false),
            TierUser(id = 7, name = "Fajar",   daysCompliant = 4, profileImageUri = null, isMe = false)
        )

        // Expected top achievers: only users with daysCompliant == 7
        val expectedTopAchievers = listOf(
            allUsers[0], // Kenzie  - 7 days
            allUsers[2], // Andi    - 7 days
            allUsers[5]  // Reza    - 7 days
        )

        // Act: Call the function under test
        val result = HydrationUtil.filterTopAchievers(allUsers)

        // Assert: Verify size and content match expected top achievers
        assertEquals("Filtered list should contain exactly 3 top achievers", 3, result.size)
        assertEquals("First top achiever should be Kenzie",  expectedTopAchievers[0], result[0])
        assertEquals("Second top achiever should be Andi",   expectedTopAchievers[1], result[1])
        assertEquals("Third top achiever should be Reza",    expectedTopAchievers[2], result[2])

        // Assert: All results have daysCompliant == 7
        result.forEach { user ->
            assertEquals("Every user in result must have daysCompliant == 7", 7, user.daysCompliant)
        }
    }
}
