package com.name.jat.ui.fragments.reader.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.name.domain.model.BookSettingsData
import com.name.domain.model.enums.ColorThemeData
import com.name.domain.model.enums.LineHeightData
import com.name.domain.model.enums.TextTypeData
import com.name.jat.R
import com.name.jat.databinding.ItemFlipPageBinding

class FlipPagerAdapter(
    private var pages: List<CharSequence>,
    private var bookSettingsData: BookSettingsData,
    private val onTouch: () -> Unit
) : RecyclerView.Adapter<PagerVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
        PagerVH(
            ItemFlipPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = pages.size

    override fun onBindViewHolder(holder: PagerVH, position: Int) = holder.itemView.run {
        holder.bind(pages[position], bookSettingsData)
        holder.itemView.setOnClickListener {
            onTouch()
        }
    }
}

class PagerVH(
    private val binding: ItemFlipPageBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: CharSequence, bookSettingsData: BookSettingsData) {
        val defaultLineSpace = 9f
        val biggerLineSpace = 13f
        with(binding) {
            message.textSize = bookSettingsData.textSize.toFloat()

            when (bookSettingsData.colorTheme) {
                ColorThemeData.BLACK -> message.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.white
                    )
                )
                ColorThemeData.SEPIA -> message.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.black_100
                    )
                )
                else -> message.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.black_100
                    )
                )
            }

            when (bookSettingsData.lineHeight) {
                LineHeightData.DEFAULT -> message.setLineSpacing(defaultLineSpace, 1.0f)
                else -> message.setLineSpacing(biggerLineSpace, 1.0f)
            }

            message.typeface = when (bookSettingsData.textType) {
                TextTypeData.PT_SERIF -> ResourcesCompat.getFont(
                    binding.root.context,
                    R.font.ptserif_regular
                )
                else -> ResourcesCompat.getFont(
                    binding.root.context,
                    R.font.roboto_regular_400
                )
            }
            message.text = item
        }
    }
}
