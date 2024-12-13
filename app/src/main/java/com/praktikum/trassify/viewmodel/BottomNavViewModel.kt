package com.praktikum.trassify.viewmodel

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel

class BottomNavViewModel : ViewModel() {
    // Menyimpan state dari item yang terpilih
    var selectedIndex = mutableIntStateOf(0)

    // Fungsi untuk mengubah indeks yang dipilih
    fun onItemSelected(index: Int) {
        selectedIndex.intValue = index
    }
}
