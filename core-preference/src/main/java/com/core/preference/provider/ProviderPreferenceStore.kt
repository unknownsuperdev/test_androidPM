package com.core.preference.provider

import android.content.Context
import com.core.preference.PreferenceStore

interface ProviderPreferenceStore {

    fun providePreferenceStore(context: Context, namePreference: String, needMigration: Boolean): PreferenceStore

}