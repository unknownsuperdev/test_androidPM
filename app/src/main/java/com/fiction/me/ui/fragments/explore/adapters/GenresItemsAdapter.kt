package com.fiction.me.ui.fragments.explore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.viewbinding.ViewBinding
import com.fiction.domain.model.GenreDataModel
import com.fiction.me.appbase.adapter.BaseAdapter
import com.fiction.me.appbase.adapter.BaseViewHolder
import com.fiction.me.databinding.ItemGenreBinding
import com.fiction.me.extensions.loadUrl
import com.fiction.me.ui.fragments.explore.ExploreFragmentDirections

class GenresItemsAdapter(
    private val setOnItemClick: (directions: NavDirections) -> Unit,
    private val sendEventListener: (title: String) -> Unit,
) : BaseAdapter<ViewBinding, GenreDataModel, BaseViewHolder<GenreDataModel, ViewBinding>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<GenreDataModel, ViewBinding> {
        return GenreItemViewHolder(
            ItemGenreBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), setOnItemClick,
            sendEventListener
        )
    }

    private class GenreItemViewHolder(
        private val binding: ItemGenreBinding,
        private val setOnItemClick: (directions: NavDirections) -> Unit,
        private val sendEventListener: (title: String) -> Unit,
    ) : BaseViewHolder<GenreDataModel, ViewBinding>(binding) {

        override fun bind(item: GenreDataModel, context: Context) {
            with(binding) {
                title.text = item.title
                iconCount.loadUrl(item.icon.split("?")[0], context)/* { fixme hotfix
                    decoderFactory(SvgDecoder.Factory())
                }*/
            }
        }

        override fun onItemClick(item: GenreDataModel) {
            sendEventListener(item.title)
            val directions =
                ExploreFragmentDirections.actionExploreFragmentToBooksByGenresFragment(item.id, title = item.title)
            setOnItemClick(directions)
        }
    }
}