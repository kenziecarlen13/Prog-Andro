package com.example.watertracker

data class TierUser(
    val id: Int,
    val name: String,
    val daysCompliant: Int,
    val profileImageUri: String?,
    val isMe: Boolean
)
