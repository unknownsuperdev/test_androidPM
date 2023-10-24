/*
 * Copyright (c) 2021 Alexander Shevelev
 *
 * Licensed under the MIT License;
 * ---------------------------------------
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.fiction.me.page_curl_lib.page_curling.textures_manager

import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.util.LruCache
import android.util.Size
import com.fiction.me.page_curl_lib.page_curling.CurlPage
import com.fiction.me.page_curl_lib.page_curling.CurlViewInvalidator
import com.fiction.me.page_curl_lib.page_curling.PageSide
import com.fiction.me.page_curl_lib.page_curling.textures_manager.repository.BitmapProvider
import com.fiction.me.page_curl_lib.page_curling.textures_manager.repository.BitmapRepository
import com.fiction.me.page_curl_lib.page_curling.textures_manager.repository.BitmapRepositoryCallbackCodes
import com.fiction.me.page_curl_lib.page_curling.toSmart

/**
 * Provide textures for pages and update pages
 */
class PageTexturesManager(
    provider: BitmapProvider,
    private val viewInvalidator: CurlViewInvalidator
) {
    val pageCount: Int
        get() = repository.pageCount

    private val cache = object: LruCache<Int, Bitmap>(4) {
        override fun entryRemoved(evicted: Boolean, key: Int?, oldValue: Bitmap?, newValue: Bitmap?) {
            super.entryRemoved(evicted, key, oldValue, newValue)
            //oldValue?.recycle()
        }
    }

    private val repository = BitmapRepository(provider, Handler(Looper.getMainLooper(), this::processRepositoryMessage))

    private var updatingState: PageTexturesManagerState? = null

    private var loadingEventsHandler: PageLoadingEventsHandler? = null

    private var renderingTimes: Int = 1

    fun resetCache(){
        repository.resetCache()
    }
    /**
     * Load initial bitmaps into the repository
     * @param pageTextureSize page texture size
     * @param index start page index
     * @param reset the cache must be clear
     * @param updateTwoPagesNeeded If the value is "true" two pages are going to be updated (left and right)
     * @param completed callback, which is called when the repository is initialized
     */
    fun init(pageTextureSize: Size, index: Int, reset: Boolean, updateTwoPagesNeeded: Boolean, completed: () -> Unit) {
        if(reset) {
            reset(updateTwoPagesNeeded)
        }

        if(repository.isInitialized) {
            completed()
        } else {
            updatingState = PageTexturesManagerState(textureSize = pageTextureSize, index = index, repositoryInitialized = completed)
            repository.init(index)
        }
    }

    /**
     * Set bitmap for page - front and back (may be texture or solid color)
     * @param page updated page
     * @param size page texture size
     * @param index page index
     */
    fun updatePage(page: CurlPage, size: Size, index: Int) {
        try {
            val texture = repository.getBitmapFromCacheByIndex(index)//cache.get(size.width xor index)

            if(texture != null) {
                updatePage(page, texture)
                repository.sync(index)
            } else {
                updatingState = PageTexturesManagerState(page, size, index)
                repository.tryGetByIndex(index)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setOnLoadingListener(handler: PageLoadingEventsHandler?) {
        loadingEventsHandler = handler
    }

    fun closeManager() = repository.closeRepository()

    private fun reset(updateTwoPagesNeeded: Boolean) {
        repository.reset()
        renderingTimes = if(updateTwoPagesNeeded) 2 else 1
    }

    private fun updatePage(page: CurlPage, texture: Bitmap) {
        texture.toSmart(false).let {
            page.setTexture(it, PageSide.Front)
            page.setTexture(it, PageSide.Back)
            page.setColor(Color.argb(50, 255, 255, 255), PageSide.Back)

            // We need to re-render view manually
            if(renderingTimes > 0) {
                renderingTimes--
                viewInvalidator.renderNow()
            }
        }
    }

    private fun processRepositoryMessage(message: Message): Boolean {
        when(message.what) {
            BitmapRepositoryCallbackCodes.BITMAP -> {
                val sourceBitmap = message.obj as Bitmap

                updatingState?.let { state ->
                    cache.put(state.textureSize.width xor state.index, sourceBitmap)
                    updatePage(state.page!!, sourceBitmap)
                }
            }

            BitmapRepositoryCallbackCodes.LOADING_STARTED -> loadingEventsHandler?.onLoadingStarted()

            BitmapRepositoryCallbackCodes.LOADING_COMPLETED -> loadingEventsHandler?.onLoadingCompleted()

            BitmapRepositoryCallbackCodes.ERROR -> loadingEventsHandler?.onLoadingError()

            BitmapRepositoryCallbackCodes.REPOSITORY_INITIALIZED -> {
                updatingState?.repositoryInitialized?.invoke()
            }
        }

        return true
    }
}