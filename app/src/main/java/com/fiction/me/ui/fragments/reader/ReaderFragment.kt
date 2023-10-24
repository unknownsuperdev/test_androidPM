package com.fiction.me.ui.fragments.reader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.provider.Settings.System
import android.util.Log
import android.util.Size
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.analytics.api.Events.USER_PROPERTY__CHAPTERS_AUTO_UNLOCK
import com.fiction.core.utils.Uuid
import com.fiction.domain.model.BookChapterText
import com.fiction.domain.model.BookContent
import com.fiction.domain.model.BookSettingsData
import com.fiction.domain.model.LastItemOfChapter
import com.fiction.domain.model.enums.ColorThemeData
import com.fiction.domain.model.enums.FlipTypeData
import com.fiction.domain.model.enums.LineHeightData
import com.fiction.domain.model.enums.TextTypeData
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentReaderBinding
import com.fiction.me.extensions.dpToPx
import com.fiction.me.extensions.preventScreenshot
import com.fiction.me.extensions.startHeaderTextAnimation
import com.fiction.me.page_curl_lib.page_curling.CurlView
import com.fiction.me.page_curl_lib.page_curling.CurlViewEventsHandler
import com.fiction.me.page_curl_lib.page_curling.textures_manager.PageLoadingEventsHandler
import com.fiction.me.page_curl_lib.user_actions_managing.Area
import com.fiction.me.page_curl_lib.user_actions_managing.Point
import com.fiction.me.ui.analytics.Analytics
import com.fiction.me.ui.fragments.chapterlist.ChapterListBottomDialog
import com.fiction.me.ui.fragments.purchase.AutoUnlockBottomDialog
import com.fiction.me.ui.fragments.purchase.CoinShopFragment.Companion.FROM_COIN_STORE
import com.fiction.me.ui.fragments.purchase.CoinShopFragment.Companion.IS_SUCCEEDED_IN_GETTING_COIN
import com.fiction.me.ui.fragments.reader.ToolsBottomSheetFragment.Companion.CHANGE_SELECTION_STATE
import com.fiction.me.ui.fragments.reader.ToolsBottomSheetFragment.Companion.SEND_CHANGED_BOOK_INFO
import com.fiction.me.ui.fragments.reader.adapters.FlipPagerAdapter
import com.fiction.me.ui.fragments.reader.custom_compose_dialog.CustomAutoUnlockDialog
import com.fiction.me.ui.fragments.reader.custom_compose_dialog.ProgressDialog
import com.fiction.me.ui.fragments.reader.provider.RawResourcesBitmapProvider
import com.fiction.me.ui.fragments.retention.RetentionInReaderBottomDialog
import com.fiction.me.utils.Constants.Companion.BARS_FADE_DURATION_IN_SECONDS
import com.fiction.me.utils.Constants.Companion.MAX_BRIGHTNESS
import com.fiction.me.utils.Events
import com.fiction.me.utils.Events.Companion.BOOK_EXCLUSIVE
import com.fiction.me.utils.Events.Companion.BOOK_ID_PROPERTY
import com.fiction.me.utils.Events.Companion.CHAPTER
import com.fiction.me.utils.Events.Companion.CHAPTER_NUMBER
import com.fiction.me.utils.Events.Companion.CHAPTER_TYPE
import com.fiction.me.utils.Events.Companion.DARK
import com.fiction.me.utils.Events.Companion.DURATION
import com.fiction.me.utils.Events.Companion.ENTER_TYPE
import com.fiction.me.utils.Events.Companion.FREE
import com.fiction.me.utils.Events.Companion.INITIAL
import com.fiction.me.utils.Events.Companion.LIGHT
import com.fiction.me.utils.Events.Companion.NEXT_CHAPTER_SCREEN_SHOWN
import com.fiction.me.utils.Events.Companion.PAGE
import com.fiction.me.utils.Events.Companion.PAID
import com.fiction.me.utils.Events.Companion.READER_SCREEN_THEME_MODE
import com.fiction.me.utils.Events.Companion.READING_SCREEN_MENU_SHOWN
import com.fiction.me.utils.Events.Companion.READING_SCREEN_SHOWN
import com.fiction.me.utils.Events.Companion.READING_START
import com.fiction.me.utils.Events.Companion.REFERRER
import com.fiction.me.utils.Events.Companion.RETURN
import com.fiction.me.utils.Events.Companion.STATE
import com.fiction.me.utils.Events.Companion.UNLOCK_NOW_SCREEN_SHOWN
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.concurrent.timerTask

