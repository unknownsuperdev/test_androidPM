package com.fiction.me.ui.fragments.purchase

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.viewbinding.ViewBinding
import coil.decode.SvgDecoder
import coil.load
import com.fiction.domain.model.TariffsItem
import com.fiction.me.R
import com.fiction.me.appbase.adapter.BaseAdapter
import com.fiction.me.appbase.adapter.BaseViewHolder
import com.fiction.me.databinding.ItemCoinOfStoreBinding

class TariffsAdapter(
    private val onItemClick: (id: Long) -> Unit
) : BaseAdapter<ViewBinding, TariffsItem, BaseViewHolder<TariffsItem, ViewBinding>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<TariffsItem, ViewBinding> {
        return TariffsItemViewHolder(
            ItemCoinOfStoreBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick
        )
    }

    private class TariffsItemViewHolder(
        private val binding: ItemCoinOfStoreBinding,
        private val onItemClick: (id: Long) -> Unit
    ) : BaseViewHolder<TariffsItem, ViewBinding>(binding) {

        @SuppressLint("ResourceType")
        override fun bind(item: TariffsItem, context: Context) {
            with(binding) {
                when (item.backGroundType) {
                    "primary" -> {
                        binding.root
                            .setBackgroundResource(R.drawable.selector_bg_colored_tariff_item)
                        coins.setTextColor(ContextCompat.getColor(context, R.color.white_400))
                    }
                    "default" -> {
                        binding.root
                            .setBackgroundResource(R.drawable.selector_bg_white_tariff_item)
                        coins.setTextColor(ContextCompat.getColor(context, R.color.black_100))
                    }
                }
                binding.root.isSelected = item.isSelected
                type.run {
                    if (item.type != "") {
                        val drawable =
                            AppCompatResources.getDrawable(context, R.drawable.bg_light_pink_r4)
                        if (drawable != null) {
                            val draw =
                                DrawableCompat.wrap(drawable)
                            draw.setTint(Color.parseColor(item.type))
                            type.background = draw
                        }
                    }
                    item.typeTxt.run {
                        if (isNotEmpty()) {
                            visibility = VISIBLE
                            text = resources.getString(R.string.free).format(item.typeTxt)
                        } else
                            visibility = INVISIBLE
                    }
                }
                coins.text = item.coinsCount
                save.run {
                    item.discountPercent.run {
                        if (isNotEmpty()) {
                            visibility = VISIBLE
                            text = this
                        } else {
                            visibility = INVISIBLE
                        }
                    }
                }
                buyBtn.text =
                    if (item.isPaid) item.price else context.resources.getString(R.string.free)
                coinsImage.load(item.coinPicture){
                    decoderFactory(SvgDecoder.Factory())
                }
            }
        }

        override fun onItemClick(item: TariffsItem) {
            onItemClick(item.id)
        }
    }
}