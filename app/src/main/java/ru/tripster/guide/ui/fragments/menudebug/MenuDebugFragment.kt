package ru.tripster.guide.ui.fragments.menudebug

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.widget.doOnTextChanged
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.tripster.di.BaseUrlUtil.baseUrl
import ru.tripster.di.BaseUrlUtil.currentBaseUrl
import ru.tripster.guide.App
import ru.tripster.guide.R
import ru.tripster.guide.analytics.ChangedApiKey
import ru.tripster.guide.appbase.FragmentBaseNCMVVM
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.databinding.MenuDebugBinding
import ru.tripster.guide.extensions.makeVisible
import ru.tripster.guide.extensions.makeVisibleGone
import ru.tripster.guide.ui.MainActivity

class MenuDebugFragment : FragmentBaseNCMVVM<MenuDebugViewModel, MenuDebugBinding>() {
    override val viewModel: MenuDebugViewModel by viewModel()
    override val binding: MenuDebugBinding by viewBinding()

    @SuppressLint("SetTextI18n")
    override fun onView() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        with(binding) {
            baseUrlDesc.makeVisible()
            baseUrlDesc.text = context?.getString(R.string.stage_url)
            stagingTab.isSelected = true
        }
    }

    override fun onEach() {
        onEach(viewModel.currentStage) {
            with(binding) {
                customTab.isSelected = false
                stagingTab.isSelected = false
                prodTab.isSelected = false

                when (it) {
                    context?.getString(R.string.stage_url), "" -> {
                        selectedTabLogic(stagingTab, 0)
                    }

                    context?.getString(R.string.production_url) -> {
                        selectedTabLogic(prodTab, 1)
                    }

                    else -> {
                        selectedTabLogic(customTab, 2, it ?: "")
                    }
                }
            }
        }
    }

    private fun selectedTabLogic(
        selectedTab: AppCompatTextView,
        tabNumber: Int,
        customBaseUrl: String = ""
    ) {
        selectedTab.setTypeface(selectedTab.typeface, Typeface.BOLD)

        with(binding) {
            tabLayout[0].isSelected = false
            tabLayout[1].isSelected = false
            tabLayout[2].isSelected = false
            tabLayout[tabNumber].isSelected = true

            when (tabNumber) {
                0 -> {
                    setBackground(
                        R.drawable.shape_white_rectangle_8,
                        R.drawable.shape_gray90_rectangle_8,
                        R.drawable.shape_gray90_rectangle_8
                    )
                    binding.prodTab.typeface = Typeface.DEFAULT
                    binding.customTab.typeface = Typeface.DEFAULT
                    binding.baseUrlDesc.makeVisible()
                    inputBaseUrl.makeVisibleGone()
                    baseUrlDesc.text = context?.getString(R.string.stage_url)
                }

                1 -> {
                    setBackground(
                        R.drawable.shape_gray90_rectangle_8,
                        R.drawable.shape_white_rectangle_8,
                        R.drawable.shape_gray90_rectangle_8
                    )
                    binding.stagingTab.typeface = Typeface.DEFAULT
                    binding.customTab.typeface = Typeface.DEFAULT
                    baseUrlDesc.makeVisible()
                    inputBaseUrl.makeVisibleGone()
                    baseUrlDesc.text = context?.getString(R.string.production_url)
                }

                2 -> {
                    setBackground(
                        R.drawable.shape_gray90_rectangle_8,
                        R.drawable.shape_gray90_rectangle_8,
                        R.drawable.shape_white_rectangle_8
                    )
                    binding.stagingTab.typeface = Typeface.DEFAULT
                    binding.prodTab.typeface = Typeface.DEFAULT
                    baseUrlDesc.makeVisibleGone()
                    inputBaseUrl.makeVisible()
                    inputBaseUrl.setText(customBaseUrl)
                }
            }
        }
    }

    override fun onViewClick() {
        with(binding) {
            prodEnabled.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    App.refreshScope()
                    ChangedApiKey.isProdAnalytics = true
                    context?.let { context -> viewModel.changeApiKey(context) }
                } else {
                    App.refreshScope()
                    ChangedApiKey.isProdAnalytics = false
                    context?.let { context -> viewModel.changeApiKey(context) }
                }
            }

            stagingTab.setOnClickListener {
                selectedTabLogic(stagingTab, 0)
            }

            prodTab.setOnClickListener {
                selectedTabLogic(prodTab, 1)
            }

            customTab.setOnClickListener {
                selectedTabLogic(customTab, 2)
            }
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) viewModel.isSavedUser = true
            }
            crashBtn.setOnClickListener {
                divide(10, 0)
            }
            anrBtn.setOnClickListener {
                simulateLongRunningTask()
            }
            nonFatalBtn.setOnClickListener {
                try {
                    divide(10, 0)
                } catch (e: ArithmeticException) {
                    // Handle the non-fatal exception
                    println("Error: Division by zero is not allowed.")
                }

            }
            okBtn.setOnClickListener {
                if (customTab.isSelected) {
                    baseUrl = if (!inputBaseUrl.text.toString().startsWith("https://")) "https://" + inputBaseUrl.text.toString() + "/api/" else inputBaseUrl.text.toString() + "/api/"
                    currentBaseUrl = baseUrl as String
                    viewModel.saveCurrentStage(inputBaseUrl.text.toString())
                } else {
                    baseUrl = "https://" + baseUrlDesc.text.toString() + "/api/"
                    currentBaseUrl = baseUrl as String
                    viewModel.saveCurrentStage(baseUrlDesc.text.toString())
                }

                navigateFragment(
                    MenuDebugFragmentDirections.actionMenuDebugFragmentToLoginFragment(
                        arguments?.getInt(
                            MainActivity.DEEP_LINK_ORDER_ID
                        ) ?: 0
                        , "",
                        viewModel.isSavedUser
                    )
                )
            }
            closeBtn.setOnClickListener {
                popBackStack()
            }
        }
    }

    private fun setBackground(stagingDrawable: Int, prodDrawable: Int, customDrawable: Int) {
        with(binding) {
            prodEnabled.isChecked = ChangedApiKey.isProdAnalytics == true
            stagingTab.background = context?.let { ContextCompat.getDrawable(it, stagingDrawable) }
            prodTab.background =
                context?.let { ContextCompat.getDrawable(it, prodDrawable) }
            customTab.background =
                context?.let { ContextCompat.getDrawable(it, customDrawable) }
        }
    }
    private fun simulateLongRunningTask() {
        var i = 0
        while (true) {
            i++
        }
    }

    private fun divide(a: Int, b: Int) {
        a / b
    }
}


