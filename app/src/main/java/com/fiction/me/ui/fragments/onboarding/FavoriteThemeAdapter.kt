package com.fiction.me.ui.fragments.onboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.fiction.domain.model.onboarding.FavoriteThemeTags
import com.fiction.me.appbase.adapter.BaseAdapter
import com.fiction.me.appbase.adapter.BaseViewHolder
import com.fiction.me.databinding.ItemFavoriteThemeBinding

class FavoriteThemeAdapter(
    private val onTagItemClick: (title: String, id: Long, isSelected: Boolean) -> Unit
) : BaseAdapter<ViewBinding, FavoriteThemeTags, BaseViewHolder<FavoriteThemeTags, ViewBinding>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<FavoriteThemeTags, ViewBinding> {
        return FavoriteThemeViewHolder(
            ItemFavoriteThemeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onTagItemClick
        )
    }

    private class FavoriteThemeViewHolder(
        private val binding: ItemFavoriteThemeBinding,
        private val onTagItemClick: (title: String, id: Long, isSelected: Boolean) -> Unit
    ) : BaseViewHolder<FavoriteThemeTags, ViewBinding>(binding) {

        override fun bind(item: FavoriteThemeTags, context: Context) {
            with(binding) {
                title.text = item.title
                title.isSelected = item.isSelected
                theme.isSelected = item.isSelected
            }
        }

        override fun onItemClick(item: FavoriteThemeTags) {
            onTagItemClick(item.title, item.id, !item.isSelected)
        }
    }
}