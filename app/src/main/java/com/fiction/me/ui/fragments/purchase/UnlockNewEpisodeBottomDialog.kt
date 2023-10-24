package com.fiction.me.ui.fragments.purchase

import android.os.Bundle
import com.fiction.me.R
import com.fiction.me.appbase.BottomSheetFragmentBaseMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.BottomDialogUnlockNewEpisodeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class UnlockNewEpisodeBottomDialog :
    BottomSheetFragmentBaseMVVM<UnlockNewEpisodeViewModel, BottomDialogUnlockNewEpisodeBinding>() {
    override val viewModel: UnlockNewEpisodeViewModel by viewModel()
    override val binding: BottomDialogUnlockNewEpisodeBinding by viewBinding()
    var chapterId: Long = 0L
    var setUnlockChapterListener: (Boolean) -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onView() {
        viewModel.getBalance()
        chapterId = arguments?.getLong(CHAPTER_KEY) ?: 0L
        binding.chapterCost.text = arguments?.getInt(CHAPTER_COST_VALUE).toString()
    }

    override fun onEach() {
        onEach(viewModel.userBalance) {
            with(binding) {
                balance.text = context?.resources?.getString(R.string.balance)?.format(it)
            }
            onEach(viewModel.isuUnlockChapter) {
                it?.let { setUnlockChapterListener(it) }
                dismiss()
            }
        }
    }

    override fun onViewClick() {
        with(binding) {
            closeBtn.setOnClickListener {
                dismiss()
            }
            unlock.setOnClickListener {
                if (chapterId != 0L) {
                    viewModel.buyChapter(chapterId)
                }
            }
        }
    }

    companion object {
        private const val CHAPTER_KEY = "CHAPTER_KEY"
        private const val CHAPTER_COST_VALUE = "CHAPTER_COST_VALUE"

        fun newInstance(chapterId: Long, chapterCostCoins: Int) =
            UnlockNewEpisodeBottomDialog().apply {
                arguments = Bundle().apply {
                    putLong(CHAPTER_KEY, chapterId)
                    putInt(CHAPTER_COST_VALUE, chapterCostCoins)
                }
            }
    }
}