class ReaderFragment : FragmentBaseNCMVVM<ReaderViewModel, FragmentReaderBinding>() {
    override val binding: FragmentReaderBinding by viewBinding()
    override val viewModel: ReaderViewModel by viewModel()
    private val args: ReaderFragmentArgs by navArgs()
    private val defaultLineSpace = 9f
    private val biggerLineSpace = 13f
    private var countOfReadPage = 0
    private var curlView: CurlView? = null
    private var currentPageIndex: Int? = 0
    private var bitmapList: MutableList<Bitmap> = mutableListOf()
    private var rawResourcesBitmapProvider: RawResourcesBitmapProvider? = null
    private var bookPageWidth = 800
    private var bookPageHeight = 1200
    private lateinit var uuid: Uuid
    private var funTimer: Timer? = null
    private var lastChapterPageBitmap: Bitmap? = null
    private var finishedBookPageBitmap: Bitmap? = null
    private val autoUnlockBottomDialog = AutoUnlockBottomDialog()
    private val mapChapterLastItem: MutableMap<ColorThemeData, Bitmap> = mutableMapOf()
    private val mapFinishedItem: MutableMap<ColorThemeData, Bitmap> = mutableMapOf()
    private var adapter: FlipPagerAdapter? = null
    private var btnDialog: ChapterListBottomDialog = ChapterListBottomDialog()

    var width = 0
    var height = 0
    var counter = 0

