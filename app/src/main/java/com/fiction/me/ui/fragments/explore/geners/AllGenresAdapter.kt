package com.fiction.me.ui.fragments.explore.geners

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.viewbinding.ViewBinding
import com.fiction.domain.model.GenreDataModel
import com.fiction.me.appbase.adapter.BaseAdapter
import com.fiction.me.appbase.adapter.BaseViewHolder
import com.fiction.me.databinding.ItemAllGenresBinding
import com.fiction.me.extensions.loadUrl

class AllGenresAdapter(
    private val onItemClick: (directions: NavDirections, title: String) -> Unit
) : BaseAdapter<ViewBinding, GenreDataModel, BaseViewHolder<GenreDataModel, ViewBinding>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<GenreDataModel, ViewBinding> {
        return GenreItemViewHolder(
            ItemAllGenresBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick
        )
    }

    private class GenreItemViewHolder(
        private val binding: ItemAllGenresBinding,
        private val onItemClick: (directions: NavDirections, title: String) -> Unit
    ) : BaseViewHolder<GenreDataModel, ViewBinding>(binding) {

        override fun bind(item: GenreDataModel, context: Context) {
            with(binding) {
                title.text = item.title
                genreIcon.loadUrl(item.icon.split("?")[0], context) // fixme hotfix
            }
        }

        override fun onItemClick(item: GenreDataModel) {
            val directions =
                AllGenresFragmentDirections.actionAllGenresFragmentToBooksByGenresFragment(item.id, item.title)
            onItemClick(directions, item.title)
        }
    }
}