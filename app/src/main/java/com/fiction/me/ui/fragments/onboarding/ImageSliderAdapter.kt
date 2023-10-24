package com.fiction.me.ui.fragments.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.fiction.me.R
import com.fiction.me.databinding.ItemSlideImageBinding

class ImageSliderAdapter(
    private var images: List<String>
) : RecyclerView.Adapter<PagerVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
        PagerVH(
            ItemSlideImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: PagerVH, position: Int) = holder.itemView.run {
        holder.bind(images[position])
    }
}

class PagerVH(
    private val binding: ItemSlideImageBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: String) {
        Glide.with(binding.root.context)
            .load(item)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .placeholder(R.drawable.blured_pic)
            .into(binding.imageSlide)
    }
}
