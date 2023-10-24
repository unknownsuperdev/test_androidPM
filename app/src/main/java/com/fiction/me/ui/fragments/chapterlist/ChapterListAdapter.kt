package com.fiction.me.ui.fragments.chapterlist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.fiction.domain.model.ChaptersDataModel
import com.fiction.me.R
import com.fiction.me.appbase.adapter.BaseAdapter
import com.fiction.me.appbase.adapter.BaseViewHolder
import com.fiction.me.databinding.ItemChapterBinding

class ChapterListAdapter(
    private val onItemClickListener: (id: Long) -> Unit
) : BaseAdapter<ViewBinding, ChaptersDataModel, BaseViewHolder<ChaptersDataModel, ViewBinding>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseViewHolder<ChaptersDataModel, ViewBinding> {
        return ChaptersViewHolder(
            ItemChapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            onItemClickListener
        )
    }

    private class ChaptersViewHolder(
        private val binding: ItemChapterBinding,
        private val onItemClickListener: (id: Long) -> Unit
    ) : BaseViewHolder<ChaptersDataModel, ViewBinding>(binding) {

        override fun bind(item: ChaptersDataModel, context: Context) {
            with(binding) {
                chapterTitleTxt.text = context.resources.getString(
                    R.string.chapter_list_title,
                    absoluteAdapterPosition + 1,
                    item.chapterTitle
                )

                if (item.isFree || !item.isFree && item.isPurchased) {
                    openOrLockedChapter.setImageResource(R.drawable.ic_arrow_white)
                } else {
                    openOrLockedChapter.setImageResource(R.drawable.ic_lock_white)
                }

                coinIcon.isVisible = !item.isFree && !item.isPurchased
                if (item.isLastReadChapter) {
                    chapterTitleTxt.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.heliotrope
                        )
                    )
                    if (item.isFree || !item.isFree && item.isPurchased) {
                        openOrLockedChapter.setImageResource(R.drawable.ic_arrow_heliotrope)
                    } else {
                        openOrLockedChapter.setImageResource(R.drawable.ic_lock_heliotrope)
                    }
                } else {
                    chapterTitleTxt.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.primary_white
                        )
                    )
                }
            }
        }

        override fun onItemClick(item: ChaptersDataModel) {
            onItemClickListener(item.id)
        }
    }
}