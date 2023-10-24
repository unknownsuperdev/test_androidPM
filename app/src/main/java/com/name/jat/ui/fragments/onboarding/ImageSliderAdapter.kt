package com.name.jat.ui.fragments.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.name.jat.R
import com.name.jat.databinding.ItemSlideImageBinding
import com.name.jat.extensions.dpToPx

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
        holder.itemView.setOnClickListener {
         //   onTouch()
        }
    }
}

class PagerVH(
    private val binding: ItemSlideImageBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: String) {
        val context = binding.root.context
        Glide.with(context)
            .load(R.drawable.book)
           // .transform(RoundedCorners(context.dpToPx(R.dimen.space_4)))
            .into(binding.imageSlide)
    }
}
