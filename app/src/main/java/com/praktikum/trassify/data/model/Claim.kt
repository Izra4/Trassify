package com.praktikum.trassify.data.model

data class Claim(
    val id: String = "",
    val userId: String = "",
    val merchandiseId: String = "",
    val pointUsed: Int = 0,
    val address: String = "",
    val phoneNumber: String = "",
    val zipCode: String = "",
    val note: String = "",
    val claimed: Boolean = false
)
