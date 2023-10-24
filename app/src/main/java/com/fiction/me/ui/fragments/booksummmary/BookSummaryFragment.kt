package com.fiction.me.ui.fragments.booksummmary

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.analytics.api.Events.USER_PROPERTY__CHAPTERS_AUTO_UNLOCK
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.fiction.domain.model.BookInfo
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentBookSummaryBinding
import com.fiction.me.extensions.fromGson
import com.fiction.me.extensions.startHeaderTextAnimation
import com.fiction.me.extensions.toFormatViewsLikes
import com.fiction.me.ui.analytics.Analytics
import com.fiction.me.ui.fragments.chapterlist.ChapterListBottomDialog
import com.fiction.me.ui.fragments.explore.GetMoreCoinsDialogFragment
import com.fiction.me.ui.fragments.purchase.AutoUnlockBottomDialog
import com.fiction.me.ui.fragments.purchase.CoinShopFragment.Companion.COIN_SHOP
import com.fiction.me.ui.fragments.purchase.CoinShopFragment.Companion.IS_CLOSED
import com.fiction.me.ui.fragments.purchase.maindialog.MainDialogFragment
import com.fiction.me.ui.fragments.reader.ChapterPurchaseBottomDialog
import com.fiction.me.ui.fragments.reader.OpenedFromWhere
import com.fiction.me.ui.fragments.reader.ReaderFragment
import com.fiction.me.ui.fragments.reader.ReaderFragment.Companion.ARGUMENT_FOR_OPENING_CHAPTER_DIALOG
import com.fiction.me.ui.fragments.reader.custom_compose_dialog.CustomAutoUnlockDialog
import com.fiction.me.ui.fragments.reader.custom_compose_dialog.ProgressDialog
import com.fiction.me.utils.Constants.Companion.BOOKS_CONTENT_18_PLUS
import com.fiction.me.utils.Constants.Companion.MAX_LINES
import com.fiction.me.utils.Events.Companion.BOOK_CLICKED
import com.fiction.me.utils.Events.Companion.BOOK_ID_PROPERTY
import com.fiction.me.utils.Events.Companion.BOOK_SUMMARY
import com.fiction.me.utils.Events.Companion.BOOK_SUMMARY_SCREEN
import com.fiction.me.utils.Events.Companion.BOOK_SUMMARY_SHOWN
import com.fiction.me.utils.Events.Companion.CHAPTERS_AUTO_UNLOCK_SWITCHED
import com.fiction.me.utils.Events.Companion.CHAPTER_NUMBER
import com.fiction.me.utils.Events.Companion.LIKE_CLICKED
import com.fiction.me.utils.Events.Companion.OFF
import com.fiction.me.utils.Events.Companion.ON
import com.fiction.me.utils.Events.Companion.PLACEMENT
import com.fiction.me.utils.Events.Companion.READ_NOW_CLICKED
import com.fiction.me.utils.Events.Companion.REFERRER
import com.fiction.me.utils.Events.Companion.SECTION
import com.fiction.me.utils.Events.Companion.SEE_ALL_CLICKED
import com.fiction.me.utils.Events.Companion.SHOW_MORE_CLICKED
import com.fiction.me.utils.Events.Companion.STATE
import com.fiction.me.utils.Events.Companion.TAG_CLICKED
import com.fiction.me.utils.Events.Companion.TAG_NAME
import com.fiction.me.utils.Events.Companion.UNLOCK_NOW_CLICKED
import com.fiction.me.utils.Events.Companion.UNLOCK_NOW_SCREEN_SHOWN
import com.fiction.me.utils.Events.Companion.YOU_MAY_ALSO_LIKE
import com.fiction.me.utils.ModelType
import com.fiction.me.utils.cacheImages
import com.fiction.me.utils.setResizableText
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookSummaryFragment : FragmentBaseNCMVVM<BookSummaryViewModel, FragmentBookSummaryBinding>() {
    override val binding: FragmentBookSummaryBinding by viewBinding()
    override val viewModel: BookSummaryViewModel by viewModel()
    private val args: BookSummaryFragmentArgs by navArgs()
    private val autoUnlockBottomDialog = AutoUnlockBottomDialog()
    private var btnDialog = ChapterListBottomDialog()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.run {
            setBookSummaryId(args.itemId)
            getInfoById(args.isFromPush)
            getAlsoLikeBooks(args.itemId)
        }

        args.bookInfo?.let {
            viewModel.run {
                likeCount = it.likes
                isAddedToLib = it.isAddedInLib
                isBookLiked = it.isLike
            }
            setBookInfo(it)
        } ?: args.bookInfoJson.let {
            if (it.isNotEmpty()) {
                val bookInfo = it.fromGson(ModelType.BOOK_INFO) as BookInfo
                viewModel.run {
                    likeCount = bookInfo.likes
                    isAddedToLib = bookInfo.isAddedInLib
                    isBookLiked = bookInfo.isLike
                }
                setBookInfo(bookInfo)
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(this) {
            if (args.isOpeningFromOnBoarding) {
                val directions =
                    BookSummaryFragmentDirections.actionBookSummaryFragmentToExploreFragment()
                navigateFragment(directions)
            } else {
                popBackStack()
            }
        }
    }

    override fun onView() {
        viewModel.isShowProgressBar.value = false

        viewModel.run {
            getReadingMode()
            getPopularTagsList()
            cacheTariffs()
        }
        binding.suggestedBooksRV.setTitle(resources.getString(R.string.you_may_also_like))
        getFragmentResult()
        binding.container.post {
            binding.container.scrollTo(0, 0)
        }
        binding.progressDialog.setContent {
            ProgressDialog(viewModel.progressState)
        }
    }

    override fun onEach() {
        onEach(viewModel.bookInfo) { bookInfo ->
            viewModel.getReadingMode()
            bookInfo?.let {
                it.bookData.id.let {
                    viewModel.getAlsoLikeBooks(it)
                }
                setBookInfo(it.bookInfo)
                context?.let { it1 ->
                    cacheImages(it1, it.bookData.cover)
                }
            }
            binding.container.scrollTo(0, 0)
            binding.appBarLayout.setExpanded(true)
        }

        onEach(viewModel.alsoLikeBooks) { bookList ->
            bookList.let { binding.suggestedBooksRV.submitData(lifecycle, it) }
        }
        onEach(viewModel.updateInfo) { updateInfo ->
            with(binding) {
                if (updateInfo?.bookInfo?.isLike == true) {
                    setFavoriteState(getString(R.string.liked), R.drawable.ic_fill_favorite)
                } else setFavoriteState(getString(R.string.like), R.drawable.ic_favorite)

                if (updateInfo?.bookInfo?.isAddedInLib == true) {
                    setAddedBookInLibState(
                        getString(R.string.added),
                        R.drawable.ic_added_to_library
                    )
                } else setAddedBookInLibState(getString(R.string.add), R.drawable.ic_add_to_library)

                chapters.text = resources.getString(R.string.chapter)
                    .format(updateInfo?.bookInfo?.chaptersCount)
                quantity.text = if (updateInfo?.bookInfo?.isFinished == true)
                    resources.getString(R.string.genre_novel_is_finished).format(updateInfo.bookInfo.genres)
                else resources.getString(R.string.genre_novel_is_not_finished).format(updateInfo?.bookInfo?.genres)
                views.text = updateInfo?.bookInfo?.views?.toFormatViewsLikes()
                likes.text = updateInfo?.bookInfo?.likes?.toFormatViewsLikes()

                //resources.getString(R.string.views_likes, updateInfo?.bookInfo?.views)
                //likes.text = resources.getString(R.string.views_likes,(updateInfo?.bookInfo?.likes))
            }
            //bookList?.let { binding.suggestedBooksRV.submitList(it) }
        }
        onEach(viewModel.popularTagsList) { tags ->
            tags?.let {
                binding.popularTagsView.submitList(it)
            }
            binding.popularTagsView.setTitle(resources.getString(R.string.popular_tags))
        }
        onEach(viewModel.readBook) {
            val isUpdateBooks = viewModel.readingStatus() == false
            BookSummaryFragmentDirections.actionBookSummaryFragmentToReaderFragment(
                bookId = viewModel.bookSummaryInfo?.bookData?.id ?: 0,
                chapterId = -1,
                isUpdateCurrentBooks = isUpdateBooks,
                isCompleted = viewModel.getIsBookCompleted(),
                colorTheme = viewModel.theme
            ).run {
                navigateFragment(this)
            }
        }
        onEach(viewModel.addBookErrorMessage) {
            with(binding) {
                customSnackBar.startHeaderTextAnimation(it)
                viewModel.isAddedToLib = !viewModel.isAddedToLib
                addToLibrary.setImageResource(
                    if (viewModel.isAddedToLib)
                        R.drawable.ic_added_to_library
                    else R.drawable.ic_add_to_library
                )
            }
        }

        onEach(viewModel.likeErrorMessage) {
            with(binding) {
                customSnackBar.startHeaderTextAnimation(it)
                if (viewModel.isBookLiked) viewModel.likeCount-- else viewModel.likeCount++
                likes.text = viewModel.likeCount.toFormatViewsLikes()
                viewModel.isBookLiked = !viewModel.isBookLiked
                favorite.setImageResource(if (viewModel.isBookLiked) R.drawable.ic_fill_favorite else R.drawable.ic_favorite)
            }
        }

        onEach(viewModel.purchasedChapter) { chapterId ->
            val directions =
                BookSummaryFragmentDirections.actionBookSummaryFragmentToReaderFragment(
                    chapterId = chapterId,
                    bookId = viewModel.bookSummaryInfo?.bookData?.id ?: 0,
                    isCompleted = viewModel.getIsBookCompleted(),
                    colorTheme = viewModel.theme
                )
            navigateFragment(directions)
        }
        onEach(viewModel.unlockChapter) { chapterId ->
            binding.readNowBookContainer.isVisible = false
            viewModel.unlockSwitchState.value = viewModel.isTurnOnUnlock
            binding.bottomSheet.setContent {
                viewModel.run {
//                    sendUnlockEvent(UNLOCK_NOW_SCREEN_SHOWN)
                    showUnlockDialog()
                    ChapterPurchaseBottomDialog(
                        myBalance,
                        isTurnOnUnlock,
                        isUnlockBottomDialogShown,
                        getChapterCost(chapterId),
                        {
                            sendUnlockEvent(UNLOCK_NOW_SCREEN_SHOWN)
                        },
                        {
                            sendUnlockEvent(UNLOCK_NOW_CLICKED)
                            buyChapterOrCoin()
                        }, {
                            val state = if (it) ON else OFF
                            trackEvents(
                                CHAPTERS_AUTO_UNLOCK_SWITCHED, hashMapOf(
                                    STATE to state
                                )
                            )
                            setUserPropertyEvent(
                                mapOf(
                                    USER_PROPERTY__CHAPTERS_AUTO_UNLOCK to state
                                )
                            )
                            isTurnOnUnlock = it
                            unlockSwitchState.value = it
                            changeAutoUnlockMode(it)
                        }, {
                            hideUnlockDialog()
                            binding.readNowBookContainer.isVisible = true
                            if (isChangedUnlockState && !openCoinShop) {
                                openLockedStateDialog()
                            }
                            openCoinShop = false
                        },
                        unlockSwitchState,
                        isShowProgressBar
                    )
                }
            }
        }

        onEach(viewModel.isTurnOnAutoUnlock) {
            viewModel.hideUnlockDialog()
            val directions =
                BookSummaryFragmentDirections.actionBookSummaryFragmentToReaderFragment(
                    chapterId = viewModel.nextReadChapterId,
                    bookId = viewModel.bookSummaryInfo?.bookData?.id ?: 0,
                    isCompleted = viewModel.getIsBookCompleted(),
                    colorTheme = viewModel.theme
                )
            if (it == false) {
                autoUnlockBottomDialog.show(
                    childFragmentManager,
                    AutoUnlockBottomDialog::class.java.simpleName
                )
                autoUnlockBottomDialog.onAutoUnlockListener = {
                    navigateFragment(directions)
                }
            } else navigateFragment(directions)
        }

        onEach(viewModel.buyCoins) { chapterId ->
            val directions =
                BookSummaryFragmentDirections.actionBookSummaryFragmentToGetCoinFromStoreFragment(
                    bookName = viewModel.bookSummaryInfo?.bookData?.title,
                    bookId = viewModel.bookSummaryInfo?.bookData?.id ?: 0,
                    chapterId = chapterId ?: 0
                )
            viewModel.openCoinShop = true
            navigateFragment(directions)
        }

        onEach(viewModel.dismissDialog) {
            btnDialog.dismissDialog()
        }
    }

    override fun onViewClick() {
        super.onViewClick()
        with(binding) {
            toolBar.setOnClickListener {
                if (args.isOpeningFromOnBoarding) {
                    val directions =
                        BookSummaryFragmentDirections.actionBookSummaryFragmentToExploreFragment()
                    navigateFragment(directions)
                } else {
                    popBackStack()
                }
            }
            favorite.setOnClickListener {
                viewModel.run {
                    changeBookLikeState()
                    favorite.setImageResource(if (viewModel.isBookLiked) R.drawable.ic_fill_favorite else R.drawable.ic_favorite)
                    if (viewModel.isBookLiked) likeCount++ else likeCount--
                    likes.text = viewModel.likeCount.toFormatViewsLikes()
                    val bookId = viewModel.bookSummaryBookId
                    trackEvents(
                        LIKE_CLICKED,
                        hashMapOf(
                            BOOK_ID_PROPERTY to bookId
                        )
                    )
                }
            }
            addToLibrary.setOnClickListener {
                viewModel.addOrRemoveBook(viewModel.bookSummaryBookId, args.section)
                addToLibrary.setImageResource(
                    if (viewModel.isAddedToLib)
                        R.drawable.ic_added_to_library
                    else R.drawable.ic_add_to_library
                )
            }
            popularTagsView.setOnSeeAllClickListener {
                val directions =
                    BookSummaryFragmentDirections.actionBookSummaryFragmentToFilterByPopularTagsFragment()
                navigateFragment(directions)
            }
            chapters.setOnClickListener {
                btnDialog = ChapterListBottomDialog()
                btnDialog.onItemClickListener = { id ->
                    viewModel.progressState.value = true
                    viewModel.getChapterInfo(id)
                }
                val arg = Bundle().apply {
                    putSerializable(
                        ARGUMENT_FOR_OPENING_CHAPTER_DIALOG,
                        OpenedFromWhere.FROM_BOOK_SUMMERY
                    )
                    putParcelable(READING_PROGRESS, viewModel.bookSummaryInfo?.readingProgress)
                    putLong(
                        BOOK_ID_FOR_CHAPTER_FROM_BS,
                        viewModel.bookSummaryInfo?.bookData?.id ?: -1L
                    )
                    putString(
                        ReaderFragment.BOOK_NAME,
                        viewModel.bookSummaryInfo?.bookData?.title ?: ""
                    )
                }
                btnDialog.arguments = arg
                btnDialog.show(
                    childFragmentManager,
                    ChapterListBottomDialog::class.java.simpleName
                )
            }
            suggestedBooksRV.onSuggestedBooksItemClick = { suggestedBookId, isAddedLibrary ->
                viewModel.updateSuggestedBooksList(suggestedBookId, isAddedLibrary, args.section)
            }
            suggestedBooksRV.navigateClickListener = { id, title ->
                viewModel.trackEvents(
                    BOOK_CLICKED,
                    hashMapOf(BOOK_ID_PROPERTY to id, PLACEMENT to BOOK_SUMMARY)
                )
                viewModel.bookSummaryBookId = id
                viewModel.getInfoById()
            }
            suggestedBooksRV.onSeeAllClickListener = {
                viewModel.trackEvents(
                    SEE_ALL_CLICKED, hashMapOf(
                        PLACEMENT to BOOK_SUMMARY_SCREEN,
                        SECTION to YOU_MAY_ALSO_LIKE
                    )
                )
                val directions =
                    BookSummaryFragmentDirections.actionBookSummaryFragmentToYouMayAlsoLikeBooksWithPagingFragment(
                        viewModel.bookSummaryBookId
                    )
                navigateFragment(directions)
            }
            popularTagsView.seeAllClickListener = {
                val directions =
                    BookSummaryFragmentDirections.actionBookSummaryFragmentToFilterByPopularTagsFragment()
                navigateFragment(directions)
            }
            popularTagsView.itemCLick = { tagId, title ->
                viewModel.trackEvents(
                    TAG_CLICKED,
                    hashMapOf(TAG_NAME to title, PLACEMENT to BOOK_SUMMARY)
                )
                val directions =
                    BookSummaryFragmentDirections.actionBookSummaryFragmentToBooksByTagFragment(
                        tagId
                    )
                navigateFragment(directions)
            }
            readNow.setOnClickListener {
                val bookId = viewModel.bookSummaryBookId
                viewModel.trackEvents(READ_NOW_CLICKED, hashMapOf(BOOK_ID_PROPERTY to bookId))
                openDialog()
            }
        }
    }

    private fun openDialog() {
        val ageRestriction = viewModel.bookSummaryInfo?.bookData?.ageRestriction
        if (ageRestriction == BOOKS_CONTENT_18_PLUS && viewModel.isTeenager) {
            showDialogForTeenager()
        } else {
            allowRead()
        }
    }

    private fun allowRead() {
        context?.let {
            with(binding) {
                readNow.setTextColor(
                    ContextCompat.getColor(
                        it,
                        R.color.secondary_purple_dark_600
                    )
                )
                readNow.text = null
                progressCircular.isVisible = true
            }
            viewModel.startReadBook()
        }
    }

    private fun showDialogForTeenager() {
        val mainDialogFragment = MainDialogFragment()
        val args = Bundle()
        args.putString(
            GetMoreCoinsDialogFragment.TITLE,
            resources.getString(R.string.this_story_contains_mature_content)
        )
        args.putString(
            GetMoreCoinsDialogFragment.DESCRIPTION,
            resources.getString(R.string.would_you_like_to_continue_view_story)
        )
        mainDialogFragment.arguments = args
        mainDialogFragment.setPositiveButtonClickListener = {
            allowRead()
        }

        mainDialogFragment.show(
            childFragmentManager,
            MainDialogFragment::class.java.simpleName
        )
        mainDialogFragment.isNegativeBtnVisible = true
        mainDialogFragment.positiveBtnTxt = resources.getString(R.string.yes_view_the_story)
    }

    private fun setBookInfo(bookInfo: BookInfo) {
        with(binding) {
            context?.let {
                cacheImages(it, bookInfo.cover)
                cacheImages(it, bookInfo.authorAvatar)

                Glide.with(it)
                    .load(bookInfo.cover)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .placeholder(R.drawable.blured_pic)
                    .into(mainImage)

                Glide.with(it)
                    .load(bookInfo.authorAvatar)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .placeholder(R.drawable.ic_user_badge_xs)
                    .circleCrop()
                    .into(userImage)
                views.text = bookInfo.views.toFormatViewsLikes()
                likes.text = bookInfo.likes.toFormatViewsLikes()
            }
            val decryptionText = bookInfo.description
            description.setResizableText(
                decryptionText,
                MAX_LINES, true, collapseText = true,
                onClick = {
                    viewModel.trackEvents(
                        SHOW_MORE_CLICKED,
                        hashMapOf(
                            REFERRER to BOOK_SUMMARY_SCREEN,
                            BOOK_ID_PROPERTY to viewModel.bookSummaryBookId
                        )
                    )
                }
            )

            bookTitle.text = bookInfo.title
            authorName.text = bookInfo.authorName
            chapters.text = resources.getString(R.string.chapter).format(bookInfo.chaptersCount)
            if (bookInfo.genres.isNotEmpty()) {
                quantity.text = if (bookInfo.isFinished)
                    resources.getString(R.string.genre_novel_is_finished).format(bookInfo.genres)
                else resources.getString(R.string.genre_novel_is_not_finished).format(bookInfo.genres)
            }
            readNow.text =
                if (bookInfo.readStatus) context?.resources?.getString(R.string.continue_reading) else context?.resources?.getString(
                    R.string.read_now
                )

            if (bookInfo.isLike) {
                setFavoriteState(getString(R.string.liked), R.drawable.ic_fill_favorite)
            } else setFavoriteState(getString(R.string.like), R.drawable.ic_favorite)

            if (bookInfo.isAddedInLib) {
                setAddedBookInLibState(
                    getString(R.string.added),
                    R.drawable.ic_added_to_library
                )
            } else setAddedBookInLibState(getString(R.string.add), R.drawable.ic_add_to_library)
        }
    }

    private fun setFavoriteState(text: String, favoriteDrawable: Int) {
        with(binding) {
            favorite.setImageResource(favoriteDrawable)
        }
    }

    private fun setAddedBookInLibState(text: String, addDrawable: Int) {
        with(binding) {
            addToLibrary.setImageResource(addDrawable)
        }
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

    private fun sendUnlockEvent(eventName: String) {
        val bookChapter = viewModel.getChapterOrder(viewModel.nextReadChapterId)
        viewModel.trackEvents(
            eventName,
            hashMapOf(
                CHAPTER_NUMBER to bookChapter,
                BOOK_ID_PROPERTY to (viewModel.bookSummaryInfo?.bookData?.id ?: -1)
            ),
            eventsType = Analytics.EventsType.ALL
        )
    }

    private fun getFragmentResult() {
        activity?.supportFragmentManager?.setFragmentResultListener(
            COIN_SHOP,
            viewLifecycleOwner
        ) { _, result ->
            val isClosed = result.getBoolean(IS_CLOSED)
            if (isClosed && viewModel.isChangedUnlockState) {
                openLockedStateDialog()
                Log.i("composeDialog", "onView: ")
            }
        }
    }

    companion object {
        const val READING_PROGRESS = "READING_PROGRESS"
        const val BOOK_ID_FOR_CHAPTER_FROM_BS = "BOOK_ID_FOR_CHAPTER_FROM_BS"
        const val BOOK_ADD_REMOVE_STATE = "bookAddRemoveState"
        const val BOOK_ID = "bookId"
        const val ID_ADDED_TO_LIB = "isAddedToLib"
    }
}