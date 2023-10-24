package com.name.jat.ui.fragments.profile.settings

import androidx.core.view.isVisible
import com.name.jat.R
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.appbase.viewmodel.BaseViewModel
import com.name.jat.databinding.FragmentDataStorageBinding
import com.name.jat.extensions.getCurrentCacheSize
import com.name.jat.extensions.startHeaderTextAnimation
import org.koin.androidx.viewmodel.ext.android.viewModel

class DataStorageFragment : FragmentBaseNCMVVM<BaseViewModel, FragmentDataStorageBinding>() {
    override val viewModel: BaseViewModel by viewModel()
    override val binding: FragmentDataStorageBinding by viewBinding()

    override fun onView() {
        with(binding) {
            val allCachesSize = context?.getCurrentCacheSize()
            val allCacheSIze = byteToMB(allCachesSize ?: 0)
            currentCacheSize.text = allCacheSIze
        }
    }


    override fun onViewClick() {
        with(binding) {
            mainToolbar.setOnBackBtnClickListener {
                popBackStack()
            }
            clearCache.setOnClickListener {
                if (context?.cacheDir?.deleteRecursively() == true &&
                    context?.filesDir?.deleteRecursively() == true &&
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