package com.praktikum.trassify.constants

import com.praktikum.trassify.R

val welcomeContent : List<WelcomeRepository> = listOf(
    WelcomeRepository(
        image = R.drawable.welcome1,
        title = "Selamat Datang di Trassify",
        description = "Jaga kebersihan lingkungan kini lebih mudah! Dengan Trassify, Kamu dapat membantu menjaga kota tetap bersih, melaporkan sampah, dan mendapatkan reward dari aksi peduli lingkungan Kamu!"
    ),
    WelcomeRepository(
        image = R.drawable.welcome2,
        title = "Jadwal Pemungutan Sampah",
        description = "Dapatkan notifikasi tentang jadwal pemungutan sampah di daerah Kamu. Tidak ada lagi kekhawatiran tentang sampah yang menumpuk!"
    ),
    WelcomeRepository(
        image = R.drawable.welcome3,
        title = "Lokasi Sampah di Sekitarmu",
        description = "Lihat sampah? Foto, laporkan, dan biarkan kami yang menindaklanjuti! Aplikasi ini akan secara otomatis mencatat lokasi sampah dan mengirim laporan ke petugas terkait."
    ),
    WelcomeRepository(
        image = R.drawable.welcome4,
        title = "Kumpulkan Poin dari Aksimu",
        description = "Setiap sampah yang berhasil Kamu laporkan dan diangkut oleh petugas akan memberimu poin! Tukarkan poin ini dengan reward menarik sebagai apresiasi atas kontribusi Kamu terhadap kebersihan lingkungan."
    )
)

data class WelcomeRepository(
    val image : Int,
    val title : String,
    val description : String
)