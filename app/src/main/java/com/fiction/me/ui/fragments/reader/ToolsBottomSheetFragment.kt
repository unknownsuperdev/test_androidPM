package com.fiction.me.ui.fragments.reader

import android.os.Bundle
import android.provider.Settings.System
import android.view.WindowManager.LayoutParams
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.fiction.domain.model.BookSettingsData
import com.fiction.domain.model.enums.ColorThemeData
import com.fiction.domain.model.enums.FlipTypeData
import com.fiction.domain.model.enums.LineHeightData
import com.fiction.me.R
import com.fiction.me.appbase.BottomSheetFragmentBaseMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.BottomsheetFragmentToolsBinding
import com.fiction.me.ui.fragments.reader.adapters.TextFontsAdapter
import com.fiction.me.utils.Constants.Companion.MAX_BRIGHTNESS
import com.fiction.me.utils.Constants.Companion.MIN_BRIGHTNESS
import com.fiction.me.utils.Constants.Companion.PROGRESS_INCREMENT_COUNT
import org.koin.androidx.viewmodel.ext.android.viewModel


class ToolsBottomSheetFragment :
    BottomSheetFragmentBaseMVVM<ToolsViewModel, BottomsheetFragmentToolsBinding>() {

    override val viewModel: ToolsViewModel by viewModel()
    override val binding: BottomsheetFragmentToolsBinding by viewBinding()

    private var textFontsAdapter = TextFontsAdapter { itemId, textType ->
        with(viewModel) {
            changeFontType(itemId)
            val updatedSettings = getSettingsData()?.copy(textType = textType)
            changeBookSettings(updatedSettings)
            updatedSettings?.let { setSelectionStateListener(it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogThemeBlack400)
    }

    override fun onView() {

        viewModel.getBookSettings()
        var brightness = 0

        with(binding) {

            textFontRV.apply {
                context?.let {
                    layoutManager = LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)
                    adapter = textFontsAdapter
                }
            }
            context?.let {
                val cResolver = it.contentResolver
                brightnessSeekBar.max = MAX_BRIGHTNESS
                brightnessSeekBar.keyProgressIncrement = PROGRESS_INCREMENT_COUNT
                brightness = System.getInt(cResolver, System.SCREEN_BRIGHTNESS)
                brightnessSeekBar.progress = brightness

                brightnessSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                        activity?.let { activity ->
                            with(activity) {
                                val layoutsParams: LayoutParams = window.attributes
                                layoutsParams.screenBrightness =
                                    brightness / MAX_BRIGHTNESS.toFloat()
                                window.attributes = layoutsParams
                                viewModel.getSettingsData()?.let { bookSetting ->
                                    val updatedSettings = bookSetting.copy(brightness = brightness)
                                    viewModel.changeBookSettings(updatedSettings)
                                    setSelectionStateListener(updatedSettings)
                                }
                            }
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar) {}

                    override fun onProgressChanged(
                        seekBar: SeekBar,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        brightness = if (progress <= MIN_BRIGHTNESS) MIN_BRIGHTNESS else progress
                    }
                })
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.bookSettings) {
            viewModel.getFontTypes()
            it?.let { it1 -> setSettingsViewChanges(it1) }
        }
        onEach(viewModel.fontsTypes) {
            textFontsAdapter.submitList(it)
        }
    }

    override fun onViewClick() {
        with(binding) {

            closeBtn.setOnClickListener {
                dismiss()
            }

            whiteTheme.setOnClickListener {
                setColorThemeSelection(isSelectWhiteTheme = true)
                val updatedSettings =
                    viewModel.getSettingsData()?.copy(colorTheme = ColorThemeData.WHITE)
                updatedSettings?.let {
                    setSelectionStateListener(updatedSettings)
                    viewModel.changeBookSettings(updatedSettings)
                    setSettingsViewChanges(updatedSettings)
                }
            }

            blackTheme.setOnClickListener {
                setColorThemeSelection(isSelectBlackTheme = true)
                val updatedSettings =
                    viewModel.getSettingsData()?.copy(colorTheme = ColorThemeData.BLACK)
                updatedSettings?.let {
                    setSelectionStateListener(updatedSettings)
                    viewModel.changeBookSettings(updatedSettings)
                    setSettingsViewChanges(updatedSettings)
                }
            }

            sepiaTheme.setOnClickListener {
                setColorThemeSelection(isSelectSepiaTheme = true)
                val updatedSettings =
                    viewModel.getSettingsData()?.copy(colorTheme = ColorThemeData.SEPIA)
                updatedSettings?.let {
                    setSelectionStateListener(updatedSettings)
                    viewModel.changeBookSettings(updatedSettings)
                    setSettingsViewChanges(updatedSettings)
                }
            }

            defaultLineHeight.setOnClickListener {
                setLineHeightSelectionState(isDefault = true)
                val updatedSettings =
                    viewModel.getSettingsData()?.copy(lineHeight = LineHeightData.DEFAULT)
                updatedSettings?.let {
                    setSelectionStateListener(updatedSettings)
                    viewModel.changeBookSettings(updatedSettings)
                }
            }

            biggerLineHeight.setOnClickListener {
                setLineHeightSelectionState(isBigger = true)
                val updatedSettings =
                    viewModel.getSettingsData()?.copy(lineHeight = LineHeightData.BIGGER)
                updatedSettings?.let {
                    setSelectionStateListener(updatedSettings)
                    viewModel.changeBookSettings(updatedSettings)
                }
            }

            flipScroll.setOnClickListener {
                setFlipTypeSelectionState(isVerticalScroll = true)
                val updatedSettings =
                    viewModel.getSettingsData()?.copy(flipType = FlipTypeData.SCROLL)
                updatedSettings?.let {
                    setSelectionStateListener(updatedSettings)
                    viewModel.changeBookSettings(updatedSettings)
                }
            }

            flipTurnPage.setOnClickListener {
                setFlipTypeSelectionState(isTurningPage = true)
                val updatedSettings =
                    viewModel.getSettingsData()?.copy(flipType = FlipTypeData.TURN_PAGE)
                updatedSettings?.let {
                    setSelectionStateListener(updatedSettings)
                    viewModel.changeBookSettings(updatedSettings)
                }
            }

            flipShift.setOnClickListener {
                setFlipTypeSelectionState(isShiftingHorizontal = true)
                val updatedSettings =
                    viewModel.getSettingsData()?.copy(flipType = FlipTypeData.SHIFT)
                updatedSettings?.let {
                    setSelectionStateListener(updatedSettings)
                    viewModel.changeBookSettings(updatedSettings)
                }
            }

            plus.setOnClickListener {
                val sizeOfText = textSize.text.toString()
                val size = viewModel.changeTextSize(sizeOfText.toInt(), true)
                if (size != -1) {
                    textSize.text = size.toString()
                    val updatedSettings = viewModel.getSettingsData()?.copy(textSize = size)
                    updatedSettings?.let {
                        setSelectionStateListener(updatedSettings)
                        viewModel.changeBookSettings(updatedSettings)
                    }
                }
            }

            minus.setOnClickListener {
                val sizeOfText = textSize.text.toString()
                val size = viewModel.changeTextSize(sizeOfText.toInt(), false)
                if (size != -1) {
                    textSize.text = size.toString()
                    val updatedSettings = viewModel.getSettingsData()?.copy(textSize = size)
                    updatedSettings?.let {
                        setSelectionStateListener(updatedSettings)
                        viewModel.changeBookSettings(updatedSettings)
                    }
                }
            }
        }
    }

    fun setSelectionStateListener(bookSettingsData: BookSettingsData) {
        activity?.supportFragmentManager?.setFragmentResult(
            CHANGE_SELECTION_STATE,
            bundleOf(SEND_CHANGED_BOOK_INFO to bookSettingsData)
        )
    }

    private fun setSettingsViewChanges(bookSettingsData: BookSettingsData) {
        when (bookSettingsData.colorTheme) {
            ColorThemeData.BLACK -> setColorThemeSelection(isSelectBlackTheme = true)
            ColorThemeData.SEPIA -> setColorThemeSelection(isSelectSepiaTheme = true)
            else -> setColorThemeSelection(isSelectWhiteTheme = true)
        }
        when (bookSettingsData.flipType) {
            FlipTypeData.SCROLL -> setFlipTypeSelectionState(isVerticalScroll = true)
            FlipTypeData.TURN_PAGE -> setFlipTypeSelectionState(isTurningPage = true)
            else -> setFlipTypeSelectionState(isShiftingHorizontal = true)
        }
        when (bookSettingsData.lineHeight) {
            LineHeightData.DEFAULT -> setLineHeightSelectionState(isDefault = true)
            else -> setLineHeightSelectionState(isBigger = true)
        }
        binding.textSize.text = bookSettingsData.textSize.toString()

        if (bookSettingsData.brightness != 0)
            binding.brightnessSeekBar.progress = bookSettingsData.brightness

    }

    private fun setColorThemeSelection(
        isSelectWhiteTheme: Boolean = false,
        isSelectBlackTheme: Boolean = false,
        isSelectSepiaTheme: Boolean = false
    ) {
        with(binding) {
            whiteTheme.isSelected = isSelectWhiteTheme
            blackTheme.isSelected = isSelectBlackTheme
            sepiaTheme.isSelected = isSelectSepiaTheme
        }
    }

    private fun setLineHeightSelectionState(
        isDefault: Boolean = false,
        isBigger: Boolean = false
    ) {
        with(binding) {
            defaultLineHeight.isSelected = isDefault
            biggerLineHeight.isSelected = isBigger
        }
    }

    private fun setFlipTypeSelectionState(
        isVerticalScroll: Boolean = false,
        isShiftingHorizontal: Boolean = false,
        isTurningPage: Boolean = false
    ) {
        with(binding) {
            flipScroll.isSelected = isVerticalScroll
            flipShift.isSelected = isShiftingHorizontal
            flipTurnPage.isSelected = isTurningPage
        }
    }

    companion object {
        const val CHANGE_SELECTION_STATE = "CHANGE_SELECTION_STATE"
        const val SEND_CHANGED_BOOK_INFO = "SEND_CHANGED_BOOK_INFO"
    }
}