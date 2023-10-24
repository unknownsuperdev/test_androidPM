package com.fiction.me.ui.fragments.onboarding

import android.gesture.GestureOverlayView.ORIENTATION_HORIZONTAL
import androidx.activity.addCallback
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.customviews.BottomFadeEdgeScrollView
import com.fiction.me.databinding.FragmentSelectionBooksBinding
import com.fiction.me.extensions.toFormatViewsLikes
import com.fiction.me.utils.Events.Companion.BOOKS_CAROUSEL_SWIPED
import com.fiction.me.utils.Events.Companion.BOOK_ID_PROPERTY
import com.fiction.me.utils.Events.Companion.NOVELS_FOR_YOU_SCREEN
import com.fiction.me.utils.Events.Companion.NOVELS_FOR_YOU_SCREEN_CLOSED
import com.fiction.me.utils.Events.Companion.NOVELS_FOR_YOU_SCREEN_CONTINUE_BUTTON_CLICKED
import com.fiction.me.utils.Events.Companion.NOVELS_FOR_YOU_SCREEN_SHOWN
import com.fiction.me.utils.Events.Companion.NOVELS_FOR_YOU_SWIPE_SCREEN_SHOWN
import com.fiction.me.utils.InternalDeepLink
import com.fiction.me.utils.cacheImages
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Math.abs

class SelectionBooksFragment :
    FragmentBaseNCMVVM<SelectionBooksViewModel, FragmentSelectionBooksBinding>() {
    override val binding: FragmentSelectionBooksBinding by viewBinding()
    override val viewModel: SelectionBooksViewModel by viewModel()
    private var isScrollDown = false
    private val maxFadingEdgeLength = 400
    private val minFadingEdgeLength = 100
    private val increaseSizeFadingEdgeLength = 5
    private val minimizeSizeFadingEdgeLength = 30
    private var currentSliderPosition = 1

    override fun onView() {
        viewModel.getBooksForYou(currentSliderPosition)
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
        activity?.onBackPressedDispatcher?.addCallback(this) {
            // activity?.finish()
        }
    }

    override fun onViewClick() {
        with(binding) {
            toolbar.setNavigationOnClickListener {
                sendEvent()
                val directions =
                    SelectionBooksFragmentDirections.actionSelectionBooksFragmentToExploreFragment()
                navigateFragment(directions)
            }
            continueReading.setOnClickListener {
                val bookId = viewModel.bookForYou.value?.id ?: -1
                viewModel.trackEvents(
                    NOVELS_FOR_YOU_SCREEN_CONTINUE_BUTTON_CLICKED,
                    hashMapOf(BOOK_ID_PROPERTY to bookId)
                )
                viewModel.getBooksForReading(currentSliderPosition)?.let {
                    val deeplink =
                        InternalDeepLink.makeCustomDeepLinkForReader(bookId = it.first, chapterId = it.second, isFromOnBoarding = true, referrer = NOVELS_FOR_YOU_SCREEN)

                            .toUri()
                    navigateFragment(deeplink)
                }
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.images) { images ->
            images?.let { imgs ->
                imgs.forEach { context?.let { it1 -> cacheImages(it1, it) } }
                createViewPager(imgs)
            }
        }

        onEach(viewModel.bookForYou) {
            it?.let { book ->
                with(binding) {
                    title.text = book.title
                    genre.text = book.genres
                    views.text = book.views.toFormatViewsLikes()
                    likes.text = book.likes.toFormatViewsLikes()
                    chapterTitle.text = book.firstChapter.title
                    val textFromHtml = HtmlCompat.fromHtml(
                        book.firstChapter.text,
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                    content.text = textFromHtml
                }

                viewModel.run {
                    val bookId = viewModel.bookForYou.value?.id ?: -1
                    if (isFirstLaunch)
                        trackEvents(
                            NOVELS_FOR_YOU_SCREEN_SHOWN,
                            hashMapOf(BOOK_ID_PROPERTY to bookId)
                        )
                    else viewModel.trackEvents(
                        NOVELS_FOR_YOU_SWIPE_SCREEN_SHOWN,
                        hashMapOf(BOOK_ID_PROPERTY to bookId)
                    )
                }
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
                setCurrentItem(currentSliderPosition, false)
            }
            imageSliderViewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int,
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    if (currentSliderPosition != position) {
                        viewModel.trackEvents(BOOKS_CAROUSEL_SWIPED)
                        viewModel.slideBooks(position)
                        currentSliderPosition = position
                    }
                }
            })
            val offsetPx = resources.getDimensionPixelOffset(R.dimen.space_30)
            val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.space_60)
            val screenHeight = 0.52f
            val sizeOfIncrease = 0.48f

            val compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.run {
                addTransformer { page, position ->
                    val r = 1 - abs(position)
                    page.scaleY = screenHeight + r * sizeOfIncrease
                    page.scaleX = screenHeight + r * sizeOfIncrease
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

    private fun sendEvent() {
        val bookId = viewModel.bookForYou.value?.id ?: -1
        viewModel.trackEvents(
            NOVELS_FOR_YOU_SCREEN_CLOSED,
            hashMapOf(BOOK_ID_PROPERTY to bookId)
        )
    }
}
