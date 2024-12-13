package com.praktikum.trassify

import android.app.Application
import com.praktikum.trassify.utils.createSupabaseClientInstance
import io.github.jan.supabase.SupabaseClient

class App : Application() {
    companion object {
        lateinit var supabaseClient: SupabaseClient
    }

    override fun onCreate() {
        super.onCreate()

        // Inisialisasi Supabase client
        val supabaseUrl = getString(R.string.supabase_url)
        val supabaseKey = getString(R.string.supabase_key)
        supabaseClient = createSupabaseClientInstance(supabaseUrl, supabaseKey)
    }
}
