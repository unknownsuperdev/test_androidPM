package com.name.jat.ui.fragments.onboarding

import android.gesture.GestureOverlayView.ORIENTATION_HORIZONTAL
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import com.name.jat.R
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.customviews.BottomFadeEdgeScrollView
import com.name.jat.databinding.FragmentSelectionBooksBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

class SelectionBooksFragment :
    FragmentBaseNCMVVM<SelectionBooksViewModel, FragmentSelectionBooksBinding>() {
    override val binding: FragmentSelectionBooksBinding by viewBinding()
    override val viewModel: SelectionBooksViewModel by viewModel()
    private var isScrollDown = false
    private val maxFadingEdgeLength = 400
    private val minFadingEdgeLength = 100
    private val increaseSizeFadingEdgeLength = 5
    private val minimizeSizeFadingEdgeLength = 30


    override fun onView() {
        viewModel.getSliderImages()
        var fadingEdgeLength = 0
        with(binding) {

            scrollView.setScrollChangeListener(object :
                BottomFadeEdgeScrollView.OnScrollChangeListener {
                override fun onScrollUp() {
                    isScrollDown = false
                }

                override fun onScrollDown() {
                    isScrollDown = true
                }
            })
            scrollView.viewTreeObserver
                .addOnScrollChangedListener {
                    if (scrollView.getChildAt(0).bottom
                        <= scrollView.height + scrollView.scrollY
                    ) {
                        scrollView.setFadingEdgeLength(maxFadingEdgeLength)
                        fadingEdgeLength = minFadingEdgeLength
                    } else {
                        if (isScrollDown)
                            fadingEdgeLength += increaseSizeFadingEdgeLength
                        else fadingEdgeLength -= minimizeSizeFadingEdgeLength
                        scrollView.setFadingEdgeLength(fadingEdgeLength)
                    }
                }
        }
    }

    override fun onViewClick() {
        with(binding) {
            toolbar.setNavigationOnClickListener {
                viewModel.setOpenedWelcomeScreens()
                val directions =
                    SelectionBooksFragmentDirections.actionSelectionBooksFragmentToExploreFragment()
                navigateFragment(directions)
            }
            continueReading.setOnClickListener {
                viewModel.setOpenedWelcomeScreens()
                val directions =
                    SelectionBooksFragmentDirections.actionSelectionBooksFragmentToReaderFragment()
                navigateFragment(directions)
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.images) { images ->
            images?.let {
                createViewPager(it)
            }
        }
    }

    private fun createViewPager(images: List<String>) {
        with(binding) {
            imageSliderViewPager.adapter = ImageSliderAdapter(images)
            imageSliderViewPager.run {
                clipToPadding = false
                clipChildren = false
                offscreenPageLimit = 3
                getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                setCurrentItem(1, false)
            }
            val offsetPx = resources.getDimensionPixelOffset(R.dimen.space_30)
            val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.space_60)
            val screenHeight = 0.85f
            val sizeOfIncrease = 0.35f

            val compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.run {
                addTransformer { page, position ->
                    val r = 1 - abs(position)
                    page.scaleY = screenHeight + r * sizeOfIncrease
                    val offset = position * -(2 * offsetPx + pageMarginPx)
                    if (imageSliderViewPager.orientation == ORIENTATION_HORIZONTAL) {
                        if (ViewCompat.getLayoutDirection(imageSliderViewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                            page.translationX = -offset
                        } else {
                            page.translationX = offset
                        }
                    } else {
                        page.translationY = offset
                    }
                }
            }
            imageSliderViewPager.setPageTransformer(compositePageTransformer)
        }
    }
}