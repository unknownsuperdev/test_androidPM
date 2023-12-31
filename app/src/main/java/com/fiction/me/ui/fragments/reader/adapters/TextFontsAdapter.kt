package com.fiction.me.ui.fragments.reader.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.fiction.domain.model.TextFontTypesModel
import com.fiction.domain.model.enums.TextTypeData
import com.fiction.me.appbase.adapter.BaseAdapter
import com.fiction.me.appbase.adapter.BaseViewHolder
import com.fiction.me.databinding.ItemTextFontBinding
import com.fiction.me.utils.Constants.Companion.TEXT_TYPE_PT_SERIF
import com.fiction.me.utils.Constants.Companion.TEXT_TYPE_ROBOTO

class TextFontsAdapter(
    private val onFontItemClick: (position: Long, fontType: TextTypeData) -> Unit
) : BaseAdapter<ViewBinding, TextFontTypesModel, BaseViewHolder<TextFontTypesModel, ViewBinding>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<TextFontTypesModel, ViewBinding> {
        return TextFontsViewHolder(
            ItemTextFontBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onFontItemClick
        )
    }

    private class TextFontsViewHolder(
        private val binding: ItemTextFontBinding,
        private val onFontItemClick: (position: Long, textType: TextTypeData) -> Unit
    ) : BaseViewHolder<TextFontTypesModel, ViewBinding>(binding) {
        var fontType = TextTypeData.PT_SERIF
        override fun bind(item: TextFontTypesModel, context: Context) {
            with(binding) {
                fontTypeField.text = when (item.fontType) {
                    TextTypeData.PT_SERIF -> {
                        fontType = TextTypeData.PT_SERIF
                        TEXT_TYPE_PT_SERIF
                    }
                    else -> {
                        fontType = TextTypeData.ROBOTO
                        TEXT_TYPE_ROBOTO
                    }
                }
                fontTypeField.isSelected = item.isSelected

                fontTypeField.setOnClickListener {
                    onFontItemClick(item.id, fontType)
                }
            }
        }
    }
}