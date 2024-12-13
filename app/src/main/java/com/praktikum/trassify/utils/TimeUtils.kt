package com.praktikum.trassify.utils

import java.text.SimpleDateFormat
import java.util.*

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss", Locale.getDefault())
    val date = Date(timestamp)
    return sdf.format(date)
}

fun extractDateAndTime(timestamp: String): Pair<String, String> {
    val parts = timestamp.split(" ") // Pisahkan berdasarkan spasi
    val date = "${parts[0]} ${parts[1]} ${parts[2]} ${parts[3]}" // Contoh: "Senin, 17 Agustus 1945"
    val time = parts[4] // Contoh: "14:45:30"
    return Pair(date, time)
}

