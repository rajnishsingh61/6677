package com.forgeos

import android.app.Application
import com.forgeos.data.AppDatabase
import com.forgeos.data.ForgeRepository

class ForgeApp : Application() {

    lateinit var repository: ForgeRepository
        private set

    override fun onCreate() {
        super.onCreate()
        // Initialize database early
        AppDatabase.getInstance(this)
        repository = ForgeRepository(this)
    }
}
