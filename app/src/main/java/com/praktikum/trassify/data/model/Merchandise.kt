package com.praktikum.trassify.data.model

data class Merchandise(
    val id: String = "",
    val name: String = "",
    val imageUrl: String = "",
    val description: String = "",
    val pointRequired: Int = 0,
    val claimed: Int = 0,
    val stock: Int = 0,
    val timestamp:String = ""
)
