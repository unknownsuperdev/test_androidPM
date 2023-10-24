package com.name.jat.ui.fragments.chapterlist

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.name.domain.model.ChaptersDataModel
import com.name.jat.R
import com.name.jat.appbase.adapter.BaseAdapter
import com.name.jat.appbase.adapter.BaseViewHolder
import com.name.jat.databinding.ItemChapterBinding

class ChapterListAdapter :
    BaseAdapter<ViewBinding, ChaptersDataModel, BaseViewHolder<ChaptersDataModel, ViewBinding>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ChaptersDataModel, ViewBinding> {
        return ChaptersViewHolder(
            ItemChapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            parent.context
        )
    }

    private class ChaptersViewHolder(
        private val binding: ItemChapterBinding,
        private val context: Context
    ) :
        BaseViewHolder<ChaptersDataModel, ViewBinding>(binding) {

        @SuppressLint("CheckResult", "SetTextI18n")
        override fun bind(item: ChaptersDataModel, context: Context) {
            with(binding) {
                chapterTitleTxt.text = "${adapterPosition + 1}. ${item.chapterTitle}"

                if (item.isLocked) {
                    openOrLockedChapter.setImageResource(R.drawable.ic_lock_white)
                    coinIcon.visibility = View.VISIBLE
                } else {
                    openOrLockedChapter.setImageResource(R.drawable.ic_arrow_white)
                    coinIcon.visibility = View.GONE
                }

            }

            if (item.isLastReadChapter) {
                binding.chapterTitleTxt.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.heliotrope
                    )
                )
            }else {
                binding.chapterTitleTxt.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.primary_white
                    )
                )
            }
        }

        override fun onItemClick(item: ChaptersDataModel) {
            with(binding) {
                chapterTitleTxt.setTextColor(ContextCompat.getColor(context, R.color.black_700))

                if (item.isLocked) {
                    openOrLockedChapter.setImageResource(R.drawable.ic_lock_black_800)
                } else
                    openOrLockedChapter.setImageResource(R.drawable.ic_arrow_black_800)
            }
        }
    }
}