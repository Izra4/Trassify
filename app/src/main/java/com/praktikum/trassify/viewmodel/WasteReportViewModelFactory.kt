package com.praktikum.trassify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.praktikum.trassify.App
import com.praktikum.trassify.data.repository.WasteReportRepository
import io.github.jan.supabase.SupabaseClient

class WasteReportViewModelFactory(
    private val repository: WasteReportRepository,
    private val supabaseClient: SupabaseClient = App.supabaseClient
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WasteReportViewModel::class.java)) {
            return WasteReportViewModel(repository, supabaseClient) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
