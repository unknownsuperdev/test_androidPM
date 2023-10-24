package com.name.jat.ui.fragments.explore.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.name.domain.model.GenreDataModel
import com.name.jat.R
import com.name.jat.appbase.adapter.BaseAdapter
import com.name.jat.appbase.adapter.BaseViewHolder
import com.name.jat.databinding.ItemGenreBinding
import com.name.jat.extensions.dpToPx

class GenresItemsAdapter :
    BaseAdapter<ViewBinding, GenreDataModel, BaseViewHolder<GenreDataModel, ViewBinding>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<GenreDataModel, ViewBinding> {
        return GenreItemViewHolder(
            ItemGenreBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    private class GenreItemViewHolder(private val binding: ItemGenreBinding) :
        BaseViewHolder<GenreDataModel, ViewBinding>(binding) {

        override fun bind(item: GenreDataModel, context: Context) {
            with(binding) {
                title.text = item.title
                if(item.icon.isNotEmpty()) {
                    val url = GlideUrl(
                        item.icon, LazyHeaders.Builder()
                            .addHeader("User-Agent", "your-user-agent")
                            .build()
                    )
                    Glide.with(context)
                        .load(url)
                        .into(iconCount)
                }
            }
        }
    }
}