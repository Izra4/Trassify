package com.praktikum.trassify.data.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val imageUrl: String = "",
    val point: Int = 0,
    val timestamp: String = ""
)