    override fun onView() {
        context?.let { context ->
            viewModel.isShowProgressBar.value = false
            binding.progressContainer.isVisible = true
            setBackgroundTheme(args.colorTheme, context)
            lifecycleScope.launch {
                if (view != null) {
                    delay(1000)
                    with(binding) {
                        bookPageWidth = chapterLastPage.width
                        bookPageHeight = chapterLastPage.height
                        mapChapterLastItem[ColorThemeData.BLACK] =
                            getBitmapFromViewGroup(chapterLastPage)
                        chapterLastPage.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.sepia_900
                            )
                        )
                        titleBook.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.black_100
                            )
                        )
                        description.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.black_200
                            )
                        )

                        mapChapterLastItem[ColorThemeData.SEPIA] =
                            getBitmapFromViewGroup(chapterLastPage)

                        chapterLastPage.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.white
                            )
                        )
                        mapChapterLastItem[ColorThemeData.WHITE] =
                            getBitmapFromViewGroup(chapterLastPage)
                        chapterLastPage.isVisible = false
                        descriptionFinishedPage.text =
                            if (args.isCompleted) context.resources.getString(
                                R.string.you_have_completed_book
                            ) else context.resources.getString(R.string.you_have_finished_ongoing)

                        mapFinishedItem[ColorThemeData.BLACK] =
                            getBitmapFromViewGroup(finishedItem)

                        finishedItem.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.sepia_900
                            )
                        )
                        titleFinishedPage.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.black_100
                            )
                        )
                        descriptionFinishedPage.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.black_200
                            )
                        )
                        mapFinishedItem[ColorThemeData.SEPIA] =
                            getBitmapFromViewGroup(finishedItem)

                        finishedItem.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.white
                            )
                        )
                        mapFinishedItem[ColorThemeData.WHITE] =
                            getBitmapFromViewGroup(finishedItem)
                        finishedItem.isVisible = false
                        backgroundView.isVisible = false
                    }
                    startReader()
                }
            }
        }
        binding.progressDialog.setContent {
            ProgressDialog(viewModel.progressState)
        }
    }

    private fun changeVisibility() {
        with(binding) {
            chapterLastPage.setBackgroundColor(Color.TRANSPARENT)
            chapterLastPage.isVisible = true
            titleBook.visibility = View.INVISIBLE
            description.visibility = View.INVISIBLE
            goForNext.visibility = View.VISIBLE
            goForNext.text = ""
            progressCircular.visibility = View.VISIBLE
        }
    }

    private fun hideVisibility() {
        with(binding) {
            context?.let {
                chapterLastPage.setBackgroundColor(ContextCompat.getColor(it, R.color.black_100))
            }
            chapterLastPage.isVisible = false
            titleBook.visibility = View.VISIBLE
            description.visibility = View.VISIBLE
            goForNext.visibility = View.VISIBLE
            goForNext.text = resources.getString(R.string.go_for_next)
            progressCircular.visibility = View.GONE
        }
    }

    private fun startReader() {
        timerForReadingAnalytics()
        getFragmentResult()
        binding.goForNext.isVisible = true
        viewModel.run {
            setChapterLockState(args.isChapterLocked)
            updateLibCurrentReadBooks(args.isUpdateCurrentBooks)
            cacheTariffs()
        }
        if (!viewModel.isNextChapterLocked || args.isChapterLocked) {
            val vto = binding.pages.viewTreeObserver
            vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding.pages.viewTreeObserver
                        .removeOnGlobalLayoutListener(this)
                    with(binding) {
                        width = pages.measuredWidth
                        height = pages.measuredHeight
                        bookPageWidth = bookPageText.width
                        bookPageHeight = bookPageText.height
                    }
                }
            })
            Log.i("notificationClicked", "onCreate: ${args.bookId}")
            viewModel.run {
                trackEvents(READING_SCREEN_SHOWN, hashMapOf(BOOK_ID_PROPERTY to args.bookId))
                getInfoById(args.bookId, args.chapterId)
            }
            if (args.isChapterLocked) {
                getOnlyGoNextChapterItem()
            }

        } else viewModel.run {
            isNextChapterLocked = false
        }

        viewModel.ticker = BARS_FADE_DURATION_IN_SECONDS
        with(binding) {
            toolbar.run {
                setFirstItem(R.drawable.ic_bookmark_stroke)
                setSecondItem(R.drawable.ic_more)
                setBackIcon(R.drawable.ic_back)
            }
            pages.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    viewModel.setCurrentPagePosition(position)
                    val countOfReadPage = position + 1
                    val countOfBookPages = pages.adapter?.itemCount ?: 1
                    readPercent.text = getPercentageOfReadPages(countOfReadPage, countOfBookPages)
                    super.onPageSelected(position)
                }
            })
        }
        activity?.onBackPressedDispatcher?.addCallback(this) {
            viewModel.run {
                setReadProgress()
                insertBookInDB()
            }
            openRetentionBottomDialog()
        }
        if (viewModel.isChangedUnlockState)
            openLockedStateDialog()
    }

    private fun getPercentageOfReadPages(countOfReadPages: Int, countOfBookPages: Int) =
        viewModel.countPercentageOfReadPages(
            countOfReadPages,
            countOfBookPages
        )

    override fun onViewClick() {
        with(binding) {
            toolbar.setOnSecondItemClickListener {
                viewModel.ticker = BARS_FADE_DURATION_IN_SECONDS
                BottomSheetFragmentMore().let {
                    it.show(childFragmentManager, BottomSheetFragmentMore::class.java.simpleName)
                    it.setReportClickListener {
                        val directions =
                            ReaderFragmentDirections.actionReaderFragmentToReportFragment(
                                viewModel.currentReadChapterId,
                                viewModel.currentReadBookId,
                                viewModel.readPercent.toFloat(),
                                viewModel.bookName
                            )
                        navigateFragment(directions)
                    }
                    viewModel.trackEvents(
                        READING_SCREEN_MENU_SHOWN,
                        hashMapOf(BOOK_ID_PROPERTY to viewModel.currentReadBookId) // TODO set real book name
                    )
                }
            }

            toolbar.setOnBackClickListener {
                viewModel.run {
                    setReadProgress()
                    insertBookInDB()
                }
                openRetentionBottomDialog()
            }

            toolbar.setOnFirstItemClickListener {
                viewModel.ticker = BARS_FADE_DURATION_IN_SECONDS
                viewModel.addOrRemoveBook()
            }

            toolsBtn.setOnClickListener {
                viewModel.ticker = BARS_FADE_DURATION_IN_SECONDS
                ToolsBottomSheetFragment().show(
                            childFragmentManager,
                    ToolsBottomSheetFragment::class.java.simpleName
                )
            }

            chapterBtn.setOnClickListener {
                viewModel.ticker = BARS_FADE_DURATION_IN_SECONDS
                btnDialog = ChapterListBottomDialog()
                btnDialog.onItemClickListener = { id ->
                    viewModel.progressState.value = true
                    viewModel.run {
                        currentReadChapterId = id
                        readPercent = 0.0
                        setReadProgress()
                        viewModel.settingsUpdated = true
                        getChapterInfo(id)
                    }
                }

                val argsBundle = Bundle().apply {
                    putSerializable(
                        ARGUMENT_FOR_OPENING_CHAPTER_DIALOG,
                        OpenedFromWhere.FROM_READER
                    )
                    putLong(
                        BOOK_ID_FOR_CHAPTERS_FROM_READER,
                        viewModel.bookSummaryInfo?.bookData?.id ?: viewModel.currentReadBookId
                    )
                    putString(BOOK_NAME, viewModel.bookName)
                }
                btnDialog.arguments = argsBundle
                btnDialog.show(childFragmentManager, ChapterListBottomDialog::class.java.simpleName)
            }

            modeBtn.setOnClickListener {
                viewModel.ticker = BARS_FADE_DURATION_IN_SECONDS
                when (viewModel.bookToolsSetting?.colorTheme) {
                    ColorThemeData.SEPIA -> {
                        modeIcon.setImageResource(R.drawable.ic_dark_mode)
                        val updatedSettings =
                            viewModel.bookToolsSetting?.copy(colorTheme = ColorThemeData.WHITE)
                        viewModel.changeBookSettings(updatedSettings)
                    }

                    ColorThemeData.BLACK -> {
                        modeIcon.setImageResource(R.drawable.ic_dark_mode)
                        val updatedSettings =
                            viewModel.bookToolsSetting?.copy(colorTheme = ColorThemeData.WHITE)
                        viewModel.changeBookSettings(updatedSettings)
                    }

                    else -> {
                        modeIcon.setImageResource(R.drawable.ic_light_mode)
                        val updatedSettings =
                            viewModel.bookToolsSetting?.copy(colorTheme = ColorThemeData.BLACK)
                        viewModel.changeBookSettings(updatedSettings)
                    }
                }
            }

            goForNext.setOnClickListener {
                Log.i("goForNext", "onViewClick: ")
                viewModel.openNextChapter()
            }

            readPercent.setOnClickListener {
                changeBarsVisibility()
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
                val state = if (it?.colorTheme == ColorThemeData.BLACK) DARK else LIGHT
                viewModel.trackEvents(
                    READER_SCREEN_THEME_MODE,
                    hashMapOf(
                        STATE to state,
                        BOOK_ID_PROPERTY to viewModel.currentReadBookId
                    )
                )
                if (!args.isChapterLocked)
                    setBookSettings(it)
            }
            onEach(viewModel.chapterInfo) {
                //progressContainer.isVisible = false
                viewModel.settingsUpdated = true
                setBookSettings(viewModel.bookToolsSetting)
                val readingStatus = if (viewModel.readingStatus() == true) INITIAL else RETURN
                val chapterType = if (viewModel.isChapterFree()) FREE else PAID
                viewModel.trackEvents(
                    READING_START,
                    hashMapOf(
                        ENTER_TYPE to readingStatus,
                        BOOK_ID_PROPERTY to viewModel.currentReadBookId,
                        CHAPTER to viewModel.chapterTitle,
                        CHAPTER_NUMBER to viewModel.getChapterOrder(),
                        PAGE to countOfReadPage,
                        CHAPTER_TYPE to chapterType,
                        REFERRER to args.referrer,
                        BOOK_EXCLUSIVE to (viewModel.bookSummaryInfo?.bookData?.isEsclusive ?: false)
                    )
                )
                viewModel.duration = java.lang.System.currentTimeMillis()
                context?.resources?.getString(R.string.chapter_)?.format(it?.order ?: 1)
                    ?.let { title -> toolbar.setTitleText(title) }
                viewModel.run {
                    setReadProgress()
                    if (isPagesFirstInit) {
                        binding.progressContainer.isVisible = false
                        isPagesFirstInit = false
                    }
                }
            }
            onEach(viewModel.bookInfo) {
                setDrawableForAddLibState()
                if (!args.isChapterLocked)
                    viewModel.getChapterInfo()
            }
            onEach(viewModel.addBookToLib) {
                with(binding) {
                    customSnackBar.startHeaderTextAnimation(R.string.added_to_library)
                    context?.let {
                        toolbar.setFirstItem(R.drawable.ic_saved_library_bookmark)
                    }
                }
            }
            onEach(viewModel.removeBookFromLib) {
                with(binding) {
                    customSnackBar.startHeaderTextAnimation(R.string.deleted_from_library)
                    binding.toolbar.setFirstItem(R.drawable.ic_bookmark_stroke)
                }
            }
            onEach(viewModel.unlockChapter) { chapterId ->
                viewModel.run {
                    showUnlockDialog()
                    adapter?.stopProgressListener?.let { it() }
                    viewModel.unlockSwitchState.value = isTurnOnUnlock
                    binding.bottomSheet.setContent {
                        ChapterPurchaseBottomDialog(
                            myBalance,
                            isTurnOnUnlock,
                            isUnlockBottomDialogShown,
                            getChapterCost(chapterId),
                            {
                                sendUnlockEvent(UNLOCK_NOW_SCREEN_SHOWN)
                            },
                            {
                                sendUnlockEvent(Events.UNLOCK_NOW_CLICKED)
                                buyChapterOrCoin()
                            }, {
                                val state = if (it) Events.ON else Events.OFF
                                viewModel.trackEvents(
                                    Events.CHAPTERS_AUTO_UNLOCK_SWITCHED, hashMapOf(
                                        STATE to state
                                    )
                                )
                                viewModel.setUserPropertyEvent(mapOf(
                                    USER_PROPERTY__CHAPTERS_AUTO_UNLOCK to state
                                ))
                                isTurnOnUnlock = it
                                viewModel.unlockSwitchState.value = isTurnOnUnlock
                                changeAutoUnlockMode(it)
                            }, {
                                hideUnlockDialog()
                                if (viewModel.isChangedUnlockState && !viewModel.openCoinShop)
                                    openLockedStateDialog()
                                viewModel.openCoinShop = false
                            },
                            viewModel.unlockSwitchState,
                            viewModel.isShowProgressBar
                        )
                    }
                }
            }
            onEach(viewModel.buyCoins) {
                if (viewModel.isNextChapterLocked) {
                    val directions =
                        ReaderFragmentDirections.actionReaderFragmentToGetCoinFromStoreFragment()
                    viewModel.openCoinShop = true
                    navigateFragment(directions)
                }
            }
            onEach(viewModel.isTurnOnAutoUnlock) {
                if (!it) {
                    autoUnlockBottomDialog.show(
                        childFragmentManager,
                        AutoUnlockBottomDialog::class.java.simpleName
                    )
                    autoUnlockBottomDialog.onAutoUnlockListener = { isTurnOn ->
                        viewModel.openNextChapter()
                    }
                } else viewModel.openNextChapter()
            }
            onEach(viewModel.isNextBtnClicked) {
                if (it == true) {
                    lifecycleScope.launch {
                        delay(200)
                        changeVisibility()
                        viewModel.openNextChapter()
                        viewModel.setClickState(false)
                    }
                }
            }
            onEach(viewModel.dismissDialog) {
                if (btnDialog.isVisible)
                    btnDialog.dismissDialog()
                if (binding.chapterLastPage.isVisible)
                    hideVisibility()
                if (progressContainer.isVisible)
                    progressContainer.isVisible = false

            }
            onEach(viewModel.progressBarVisibility) {
                if (progressContainer.isVisible)
                    progressContainer.isVisible = false
            }
        }
    }

    private fun openRetentionBottomDialog() {
        val retentionDialog = RetentionInReaderBottomDialog.newInstance(
            viewModel.currentReadBookId,
            viewModel.bookName
        )
        retentionDialog.run {
            dismissListener = { isGoBack ->
                if (isGoBack) {
                    if (args.isFromOnBoarding) {
                        val direction =
                            ReaderFragmentDirections.actionReaderFragmentToExploreFragment()
                        navigateFragment(direction)
                    } else popBackStack()
                }
            }
            startReadListener = { bookId ->
                setView(bookId)
                this@ReaderFragment.viewModel.getInfoById(bookId)
            }
        }
        retentionDialog.show(
            childFragmentManager,
            RetentionInReaderBottomDialog::class.java.simpleName
        )
    }

    private fun setView(bookId: Long) {
        viewModel.setViewInBook(bookId)
    }

    private fun setBookSettings(
        bookSettingsData: BookSettingsData?
    ) {
        bookSettingsData?.let { bookSettings ->
            val lineSpace = getLineSpace(bookSettings.lineHeight, bookSettings.flipType)
            val (contextFont, titleFont) = getTextFont(bookSettings.textType)
            setColorTheme(bookSettings.colorTheme, bookSettings.flipType)
            setBrightness(bookSettings.brightness)
            val pageSplitter =
                getChapterPages(contextFont, titleFont, lineSpace, bookSettings.textSize)
            val bookPages = pageSplitter?.getPages(viewModel.isLastChapter()) ?: emptyList()
            if (bookSettings.flipType == FlipTypeData.TURN_PAGE) {
                binding.bookPageText.run {
                    textSize = bookSettings.textSize.toFloat()
                    typeface = contextFont
                }
            }
            counter++
            if (bookSettings.flipType == FlipTypeData.TURN_PAGE) {
                //binding.backgroundView.isVisible = true
                // binding.chapterLastPage.isVisible = true
                if (viewModel.settingsUpdated && curlView != null) {
                    updateCurlViewPages(bookPages, viewModel.getCurrentPagePosition())
                    changeVisibleBookViews(true)
                    viewModel.hideUnlockDialog()
                    if (viewModel.isShowProgressBar.value) viewModel.isShowProgressBar.value = false
                    //binding.chapterLastPage.isVisible = false
                    viewModel.settingsUpdated = false
                } else
                    initCurlView(bookPages)
            } else {
                //binding.backgroundView.isVisible = false
                getPagerOrientation(bookSettings.flipType)
                initAdapter(bookSettings, bookPages)
                adapter?.stopProgressListener?.let { it() }
            }
        }
    }

    private fun getNextChapter(isGoingNextChapter: Boolean) {
        if (isGoingNextChapter) {
            viewModel.openNextChapter()
            val chapterType = if (viewModel.isChapterFree()) FREE else PAID
            val chapterNumber = viewModel.chapterInfo.value?.order ?: 0
            viewModel.trackEvents(
                NEXT_CHAPTER_SCREEN_SHOWN,
                hashMapOf(
                    BOOK_ID_PROPERTY to viewModel.currentReadBookId,
                    CHAPTER_NUMBER to chapterNumber,
                    CHAPTER_TYPE to chapterType
                )
            )
        } else {
            changeBarsVisibility()
        }
    }

    private fun changeBarsVisibility() {
        with(binding) {
            appBar.isVisible = !appBar.isVisible
            bottomBar.isVisible = !bottomBar.isVisible
            if (appBar.isShown)
                viewModel.ticker = BARS_FADE_DURATION_IN_SECONDS
        }
    }

    private fun changeVisibleBookViews(isTypeCurl: Boolean) {
        with(binding) {
            bookPageText.isVisible = isTypeCurl
            curlPageView.isVisible = isTypeCurl
            pages.isVisible = !isTypeCurl
        }
    }

    private fun changeChapterLastItemVisible() {
        with(binding) {
            //chapterLastPage.isVisible = true
            description.visibility = View.INVISIBLE
            titleBook.visibility = View.INVISIBLE
        }
    }

    private fun changeColorTheme(flipType: FlipTypeData, backgroundColor: Int, textColor: Int) {
        with(binding) {
            context?.let { context ->
                val view = if (flipType == FlipTypeData.TURN_PAGE) {
                    bookPageText.setTextColor(ContextCompat.getColor(context, textColor))
                    bookPageText
                } else pages
                view.setBackgroundColor(
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

    private fun getOnlyGoNextChapterItem() {
        val adapter = FlipPagerAdapter(BookSettingsData.defaultItem()) { isGoingNextChapter ->
            if (isGoingNextChapter)
                viewModel.openNextChapter()
            else {
                changeBarsVisibility()
            }
        }
        binding.pages.adapter = adapter
        adapter.submitList(listOf(LastItemOfChapter()))
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

        activity?.supportFragmentManager?.setFragmentResultListener(
            FROM_COIN_STORE,
            viewLifecycleOwner
        ) { _, result ->
            val isSucceeded = result.getBoolean(IS_SUCCEEDED_IN_GETTING_COIN)
            if (isSucceeded) viewModel.getPaidChapter()
        }
    }

    private fun setDrawableForAddLibState() {
        if (viewModel.isAddedToLib) {
            context?.let {
                binding.toolbar.setFirstItem(R.drawable.ic_saved_library_bookmark)
            }
        } else {
            context?.let {
                binding.toolbar.setFirstItem(R.drawable.ic_bookmark_stroke)
            }
        }
    }

    /*    private fun showError() {
            MainDialogFragment.newInstance(
                resources.getString(R.string.error),
                resources.getString(R.string.error_content)
            ).show(childFragmentManager, MainDialogFragment::class.java.simpleName)
        }

           private fun showWellDone() {
                val mainDialogFragment = MainDialogFragment.newInstance(
                    resources.getString(R.string.well_done),
                    resources.getString(R.string.well_done_content)
                )
                mainDialogFragment.show(childFragmentManager, MainDialogFragment::class.java.simpleName)
                mainDialogFragment.setPositiveButtonClickListener = {
                    viewModel.getLockState()
                }
            }*/

    private fun initAdapter(bookSettings: BookSettingsData, bookPages: List<BookContent>) {
        with(binding) {
            changeVisibleBookViews(false)
            adapter = FlipPagerAdapter(
                bookSettings,
                viewModel.getIsBookCompleted()
            ) { isGoingNextChapter ->
                getNextChapter(isGoingNextChapter)
            }
            pages.adapter = adapter
            adapter?.submitList(bookPages)
            viewModel.hideUnlockDialog()
            viewModel.isShowProgressBar.value = false
            viewModel.hideProgressBar()
            pages.setCurrentItem(viewModel.getCurrentPagePosition(), false)
        }
    }

    private fun initCurlView(bookPages: List<BookContent>) {
        viewModel.hideUnlockDialog()
        if (viewModel.isShowProgressBar.value) viewModel.isShowProgressBar.value = false
        if (bookPages[0] is BookChapterText && (bookPages[0] as BookChapterText).chapterPage.isEmpty()) return
        with(binding) {
            changeVisibleBookViews(true)
            addPagesAsImage(bookPages)
            bookPageText.isVisible = false
            rawResourcesBitmapProvider = RawResourcesBitmapProvider(bitmapList)
            currentPageIndex = viewModel.getCurrentPagePosition()

            curlView = curlPageView.also {
                it.setBitmapProvider(rawResourcesBitmapProvider!!)
                it.initCurrentPageIndex(currentPageIndex!!)
                it.touchGoNextBtnListener = {
                    if (currentPageIndex == bitmapList.size - 1) {
                        Log.i("touchGoNextBtnListener", "initCurlView: ")
                        viewModel.setClickState(true)
                    }
                }
                it.setGoNextBtnCoordinates(
                    binding.goForNext.x.toInt(),
                    getTouchMaxXY(binding.goForNext.x, 328),
                    binding.goForNext.y.toInt(),
                    getTouchMaxXY(binding.goForNext.y, 56)
                )
                //it.setBackgroundColor(Color.WHITE)
                it.setHotAreas(listOf(Area(0, Point(0, 0), Size(100, 100))))
                it.onContentTouchListener = {
                    viewModel.ticker = BARS_FADE_DURATION_IN_SECONDS
                    appBar.isVisible = !appBar.isVisible
                    bottomBar.isVisible = !bottomBar.isVisible
                }

                it.setExternalEventsHandler(object : CurlViewEventsHandler {
                    override fun onPageChanged(newPageIndex: Int) {
                        currentPageIndex = newPageIndex
                        viewModel.setCurrentPagePosition(newPageIndex)
                        CoroutineScope(Dispatchers.Main).launch {
                            if (bitmapList.size - 1 == newPageIndex)
                                changeChapterLastItemVisible()
                            else chapterLastPage.isVisible = false
                            readPercent.text =
                                getPercentageOfReadPages(newPageIndex + 1, bitmapList.size)
                        }
                    }

                    override fun onHotAreaPressed(areaId: Int) {
                        it.setCurrentPageIndex(currentPageIndex ?: 0)
                    }
                })

                it.setOnPageLoadingListener(object : PageLoadingEventsHandler {
                    override fun onLoadingStarted() {
                        Log.i("ToolsChange", "onLoadingStarted: ")
                        // findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
                    }

                    override fun onLoadingCompleted() {
                        Log.i("ToolsChange", "onLoadingCompleted: ")
                        // findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                    }

                    override fun onLoadingError() {
                        Log.i("ToolsChange", "onLoadingCompleted: ")
                        // Toast.makeText(this@CurlActivity, R.string.generalError, Toast.LENGTH_SHORT).show()
                    }
                })
            }
            binding.progressContainer.isVisible = false
        }
    }

    private fun getTouchMaxXY(xOry: Float, buttonWidth: Int) =
        context?.let {
            (xOry + buttonWidth.dpToPx(it)).toInt()
        } ?: 0

    private fun updateCurlViewPages(bookPages: List<BookContent>, pagePosition: Int = -1) {
        with(binding) {
            curlPageView.isVisible = false
            bitmapList.clear()
            bookPageText.isVisible = true
            addPagesAsImage(bookPages)
            bookPageText.isVisible = false
            rawResourcesBitmapProvider?.updateBitmaps(bitmapList)
            curlView?.resetAndInit(pagePosition)
            curlPageView.isVisible = true
        }
    }

    private fun addPagesAsImage(bookPages: List<BookContent>) {
        binding.backgroundView.isVisible = false
        bookPages.forEach {
            if (it is BookChapterText) {
                val bitmap = getBitmapFromTextView(it.chapterPage)
                bitmapList.add(bitmap)
            }
        }
        if (viewModel.isLastChapter())
            finishedBookPageBitmap?.let { bitmapList.add(it) }
        else lastChapterPageBitmap?.let { bitmapList.add(it) }
    }

    private fun getBitmapFromTextView(chapterPage: CharSequence): Bitmap {
        binding.bookPageText.text = chapterPage
        val bitmap = Bitmap.createBitmap(
            bookPageWidth, bookPageHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        binding.bookPageText.draw(canvas)
        return bitmap
    }

    private fun getBitmapFromViewGroup(layout: ConstraintLayout): Bitmap {
        val bitmap = Bitmap.createBitmap(
            bookPageWidth, bookPageHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        layout.draw(canvas)
        return bitmap
    }

    private fun getBitmapFromViewGroup(layout: LinearLayout): Bitmap {
        val bitmap = Bitmap.createBitmap(
            bookPageWidth, bookPageHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        layout.draw(canvas)
        return bitmap
    }

    private fun getChapterPages(
        contextFont: Typeface?,
        titleFont: Typeface?,
        lineSpace: Float,
        textSize: Int?
    ): PageSplitter? {
        val pageSplitter = contextFont?.let { type ->
            PageSplitter(
                width - resources.getDimensionPixelSize(R.dimen.space_32),
                height - resources.getDimensionPixelSize(R.dimen.space_16),
                lineSpace.toInt(),
                type,
                titleFont
            )
        }
        context?.let { context ->
            textSize?.dpToPx(context)?.let { size ->
                val titleSize = (textSize + 4).dpToPx(context)
                pageSplitter?.append(
                    viewModel.chapterTitle,
                    viewModel.chapterContext,
                    size.toFloat(),
                    titleSize.toFloat()
                )
            }
        }
        return pageSplitter
    }

    private fun getTextFont(textType: TextTypeData?): Pair<Typeface?, Typeface?> {
        context?.let { context ->
            when (textType) {
                TextTypeData.PT_SERIF -> {
                    val contextFont =
                        ResourcesCompat.getFont(context, R.font.ptserif_regular)
                    val titleFont =
                        ResourcesCompat.getFont(context, R.font.ptserif_bold)
                    return Pair(contextFont, titleFont)
                }

                else -> {
                    val contextFont =
                        ResourcesCompat.getFont(context, R.font.roboto_regular_400)
                    val titleFont =
                        ResourcesCompat.getFont(context, R.font.roboto_bold_700)
                    return Pair(contextFont, titleFont)
                }
            }
        } ?: return Pair(null, null)
    }

    private fun getLineSpace(lineHeight: LineHeightData, flipType: FlipTypeData) =
        when (lineHeight) {
            LineHeightData.DEFAULT -> {
                if (flipType == FlipTypeData.TURN_PAGE)
                    binding.bookPageText.setLineSpacing(defaultLineSpace, 1f)
                defaultLineSpace
            }

            else -> {
                binding.bookPageText.setLineSpacing(biggerLineSpace, 1f)
                biggerLineSpace
            }
        }

    private fun setColorTheme(colorTheme: ColorThemeData, flipType: FlipTypeData) {
        when (colorTheme) {
            ColorThemeData.BLACK -> {
                changeColorTheme(flipType, R.color.black_100, R.color.white)
                lastChapterPageBitmap = mapChapterLastItem[ColorThemeData.BLACK]
                finishedBookPageBitmap = mapFinishedItem[ColorThemeData.BLACK]
            }

            ColorThemeData.SEPIA -> {
                changeColorTheme(flipType, R.color.sepia_900, R.color.black_100)
                lastChapterPageBitmap = mapChapterLastItem[ColorThemeData.SEPIA]
                finishedBookPageBitmap = mapFinishedItem[ColorThemeData.SEPIA]
            }

            else -> {
                changeColorTheme(flipType, R.color.white, R.color.black_100)
                lastChapterPageBitmap = mapChapterLastItem[ColorThemeData.WHITE]
                finishedBookPageBitmap = mapFinishedItem[ColorThemeData.WHITE]
            }
        }
    }

    private fun setBackgroundTheme(colorTheme: ColorThemeData, context: Context) {
        with(binding) {
            when (colorTheme) {
                ColorThemeData.BLACK -> backgroundView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.black_100
                    )
                )

                ColorThemeData.SEPIA -> backgroundView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.sepia_900
                    )
                )

                else -> backgroundView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )
            }
        }
    }

    private fun setBrightness(brightness: Int) {
        if (brightness != 0) {
            activity?.window?.let { window ->
                val layoutsParams: WindowManager.LayoutParams = window.attributes
                layoutsParams.screenBrightness =
                    brightness / MAX_BRIGHTNESS.toFloat()
                window.attributes = layoutsParams
            }
        }
    }

    private fun getPagerOrientation(flipType: FlipTypeData) {
        when (flipType) {
            FlipTypeData.SHIFT -> binding.pages.orientation =
                ViewPager2.ORIENTATION_HORIZONTAL

            FlipTypeData.SCROLL -> binding.pages.orientation =
                ViewPager2.ORIENTATION_VERTICAL

            else -> {}
        }
    }

    private fun setLastItemTextColor(titleColor: Int, descriptionColor: Int) {
        with(binding) {
            context?.let { context ->
                titleBook.setTextColor(
                    ContextCompat.getColor(
                        context,
                        titleColor
                    )
                )
                description.setTextColor(
                    ContextCompat.getColor(
                        context,
                        descriptionColor
                    )
                )
            }
        }
    }

    private fun doneSuccessfully(message: Int) {
        binding.customSnackBar.startHeaderTextAnimation(message)
    }

    private fun openLockedStateDialog() {
        viewModel.isChangedUnlockState = false
        binding.autoUnlockDialog.isVisible = true
        viewModel.onAutoUnlockStateChanged()
        binding.autoUnlockDialog.setContent {
            CustomAutoUnlockDialog({
                binding.autoUnlockDialog.isVisible = false
                viewModel.onDismissDialog()
            }, {
                viewModel.changeAutoUnlockMode(it)
            },
                viewModel.isTurnOnUnlock,
                viewModel.isDialogShown
            )
            viewModel.isChangedUnlockState = false
        }
    }

    override fun onStart() {
        requireActivity().preventScreenshot(true)
        super.onStart()
    }

    override fun onStop() {
        viewModel.trackEvents(
            Events.READING_END,
            hashMapOf(
                BOOK_ID_PROPERTY to viewModel.currentReadBookId,
                CHAPTER to viewModel.chapterTitle,
                CHAPTER_NUMBER to viewModel.getChapterOrder(),
                PAGE to countOfReadPage,
                CHAPTER_TYPE to if (viewModel.isChapterFree()) FREE else PAID,
                BOOK_EXCLUSIVE to (viewModel.bookSummaryInfo?.bookData?.isEsclusive ?: false),
                DURATION to ((java.lang.System.currentTimeMillis() - viewModel.duration) / 1000).toInt()
            )
        )
        requireActivity().preventScreenshot(false)
        super.onStop()
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
        timerCancel()
    }

    private fun timerForReadingAnalytics() {
        funTimer = Timer()
        uuid = Uuid(requireContext())
        val deviceSessionID = UUID.randomUUID().toString().replace("-", " ")
        funTimer?.scheduleAtFixedRate(
            timerTask()
            {
                with(viewModel) {
                    sendAnalytics(currentReadBookId, deviceSessionID, uuid.getUuid())
                }
            }, 5000, 5000
        )
    }

    private fun sendUnlockEvent(eventName: String) {
        val bookChapter = viewModel.getChapterOrder()
        adapter?.stopProgressListener?.let { it() }
        viewModel.trackEvents(
            eventName,
            hashMapOf(
                CHAPTER_NUMBER to bookChapter,
                BOOK_ID_PROPERTY to viewModel.currentReadBookId
            ),
            eventsType = Analytics.EventsType.ALL
        )
    }

    private fun timerCancel() {
        funTimer?.cancel()
    }

    companion object {
        const val ARGUMENT_FOR_OPENING_CHAPTER_DIALOG = "ARGUMENT_FOR_OPENING_CHAPTER_DIALOG"
        const val BOOK_ID_FOR_CHAPTERS_FROM_READER = "BOOK_ID_FOR_CHAPTERS_FROM_READER"
        const val BOOK_NAME = "bookName"
    }
}