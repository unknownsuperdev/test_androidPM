package com.name.jat.ui.fragments.explore.geners

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.name.domain.model.GenreDataModel
import com.name.jat.appbase.adapter.BaseAdapter
import com.name.jat.appbase.adapter.BaseViewHolder
import com.name.jat.databinding.ItemAllGenresBinding

class AllGenresAdapter(
    private val onItemClick: (directions: NavDirections) -> Unit
) :
    BaseAdapter<ViewBinding, GenreDataModel, BaseViewHolder<GenreDataModel, ViewBinding>>() {
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
        private val onItemClick: (directions: NavDirections) -> Unit
    ) : BaseViewHolder<GenreDataModel, ViewBinding>(binding) {

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
                        .into(genreIcon)
                }
            }
        }

        override fun onItemClick(item: GenreDataModel) {
            val directions =
                AllGenresFragmentDirections.actionAllGenresFragmentToBooksByGenresFragment(item.id)
            onItemClick(directions)
        }
    }
}