package com.fiction.me.ui.fragments.reader.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.fiction.domain.model.*
import com.fiction.domain.model.enums.ColorThemeData
import com.fiction.domain.model.enums.LineHeightData
import com.fiction.domain.model.enums.TextTypeData
import com.fiction.me.R
import com.fiction.me.appbase.adapter.BaseAdapter
import com.fiction.me.appbase.adapter.BaseViewHolder
import com.fiction.me.databinding.ItemFinishedReadingBinding
import com.fiction.me.databinding.ItemFlipPageBinding
import com.fiction.me.databinding.ItemOpenNextChapterBinding
import com.fiction.me.extensions.setOnDebounceClickListener

class FlipPagerAdapter(
    private var bookSettingsData: BookSettingsData,
    private val isCompleted: Boolean = true,
    private val onTouch: (isGoingNextChapter: Boolean) -> Unit
) : BaseAdapter<ViewBinding, BookContent, BaseViewHolder<BookContent, ViewBinding>>() {

    private val TYPE_TEXT = 0
    private val TYPE_LAST_ITEM = 1
    private val TYPE_FINISHED_ITEM = 2
    lateinit var lastItemVH: BaseViewHolder<BookContent, ViewBinding>
    var stopProgressListener : () -> Unit = {
        Log.i("stopProgressListener", ": bdfjbodbjpd")
        if (::lastItemVH.isInitialized)
        (lastItemVH as LastItemVH).stopProgressListener()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            TYPE_TEXT -> TextPagerVH(
                ItemFlipPageBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                bookSettingsData,
                onTouch
            ) as BaseViewHolder<BookContent, ViewBinding>

            TYPE_LAST_ITEM -> {
                lastItemVH = LastItemVH(
                    ItemOpenNextChapterBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    bookSettingsData,
                    onTouch,
                    // stopProgressListener
                ) as BaseViewHolder<BookContent, ViewBinding>
                lastItemVH
            }

            else -> {
                FinishedReadingItemVH(
                    ItemFinishedReadingBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    bookSettingsData,
                    onTouch,
                    isCompleted
                ) as BaseViewHolder<BookContent, ViewBinding>
            }
        }

    override fun getItemViewType(position: Int): Int {

        return when (getItem(position)) {
            is BookChapterText -> TYPE_TEXT
            is LastItemOfChapter -> TYPE_LAST_ITEM
            else -> TYPE_FINISHED_ITEM
        }
    }

    class TextPagerVH(
        private val binding: ItemFlipPageBinding,
        private val bookSettingsData: BookSettingsData,
        private val onTouch: (isGoingNextChapter: Boolean) -> Unit
    ) : BaseViewHolder<BookChapterText, ViewBinding>(binding) {

        override fun bind(item: BookChapterText, context: Context) {
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
                message.text = item.chapterPage
            }
        }

        override fun onItemClick(item: BookChapterText) {
            onTouch(false)
        }
    }

    class LastItemVH(
        private val binding: ItemOpenNextChapterBinding,
        private var bookSettingsData: BookSettingsData,
        private val onTouch: (isGoingNextChapter: Boolean) -> Unit,
       // var stopProgressListener : () -> Unit
    ) : BaseViewHolder<LastItemOfChapter, ViewBinding>(binding) {

        override fun bind(item: LastItemOfChapter, context: Context) {
            with(binding) {
                when (bookSettingsData.colorTheme) {
                    ColorThemeData.BLACK -> setTextColor(
                        R.color.primary_white,
                        R.color.dark_white_900
                    )
                    else -> setTextColor(R.color.black_100, R.color.black_200)
                }

                goForNext.setOnDebounceClickListener(1000L) {
                    goForNext.text = ""
                    progressCircular.visibility = View.VISIBLE
                    Log.i("stopProgressListener", ": visible${progressCircular.visibility}")
                    onTouch(true)
                }
            }
        }
       fun stopProgressListener()  {
           binding.progressCircular.visibility = View.GONE
           binding.goForNext.text = binding.root.context.getString(R.string.go_for_next)
       }
        override fun onItemClick(item: LastItemOfChapter) {
            onTouch(false)
        }

        private fun setTextColor(titleColor: Int, descriptionColor: Int) {
            with(binding) {
                title.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        titleColor
                    )
                )
                description.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        descriptionColor
                    )
                )
            }
        }
    }

    class FinishedReadingItemVH(
        private val binding: ItemFinishedReadingBinding,
        private var bookSettingsData: BookSettingsData,
        private val onTouch: (isGoingNextChapter: Boolean) -> Unit,
        private val isCompleted: Boolean
    ) : BaseViewHolder<FinishedItemOfChapter, ViewBinding>(binding) {

        override fun bind(item: FinishedItemOfChapter, context: Context) {
            with(binding) {
                description.text =
                    if (isCompleted) context.resources.getString(
                        R.string.you_have_completed_book
                    ) else  context.resources.getString(R.string.you_have_finished_ongoing)
                when (bookSettingsData.colorTheme) {
                    ColorThemeData.BLACK -> setTextColor(
                        R.color.primary_white,
                        R.color.dark_white_900
                    )
                    else -> setTextColor(R.color.black_100, R.color.black_200)
                }
            }
        }

        override fun onItemClick(item: FinishedItemOfChapter) {
            onTouch(false)
        }

        private fun setTextColor(titleColor: Int, descriptionColor: Int) {
            with(binding) {
                title.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        titleColor
                    )
                )
                description.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        descriptionColor
                    )
                )
            }
        }
    }
}