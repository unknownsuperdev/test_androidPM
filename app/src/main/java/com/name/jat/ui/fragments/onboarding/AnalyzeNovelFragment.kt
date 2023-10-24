package com.name.jat.ui.fragments.onboarding

import androidx.core.view.isVisible
import com.name.jat.R
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.appbase.viewmodel.BaseViewModel
import com.name.jat.databinding.FragmentAnalyzeNovelBinding
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AnalyzeNovelFragment : FragmentBaseNCMVVM<BaseViewModel, FragmentAnalyzeNovelBinding>() {
    override val binding: FragmentAnalyzeNovelBinding by viewBinding()
    override val viewModel: BaseViewModel by viewModel()

    override fun onView() {
        with(binding) {
            CoroutineScope(Dispatchers.IO).launch {
                for (i in 0..100) {
                    delay(40)
                    withContext(Dispatchers.Main) {
                        circleProgressBar.progress = i
                        binding.analyzePercentage.text =
                            resources.getString(R.string.novels_analyzed, i)
                        when (i) {
                            25 -> {
                                genreImage.setImageResource(R.drawable.ic_werewolf)
                                genre.text = resources.getString(R.string.werewolf)
                            }
                            48 -> {
                                genreImage.setImageResource(R.drawable.ic_fantasy)
                                genre.text = resources.getString(R.string.fantasy)
                            }
                            100 -> {
                                genreImage.setImageResource(R.drawable.ic_big_done)
                                genre.text = resources.getString(R.string.done)
                                doneBg.isVisible = true
                                val directions =
                                    AnalyzeNovelFragmentDirections.actionAnalyzeNovelFragmentToSelectionBooksFragment()
                                navigateFragment(directions)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onEach() {

    }
}