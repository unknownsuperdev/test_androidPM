package com.core.preference.provider

import android.content.Context
import com.core.preference.PreferenceStore
import com.core.preference.datastore.DataStoreImpl

class ProviderPreferenceStoreImpl : ProviderPreferenceStore {

    override fun providePreferenceStore(
        context: Context,
        namePreference: String,
        needMigration: Boolean
    ): PreferenceStore {
        return DataStoreImpl(context, namePreference, needMigration)
    }

}