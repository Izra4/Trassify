package com.praktikum.trassify.data.model

data class Merchandise(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val points: Int = 0,
    val redeemedCount: Int = 0,
    val reviews: Map<String, Review> = emptyMap() // Menyimpan review sebagai map dengan key berupa ID reviewer
)
