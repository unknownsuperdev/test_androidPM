package com.fiction.me.ui.fragments.dataProvider

interface AdIdDataSource {
    fun getAdvertisingId(): Pair<String?, String>
}