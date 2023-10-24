package com.name.jat.ui.fragments.onboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.name.domain.model.FavoriteThemeData
import com.name.jat.appbase.adapter.BaseAdapter
import com.name.jat.appbase.adapter.BaseViewHolder
import com.name.jat.databinding.ItemFavoriteThemeBinding

class FavoriteThemeAdapter(
    private val onTagItemClick: (id: Long, isSelected: Boolean) -> Unit
) : BaseAdapter<ViewBinding, FavoriteThemeData, BaseViewHolder<FavoriteThemeData, ViewBinding>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<FavoriteThemeData, ViewBinding> {
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
        private val onTagItemClick: (id: Long, isSelected: Boolean) -> Unit
    ) : BaseViewHolder<FavoriteThemeData, ViewBinding>(binding) {

        override fun bind(item: FavoriteThemeData, context: Context) {
            with(binding) {
                title.text = item.title
                title.isSelected = item.isSelected
                theme.isSelected = item.isSelected
            }
        }

        override fun onItemClick(item: FavoriteThemeData) {
            super.onItemClick(item)
            onTagItemClick(item.id, !item.isSelected)
        }
    }
}