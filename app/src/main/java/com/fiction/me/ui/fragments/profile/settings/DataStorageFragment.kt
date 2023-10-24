package com.fiction.me.ui.fragments.profile.settings

import androidx.core.view.isVisible
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.appbase.viewmodel.BaseViewModel
import com.fiction.me.databinding.FragmentDataStorageBinding
import com.fiction.me.extensions.getCurrentCacheSize
import com.fiction.me.extensions.startHeaderTextAnimation
import com.fiction.me.utils.Events.Companion.CLEAR_CACHE_CLICKED
import com.fiction.me.utils.Events.Companion.DATA_STORAGE_SCREEN_SHOWN
import org.koin.androidx.viewmodel.ext.android.viewModel

class DataStorageFragment : FragmentBaseNCMVVM<BaseViewModel, FragmentDataStorageBinding>() {
    override val viewModel: DataStorageViewModel by viewModel()
    override val binding: FragmentDataStorageBinding by viewBinding()

    override fun onView() {
        viewModel.trackEvents(DATA_STORAGE_SCREEN_SHOWN)
        with(binding) {
            val allCachesSize = context?.getCurrentCacheSize()
            val allCacheSIze = byteToMB(allCachesSize ?: 0)
            currentCacheSize.text = allCacheSIze
            mainToolbar.setBackBtnIcon(R.drawable.ic_back)
        }
    }

    override fun onViewClick() {
        with(binding) {
            mainToolbar.setOnBackBtnClickListener {
                popBackStack()
            }
            clearCache.setOnClickListener {
                viewModel.trackEvents(CLEAR_CACHE_CLICKED)
                if (context?.cacheDir?.deleteRecursively() == true &&
                    //context?.filesDir?.deleteRecursively() == true &&
                    context?.codeCacheDir?.deleteRecursively() == true
                ) {
                    val allCachesSize = context?.getCurrentCacheSize()
                    currentCacheSize.text = byteToMB(allCachesSize ?: 0)
                    customSnackBar.startHeaderTextAnimation(resources.getString(R.string.your_cache_is_completely_cleared))
                }
            }
        }
    }

    private fun byteToMB(sizeInByte: Long): String {
        val result = sizeInByte.toDouble().div(1000).div(1000)
        changeClearButtonState(result.toInt())
        return if (result >= 1) {
            String.format(resources.getString(R.string.cache_size_in_mb, result))
        } else String.format(resources.getString(R.string.cache_size_in_empty_mb, 0))
    }

    private fun changeClearButtonState(cacheSize: Int) {
        binding.clearCache.isVisible = cacheSize != 0
    }
}