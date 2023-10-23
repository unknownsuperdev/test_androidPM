package ru.tripster.guide.ui.fragments.home

import android.content.*
import android.view.*
import androidx.viewbinding.*
import ru.tripster.domain.model.*
import ru.tripster.guide.appbase.adapter.*
import ru.tripster.guide.databinding.*
import com.bumptech.glide.*

class DataInfoListAdapter :
    BaseAdapter<ViewBinding, DataInfo, BaseViewHolder<DataInfo, ViewBinding>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<DataInfo, ViewBinding> {
        return ItemViewHolder(
            ItemDataBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    private inner class ItemViewHolder(private val binding: ItemDataBinding) :
        BaseViewHolder<DataInfo, ViewBinding>(binding) {
        override fun bind(item: DataInfo, context: Context) {
            with(binding) {
                nameText.text = item.name
                Glide.with(context).load(item.url).into(image)
                widthText.text = item.width.toString()
                heightText.text = item.height.toString()
            }
        }
    }
}


