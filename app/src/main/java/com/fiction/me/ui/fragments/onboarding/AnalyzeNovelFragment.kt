package com.fiction.me.ui.fragments.onboarding

import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.navigation.NavDirections
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.appbase.viewmodel.BaseViewModel
import com.fiction.me.databinding.FragmentAnalyzeNovelBinding
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AnalyzeNovelFragment : FragmentBaseNCMVVM<BaseViewModel, FragmentAnalyzeNovelBinding>() {
    override val binding: FragmentAnalyzeNovelBinding by viewBinding()
    override val viewModel: AnalyzeNovelViewModel by viewModel()
    val directions: NavDirections? = null

    override fun onView() {
        with(binding) {
            CoroutineScope(Dispatchers.IO).launch {
                for (i in 0..100) {
                    delay(50)
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
                                if (viewModel.isCacheData) navigateFragment(directions)
                            }
                        }
                    }
                }
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(this) {
            //activity?.finish()
        }
    }

    override fun onEach() {
        onEach(viewModel.bookForYou) { books ->
            viewModel.isCacheData = true
            if (directions != null) navigateFragment(directions)
        }
    }
}