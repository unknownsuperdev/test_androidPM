package com.fiction.me.ui.fragments.reader.provider

import android.graphics.Bitmap
import android.util.Log
import com.fiction.me.page_curl_lib.page_curling.textures_manager.repository.BitmapProvider
import com.fiction.me.page_curl_lib.page_curling.textures_manager.repository.BitmapRepository.Companion.CACHE

/**
 * Provides bitmaps from Raw resources of the app
 */
class RawResourcesBitmapProvider(bitmaps: List<Bitmap>) : BitmapProvider {
    private var allBitmaps: List<Bitmap> = bitmaps

    /**
     * Total quantity of bitmap
     */
    override val total: Int
        get() = allBitmaps.size

    fun updateBitmaps(updatedBitmaps: List<Bitmap>) {
        for (i in 0..allBitmaps.size-1)
            Log.i("BitmapProvider", "updateBitmaps:index = $i ${allBitmaps[i]}")
        allBitmaps = updatedBitmaps
    }

    /**
     * Returns a stream of bitmap data for given bitmap index
     * The stream will be closed by caller
     */
    override fun getBitmaps(index: Int): Bitmap  {
        Log.i("BitmapProvider", "getBitmaps:index = $index ${allBitmaps[index]}")
        return  allBitmaps[index]
    }
}