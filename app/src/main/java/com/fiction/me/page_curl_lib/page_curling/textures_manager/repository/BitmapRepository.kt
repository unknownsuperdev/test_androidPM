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

package com.fiction.me.page_curl_lib.page_curling.textures_manager.repository

import android.os.Handler
import android.util.Log
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

/**
 * Repository for bitmaps
 * Loads bitmaps and stores them into cache
 * @property provider a provider of bitmaps
 * @param handler a handler for callbacks
 */
class BitmapRepository(
    private val provider: BitmapProvider,
    handler: Handler
) {
    private val messageSender = MessageSender(handler)

    private val cache = ConcurrentCache(BitmapLoader(provider))

    private val loadingExecutor = Executors.newSingleThreadExecutor()

    private var activeLoadingTask: Future<*>? = null

    val isInitialized: Boolean
        get() = !cache.isEmpty

    val pageCount: Int
        get() = provider.total

    fun resetCache() {
        cache.resetCache()
    }

    fun getBitmapFromCacheByIndex(index: Int) = cache[index]

    fun tryGetByIndex(index: Int) {
        val bitmap = cache[index]

        activeLoadingTask = if (bitmap != null) {
            messageSender.sendBitmapLoaded(bitmap)
            loadingExecutor.submit { updateCacheSilent(index) }
        } else {
            loadingExecutor.submit { updateCache(index) }
        }
    }

    /**
     * Updates cache without other actions
     */
    fun sync(index: Int) {
        activeLoadingTask = loadingExecutor.submit { updateCacheSilent(index) }
    }

    fun closeRepository() {
        // loadingExecutor.shutdown()
        activeLoadingTask?.takeIf { !it.isCancelled || !it.isDone }?.cancel(true)
        cache.clear()
    }

    fun reset() = cache.clear()

    fun init(index: Int) {
        activeLoadingTask = loadingExecutor.submit { initCache(index) }
    }

    /**
     * Updates cache by new data without loading indicator
     * @param index loaded bitmap's index
     * @param viewAreaSize bitmap's size
     */
    private fun updateCacheSilent(index: Int) {
        try {
            cache.update(index)
        } catch (ex: Exception) {
            ex.printStackTrace()
            messageSender.sendError(ex)
        }
    }

    /**
     * Updates cache by new data with loading indicator
     * @param index loaded bitmap's index
     * @param viewAreaSize bitmap's size
     */
    private fun updateCache(index: Int) {
        try {
            messageSender.sendLoadingStarted()
            cache.update(index)
        } catch (ex: Exception) {
            ex.printStackTrace()
            messageSender.sendError(ex)
        } finally {
            messageSender.sendLoadingCompleted()
            cache[index]?.let {
                messageSender.sendBitmapLoaded(it)
            }
        }
    }

    /**
     * Fills the cache by initial data
     * @param index start bitmap's index
     * @param viewAreaSize bitmap's size
     */
    private fun initCache(index: Int) {
        var success = true

        try {
            messageSender.sendLoadingStarted()
            cache.clear()
            cache.update(index)
        } catch (ex: Exception) {
            ex.printStackTrace()
            messageSender.sendError(ex)
            success = false
        } finally {
            messageSender.sendLoadingCompleted()

            if (success) {
                messageSender.sendRepositoryInitialized()
            }
        }
    }

    companion object{
        const val CACHE = "CacheLoadedImage"
    }
}
