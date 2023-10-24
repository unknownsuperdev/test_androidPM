package com.fiction.me.ui.fragments.explore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding
import com.fiction.domain.model.*
import com.fiction.me.appbase.adapter.BaseAdapter
import com.fiction.me.appbase.adapter.BaseViewHolder
import com.fiction.me.databinding.*
import com.fiction.me.ui.fragments.explore.ExploreFragmentDirections
import com.fiction.me.utils.Events.Companion.BOOK_CLICKED
import com.fiction.me.utils.Events.Companion.BOOK_ID_PROPERTY
import com.fiction.me.utils.Events.Companion.GENRE_CLICKED
import com.fiction.me.utils.Events.Companion.GENRE_NAME
import com.fiction.me.utils.Events.Companion.TAG_CLICKED
import com.fiction.me.utils.Events.Companion.TAG_NAME

class ExploreListAdapter(
    private var onSuggestedBookItemClick: (dataModelId: String, suggestedBookId: Long, isAddedLibrary: Boolean, pos: Int, offset: Int) -> Unit,
    private var onStoryItemAddedLibraryClick: (dataModelId: String, storyId: Long, isAddedLibrary: Boolean, pos: Int, offset: Int) -> Unit,
    private var onItemClick: (directions: NavDirections, section: String?, isClickInSeeAll: Boolean, bookTitle: String?, bookId: Long?) -> Unit,
    private var sendEventListener: (event: String, eventProperty: Map<String, Any>) -> Unit = { _, _ -> }
) : BaseAdapter<ViewBinding, BaseExploreDataModel, BaseViewHolder<BaseExploreDataModel, ViewBinding>>() {

    private val TYPE_STORY = 0
    private val TYPE_SUGGESTED_BOOKS = 1
    private val TYPE_GENRES = 2
    private val TYPE_BESTSELLERS = 3
    private val TYPE_POPULAR_TAGS = 4


    private var firstStoryItem = 0
    private var storyLeftOffset = 0
    private var firstItem: Int = 0
    private var leftOffset = 0
    private var storyItemsAdapter: StoryItemsAdapter? = null
    private var genresItemsAdapter: GenresItemsAdapter? = null
    private var popularTagsItemsAdapter: PopularTagsItemsAdapter? = null
    private var bestsellersItemsAdapter: BestsellersItemsAdapter? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<BaseExploreDataModel, ViewBinding> {

        return when (viewType) {
            TYPE_STORY -> StoryListViewHolder(
                ItemStoryListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                storyItemsAdapter,
                onStoryItemAddedLibraryClick,
                sendEventListener,
                onItemClick,
                firstStoryItem,
                storyLeftOffset

            ) as BaseViewHolder<BaseExploreDataModel, ViewBinding>

            TYPE_SUGGESTED_BOOKS -> SuggestedBooksListViewHolder(
                ItemSuggestedBooksListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onSuggestedBookItemClick,
                sendEventListener,
                onItemClick,
                firstItem,
                leftOffset

            ) as BaseViewHolder<BaseExploreDataModel, ViewBinding>

            TYPE_GENRES -> GenreListViewHolder(
                ItemGenresListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                genresItemsAdapter,
                onItemClick,
                sendEventListener
            ) as BaseViewHolder<BaseExploreDataModel, ViewBinding>

            TYPE_POPULAR_TAGS -> PopularTagsListViewHolder(
                ItemPopularTagsListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                popularTagsItemsAdapter, onItemClick, sendEventListener
            ) as BaseViewHolder<BaseExploreDataModel, ViewBinding>

            else -> BestsellersListViewHolder(
                ItemBestsellersListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                bestsellersItemsAdapter,
                sendEventListener,
                onItemClick
            ) as BaseViewHolder<BaseExploreDataModel, ViewBinding>
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SuggestedBooksModel -> TYPE_SUGGESTED_BOOKS
            is StoryModel -> TYPE_STORY
            is GenreModel -> TYPE_GENRES
            is PopularTagsModel -> TYPE_POPULAR_TAGS
            else -> TYPE_BESTSELLERS
        }
    }

    private class StoryListViewHolder(
        private val binding: ItemStoryListBinding,
        private var storyItemsAdapter: StoryItemsAdapter?,
        private var onStoryItemAddedLibraryClick: (dataModelId: String, storyId: Long, isAddedLibrary: Boolean, pos: Int, offset: Int) -> Unit,
        private var sendEventListener: (event: String, eventProperty: Map<String, Any>) -> Unit,
        private var onItemClick: (directions: NavDirections, section: String?, isClickInSeeAll: Boolean, bookTitle: String?, bookId: Long?) -> Unit,
        private var firstStoryItem: Int,
        private var storyLeftOffset: Int
    ) : BaseViewHolder<StoryModel, ViewBinding>(binding) {
        init {
            storyItemsAdapter = StoryItemsAdapter({ parentId, storyId, isAddedLibrary ->
                onStoryItemAddedLibraryClick(
                    parentId,
                    storyId,
                    isAddedLibrary,
                    firstStoryItem,
                    storyLeftOffset
                )
            },
                { directions, title, id ->
                    sendEventListener(BOOK_CLICKED, mapOf(BOOK_ID_PROPERTY to (id ?: -1)))
                    val section = binding.title.text.toString()
                    onItemClick(directions, section, false, title, id)
                })
        }

        override fun bind(item: StoryModel, context: Context) {
            with(binding) {
                title.text = item.title
                storyRV.apply {
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = storyItemsAdapter

                    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
                            firstStoryItem =
                                (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                            val firstItemView: View? =
                                (layoutManager as LinearLayoutManager).findViewByPosition(
                                    firstStoryItem
                                )
                            storyLeftOffset = firstItemView?.left ?: 0
                        }
                    })
                    (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                        item.position,
                        item.offset
                    )
                    itemAnimator = null
                }
                seeAll.setOnClickListener {
                    val directions =
                        ExploreFragmentDirections.actionExploreFragmentToSuggestBookSeeAllFragment(
                            item.type
                        )
                    onItemClick(directions, item.title, true, null, null)
                }
            }
            storyItemsAdapter?.submitList(item.storyList)
        }
    }

    private class SuggestedBooksListViewHolder(
        private val binding: ItemSuggestedBooksListBinding,
        private val onSuggestedBookItemClick: (dataModelId: String, suggestedBookId: Long, isAddedLibrary: Boolean, pos: Int, offset: Int) -> Unit,
        private var sendEventListener: (event: String, eventProperty: Map<String, Any>) -> Unit,
        private var onItemClick: (directions: NavDirections, section: String?, isClickInSeeAll: Boolean, bookTitle: String?, bookId: Long?) -> Unit,
        private var firstItem: Int,
        private var leftOffset: Int
    ) : BaseViewHolder<SuggestedBooksModel, ViewBinding>(binding) {

        override fun bind(item: SuggestedBooksModel, context: Context) {
            val suggestedBooksItemsAdapter =
                SuggestedBooksItemsAdapter({ suggestedBookId, isAddedLibrary ->
                    onSuggestedBookItemClick(
                        item.id,
                        suggestedBookId,
                        isAddedLibrary,
                        firstItem,
                        leftOffset
                    )
                },
                    { id, directions, title, bookId ->
                        sendEventListener(BOOK_CLICKED, hashMapOf<String, Any>(BOOK_ID_PROPERTY to (bookId ?: -1)))
                        val section = binding.title.text.toString()
                        onItemClick(directions, section, false, title, bookId)
                    }
                )
            with(binding) {
                title.text = item.title
                suggestedBooksRV.apply {
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = suggestedBooksItemsAdapter

                    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
                            firstItem =
                                (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                            val firstItemView: View? =
                                (layoutManager as LinearLayoutManager).findViewByPosition(
                                    firstItem
                                )
                            leftOffset = firstItemView?.left ?: 0
                        }
                    })
                    (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                        item.position,
                        item.offset
                    )
                    itemAnimator = null
                }
                seeAll.setOnClickListener {
                    val directions =
                        ExploreFragmentDirections.actionExploreFragmentToSuggestBookSeeAllFragment(
                            item.type,
                            item.genreId ?: -1
                        )
                    onItemClick(directions, item.title, true, null, null)
                }
            }
            suggestedBooksItemsAdapter.submitList(item.booksList)
        }
    }

    private class GenreListViewHolder(
        private val binding: ItemGenresListBinding,
        private var genresItemsAdapter: GenresItemsAdapter?,
        private var onItemClick: (directions: NavDirections, section: String?, isClickInSeeAll: Boolean, bookTitle: String?, bookId: Long?) -> Unit,
        private var sendEventListener: (event: String, eventProperty: Map<String, Any>) -> Unit
    ) : BaseViewHolder<GenreModel, ViewBinding>(binding) {

        init {
            genresItemsAdapter = GenresItemsAdapter({
                onItemClick(it, null, false, null, null)
            }, { title ->
                sendEventListener(GENRE_CLICKED, hashMapOf<String, Any>(GENRE_NAME to title))
            })
        }

        override fun bind(item: GenreModel, context: Context) {
            with(binding) {
                title.text = item.title
                genreRV.apply {
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = genresItemsAdapter
                }
                seeAll.setOnClickListener {
                    val directions =
                        ExploreFragmentDirections.actionExploreFragmentToAllGenresFragment()
                    onItemClick(directions, item.title, true, null, null)
                }
            }
            genresItemsAdapter?.submitList(item.genreList)
        }
    }

    private class PopularTagsListViewHolder(
        private val binding: ItemPopularTagsListBinding,
        private var popularTagsItemsAdapter: PopularTagsItemsAdapter?,
        private var onItemClick: (directions: NavDirections, section: String?, isClickInSeeAll: Boolean, bookTitle: String?, bookId: Long?) -> Unit,
        private var sendEventListener: (event: String, eventProperty: Map<String, Any>) -> Unit
    ) : BaseViewHolder<PopularTagsModel, ViewBinding>(binding) {

        init {
            popularTagsItemsAdapter = PopularTagsItemsAdapter({
                onItemClick(it, null, false, null, null)
            }, { id, title ->
                sendEventListener(TAG_CLICKED, hashMapOf<String, Any>(TAG_NAME to title))
            })
        }

        override fun bind(item: PopularTagsModel, context: Context) {
            with(binding) {
                title.text = item.title
                popularTagsRV.apply {
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

                    adapter = popularTagsItemsAdapter
                }
                seeAll.setOnClickListener {
                    val directions =
                        ExploreFragmentDirections.actionExploreFragmentToFilterByPopularTagsFragment()
                    onItemClick(directions, item.title, true, null, null)
                }
            }
            popularTagsItemsAdapter?.submitList(item.popularTagsList)
        }
    }

    private class BestsellersListViewHolder(
        private val binding: ItemBestsellersListBinding,
        private var bestsellersItemsAdapter: BestsellersItemsAdapter?,
        private var sendEventListener: (event: String, eventProperty: Map<String, Any>) -> Unit,
        private var onItemClick: (directions: NavDirections, section: String?, isClickInSeeAll: Boolean, bookTitle: String?, bookId: Long?) -> Unit,
    ) : BaseViewHolder<BestsellersModel, ViewBinding>(binding) {
        init {
            bestsellersItemsAdapter = BestsellersItemsAdapter { directions, title, id ->
                sendEventListener(BOOK_CLICKED, hashMapOf<String, Any>(BOOK_ID_PROPERTY to (id ?: -1)))
                val section = binding.title.text.toString()
                onItemClick(directions, section, false, title, id)
            }
        }

        override fun bind(item: BestsellersModel, context: Context) {
            val columns = 2
            with(binding) {
                title.text = item.title
                bestsellersRV.apply {
                    layoutManager =
                        StaggeredGridLayoutManager(columns, LinearLayoutManager.HORIZONTAL)
                    adapter = bestsellersItemsAdapter
                }
                seeAll.setOnClickListener {
                    val directions =
                        ExploreFragmentDirections.actionExploreFragmentToSuggestBookSeeAllFragment(
                            item.type,
                            item.genreId ?: -1
                        )
                    onItemClick(directions, item.title, true, null, null)
                }
            }
            bestsellersItemsAdapter?.submitList(item.bestsellersList)
        }
    }
}

