package com.name.jat.ui.fragments.reader

import android.os.Bundle
import android.provider.Settings.System
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.name.domain.model.BookSettingsData
import com.name.domain.model.enums.ColorThemeData
import com.name.domain.model.enums.FlipTypeData
import com.name.domain.model.enums.LineHeightData
import com.name.domain.model.enums.TextTypeData
import com.name.jat.R
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentReaderBinding
import com.name.jat.extensions.dpToPx
import com.name.jat.extensions.startHeaderTextAnimation
import com.name.jat.ui.fragments.chapterlist.ChapterListBottomDialog
import com.name.jat.ui.fragments.reader.ToolsBottomSheetFragment.Companion.CHANGE_SELECTION_STATE
import com.name.jat.ui.fragments.reader.ToolsBottomSheetFragment.Companion.SEND_CHANGED_BOOK_INFO
import com.name.jat.ui.fragments.reader.adapters.FlipPagerAdapter
import com.name.jat.utils.Constants.Companion.BARS_FADE_DURATION_IN_SECONDS
import com.name.jat.utils.Constants.Companion.MAX_BRIGHTNESS
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReaderFragment : FragmentBaseNCMVVM<ReaderViewModel, FragmentReaderBinding>() {
    override val binding: FragmentReaderBinding by viewBinding()
    override val viewModel: ReaderViewModel by viewModel()
    private val defaultLineSpace = 9f
    private val biggerLineSpace = 13f
    private var isSaved = false



    override fun onView() {
        viewModel.getBookSettings()
        viewModel.ticker = BARS_FADE_DURATION_IN_SECONDS
        with(binding) {
            toolbar.run {
                setFirstItem(R.drawable.ic_bookmark_stroke)
                setSecondItem(R.drawable.ic_more)
                setBackIcon(R.drawable.ic_back)
            }
        }
        getFragmentResult()
    }

    override fun onViewClick() {
        with(binding) {
            toolbar.setOnSecondItemClickListener {
                BottomSheetFragmentMore().let {
                    it.show(childFragmentManager, BottomSheetFragmentMore::class.java.simpleName)
                    it.setReportClickListener {
                        navigateFragment(R.id.reportFragment)
                    }
                }
            }

            toolbar.setOnBackClickListener {
                popBackStack()
            }

            toolbar.setOnFirstItemClickListener {
                isSaved = !isSaved
                if (isSaved) {
                    context?.let {
                        toolbar.setFirstItem(R.drawable.ic_saved_library_bookmark)
                    }
                    doneSuccessfully(R.string.saved_library)
                } else {
                    context?.let {
                        toolbar.setFirstItem(R.drawable.ic_bookmark_stroke)
                    }
                    doneSuccessfully(R.string.novel_deleted_from_library)
                }
            }

            toolsBtn.setOnClickListener {
                ToolsBottomSheetFragment().show(
                    childFragmentManager,
                    ToolsBottomSheetFragment::class.java.simpleName
                )
            }

            chapterBtn.setOnClickListener {
                val btnDialog = ChapterListBottomDialog()
                val args = Bundle().apply {
                    putSerializable(
                        ARGUMENT_FOR_OPENING_CHAPTER_DIALOG,
                        OpenedFromWhere.FROM_READER
                    )
                }
                btnDialog.arguments = args
                btnDialog.show(childFragmentManager, ChapterListBottomDialog::class.java.simpleName)
            }

            modeBtn.setOnClickListener {
                when (viewModel.bookSettings.value?.colorTheme) {
                    ColorThemeData.SEPIA -> {
                        modeIcon.setImageResource(R.drawable.ic_dark_mode)
                        val updatedSettings =
                            viewModel.bookSettings.value?.copy(colorTheme = ColorThemeData.WHITE)
                        viewModel.changeBookSettings(updatedSettings)
                    }
                    ColorThemeData.BLACK -> {
                        modeIcon.setImageResource(R.drawable.ic_dark_mode)
                        val updatedSettings =
                            viewModel.bookSettings.value?.copy(colorTheme = ColorThemeData.WHITE)
                        viewModel.changeBookSettings(updatedSettings)
                    }
                    else -> {
                        modeIcon.setImageResource(R.drawable.ic_light_mode)
                        val updatedSettings =
                            viewModel.bookSettings.value?.copy(colorTheme = ColorThemeData.BLACK)
                        viewModel.changeBookSettings(updatedSettings)
                    }
                }
            }
        }
    }

    override fun onEach() {
        with(binding) {
            onEach(viewModel.timer) {
                if (appBar.isShown) {
                    appBar.isVisible = false
                    bottomBar.isVisible = false
                }
            }
            onEach(viewModel.bookSettings) {
                setBookSettings(it)
            }
        }
    }

    private fun setBookSettings(bookSettingsData: BookSettingsData?) {
        with(binding) {

            val lineSpace = when (bookSettingsData?.lineHeight) {
                LineHeightData.DEFAULT -> defaultLineSpace
                else -> biggerLineSpace
            }
            context?.let { context ->
                val font = when (bookSettingsData?.textType) {
                    TextTypeData.PT_SERIF -> ResourcesCompat.getFont(
                        context,
                        R.font.ptserif_regular
                    )
                    else -> ResourcesCompat.getFont(context, R.font.roboto_regular_400)
                }

                pages.doOnPreDraw {
                    val pageSplitter = font?.let { type ->
                        PageSplitter(
                            it.width - resources.getDimensionPixelSize(R.dimen.space_32),
                            it.height - resources.getDimensionPixelSize(R.dimen.space_16),
                            lineSpace.toInt(),
                            type
                        )
                    }

                    bookSettingsData?.textSize?.dpToPx(requireContext())?.let { size ->
                        pageSplitter?.append(
                            resources.getString(R.string.content),
                            size.toFloat()
                        )
                    }

                    when (bookSettingsData?.flipType) {
                        FlipTypeData.SHIFT -> pages.orientation =
                            ViewPager2.ORIENTATION_HORIZONTAL
                        FlipTypeData.SCROLL -> pages.orientation =
                            ViewPager2.ORIENTATION_VERTICAL
                        else -> pages.orientation = ViewPager2.ORIENTATION_VERTICAL // To Do
                    }

                    bookSettingsData?.let {
                        if (pageSplitter != null) {
                            val bookPages = pageSplitter.getPages()

                            pages.adapter =
                                FlipPagerAdapter(bookPages, bookSettingsData) {
                                    appBar.isVisible = !appBar.isVisible
                                    bottomBar.isVisible = !bottomBar.isVisible
                                    if (appBar.isShown)
                                        viewModel.ticker = BARS_FADE_DURATION_IN_SECONDS
                                }
                            pages.setCurrentItem(viewModel.getCurrentPagePosition(), false)
                        }
                    }
                }

                pages.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        viewModel.setCurrentPagePosition(position)
                        val countOfReadPage = position + 1
                        val countOfBookPages = pages.adapter?.itemCount ?: 1
                        val percentageOfReadPages =
                            viewModel.countPercentageOfReadPages(
                                countOfReadPage,
                                countOfBookPages
                            )
                        readPercent.text = percentageOfReadPages
                        super.onPageSelected(position)
                    }
                })
            }

            when (bookSettingsData?.colorTheme) {
                ColorThemeData.BLACK -> {
                    changeColorTheme(R.color.black_100, R.color.white)
                }
                ColorThemeData.SEPIA -> {
                    changeColorTheme(R.color.sepia_900, R.color.black_100)
                }
                else -> {
                    changeColorTheme(R.color.white, R.color.black_100)
                }
            }

            bookSettingsData?.let {
                if (it.brightness != 0) {
                    activity?.window?.let { window ->
                        val layoutsParams: WindowManager.LayoutParams = window.attributes
                        layoutsParams.screenBrightness =
                            it.brightness / MAX_BRIGHTNESS.toFloat()
                        window.attributes = layoutsParams
                    }
                }
            }
        }
    }

    private fun changeColorTheme(backgroundColor: Int, textColor: Int) {
        with(binding) {
            context?.let { context ->
                pages.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        backgroundColor
                    )
                )
                readPercent.setBackgroundColor(ContextCompat.getColor(context, backgroundColor))
                readPercent.setTextColor(ContextCompat.getColor(context, textColor))
            }
        }
    }

    private fun getFragmentResult() {
        activity?.supportFragmentManager?.setFragmentResultListener(
            OtherReportFragment.FROM_OTHER_REPORT_FRAGMENT,
            viewLifecycleOwner
        ) { _, result ->
            if (result.getBoolean(OtherReportFragment.DID_SEND_REPORT)) {
                doneSuccessfully(R.string.your_report_sent)
            }
        }
        activity?.supportFragmentManager?.setFragmentResultListener(
            ReportFragment.FROM_REPORT_FRAGMENT,
            viewLifecycleOwner
        ) { _, result ->
            if (result.getBoolean(ReportFragment.DID_SEND_REPORT)) {
                doneSuccessfully(R.string.your_report_sent)
            }
        }
        activity?.supportFragmentManager?.setFragmentResultListener(
            CHANGE_SELECTION_STATE,
            viewLifecycleOwner
        ) { _, result ->
            val data = result.getParcelable<BookSettingsData>(SEND_CHANGED_BOOK_INFO)
            viewModel.changeBookSettings(data)
        }
    }

    private fun doneSuccessfully(message: Int) {
        binding.successfullySaved.startHeaderTextAnimation(message)
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.window?.let { window ->
            val layoutsParams: WindowManager.LayoutParams = window.attributes
            context?.let {
                layoutsParams.screenBrightness =
                    System.getInt(it.contentResolver, System.SCREEN_BRIGHTNESS).toFloat()
                window.attributes = layoutsParams
            }
        }
    }
    companion object {
        const val ARGUMENT_FOR_OPENING_CHAPTER_DIALOG = "ARGUMENT_FOR_OPENING_CHAPTER_DIALOG"
    }
}