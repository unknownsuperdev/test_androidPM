package com.name.jat.ui.fragments.explore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding
import com.name.domain.model.*
import com.name.jat.appbase.adapter.BaseAdapter
import com.name.jat.appbase.adapter.BaseViewHolder
import com.name.jat.databinding.*
import com.name.jat.ui.fragments.explore.ExploreFragmentDirections


class ExploreListAdapter(
    private var onSuggestedBookItemClick: (dataModelId: String, suggestedBookId: Long, isAddedLibrary: Boolean, pos: Int, offset: Int) -> Unit,
    private var onStoryItemAddedLibraryClick: (dataModelId: String, storyId: Long, isAddedLibrary: Boolean, pos: Int, offset: Int) -> Unit,
    private var onItemClick: (directions: NavDirections) -> Unit,
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
                onItemClick
            ) as BaseViewHolder<BaseExploreDataModel, ViewBinding>

            TYPE_POPULAR_TAGS -> PopularTagsListViewHolder(
                ItemPopularTagsListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                popularTagsItemsAdapter, onItemClick
            ) as BaseViewHolder<BaseExploreDataModel, ViewBinding>
            TYPE_BESTSELLERS -> BestsellersListViewHolder(
                ItemBestsellersListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                bestsellersItemsAdapter,
                onItemClick
            ) as BaseViewHolder<BaseExploreDataModel, ViewBinding>

            else -> throw IllegalArgumentException("Invalid type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SuggestedBooksModel -> TYPE_SUGGESTED_BOOKS
            is StoryModel -> TYPE_STORY
            is GenreModel -> TYPE_GENRES
            is PopularTagsModel -> TYPE_POPULAR_TAGS
            is BestsellersModel -> TYPE_BESTSELLERS
            else -> -1
        }
    }

    private class StoryListViewHolder(
        private val binding: ItemStoryListBinding,
        private var storyItemsAdapter: StoryItemsAdapter?,
        private var onStoryItemAddedLibraryClick: (dataModelId: String, storyId: Long, isAddedLibrary: Boolean, pos: Int, offset: Int) -> Unit,
        private var onItemClick: (directions: NavDirections) -> Unit,
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
                { directions ->
                    onItemClick(directions)
                })
        }

        override fun bind(item: StoryModel, context: Context) {
            with(binding) {
                title.text = item.title
                storyRV.apply {
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    setHasFixedSize(true)
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
                        ExploreFragmentDirections.actionExploreFragmentToSuggestBookSeeAllFragment()
                    onItemClick(directions)
                }
            }
            storyItemsAdapter?.submitList(item.storyList)
        }
    }

    private class SuggestedBooksListViewHolder(
        private val binding: ItemSuggestedBooksListBinding,
        private val onSuggestedBookItemClick: (dataModelId: String, suggestedBookId: Long, isAddedLibrary: Boolean, pos: Int, offset: Int) -> Unit,
        private var onItemClick: (directions: NavDirections) -> Unit,
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
                    { directions ->
                        onItemClick(directions)
                    }
                )
            with(binding) {
                title.text = item.title
                suggestedBooksRV.apply {
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    setHasFixedSize(true)
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
                }
                seeAll.setOnClickListener {
                    val directions =
                        ExploreFragmentDirections.actionExploreFragmentToSuggestBookSeeAllFragment()
                    onItemClick(directions)
                }
            }
            suggestedBooksItemsAdapter.submitList(item.booksList)
        }
    }

    private class GenreListViewHolder(
        private val binding: ItemGenresListBinding,
        private var genresItemsAdapter: GenresItemsAdapter?,
        private var onItemClick: (directions: NavDirections) -> Unit,
    ) : BaseViewHolder<GenreModel, ViewBinding>(binding) {

        init {
            genresItemsAdapter = GenresItemsAdapter()
        }

        override fun bind(item: GenreModel, context: Context) {
            with(binding) {
                title.text = item.title
                genreRV.apply {
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    setHasFixedSize(true)
                    adapter = genresItemsAdapter
                }
                seeAll.setOnClickListener {
                    val directions =
                        ExploreFragmentDirections.actionExploreFragmentToAllGenresFragment()
                    onItemClick(directions)
                }
            }
            genresItemsAdapter?.submitList(item.genreList)
        }
    }

    private class PopularTagsListViewHolder(
        private val binding: ItemPopularTagsListBinding,
        private var popularTagsItemsAdapter: PopularTagsItemsAdapter?,
        private var onItemClick: (directions: NavDirections) -> Unit,
    ) : BaseViewHolder<PopularTagsModel, ViewBinding>(binding) {

        init {
            popularTagsItemsAdapter = PopularTagsItemsAdapter {}
        }

        override fun bind(item: PopularTagsModel, context: Context) {
            with(binding) {
                title.text = item.title
                popularTagsRV.apply {
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    setHasFixedSize(true)

                    adapter = popularTagsItemsAdapter
                }
                seeAll.setOnClickListener {
                    val directions =
                        ExploreFragmentDirections.actionExploreFragmentToFilterByPopularTagsFragment()
                    onItemClick(directions)
                }
            }
            popularTagsItemsAdapter?.submitList(item.popularTagsList)
        }
    }

    private class BestsellersListViewHolder(
        private val binding: ItemBestsellersListBinding,
        private var bestsellersItemsAdapter: BestsellersItemsAdapter?,
        var onItemClick: (directions: NavDirections) -> Unit
    ) : BaseViewHolder<BestsellersModel, ViewBinding>(binding) {
        init {
            bestsellersItemsAdapter = BestsellersItemsAdapter { directions ->
                onItemClick(directions)
            }
        }

        override fun bind(item: BestsellersModel, context: Context) {
            val columns = 2
            with(binding) {
                title.text = item.title
                bestsellersRV.apply {
                    layoutManager =
                        StaggeredGridLayoutManager(columns, LinearLayoutManager.HORIZONTAL)
                    setHasFixedSize(true)
                    adapter = bestsellersItemsAdapter
                }
            }
            bestsellersItemsAdapter?.submitList(item.bestsellersList)
        }
    }
}

