package ru.tripster.guide.ui.fragments.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import ru.tripster.domain.model.calendar.filtering.ExperienceResults
import ru.tripster.guide.appbase.adapter.BasePagingAdapter
import ru.tripster.guide.appbase.adapter.BaseViewHolder
import ru.tripster.guide.databinding.ItemOfFilteringBottomSheetBinding

class ExperienceAdapter(
    private val itemClick: (result: ExperienceResults) -> Unit
) :
    BasePagingAdapter<ViewBinding, ExperienceResults, BaseViewHolder<ExperienceResults, ViewBinding>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ExperienceResults, ViewBinding> {
        return ViewHolderExperienceTitle(
            ItemOfFilteringBottomSheetBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    inner class ViewHolderExperienceTitle(private val binding: ItemOfFilteringBottomSheetBinding) :
        BaseViewHolder<ExperienceResults, ViewBinding>(binding) {
        override fun onItemClick(item: ExperienceResults) {
            itemClick.invoke(item)
        }

        override fun bind(item: ExperienceResults, context: Context) {
            binding.checkIcon.isVisible = item.isChecked
            binding.experience.text = item.title
        }

    }
}