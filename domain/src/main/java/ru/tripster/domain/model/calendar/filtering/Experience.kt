package ru.tripster.domain.model.calendar.filtering

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.tripster.core.DiffUtilModel
import ru.tripster.entities.response.calendar.filtering.ExperienceResponse
import ru.tripster.entities.response.calendar.filtering.ExperienceTitleResponseModel

data class Experience(
    val count: Int,
    val next: String,
    val previous: String,
    val results: List<ExperienceResults>
) {
    companion object {
        fun from(experienceResponse: ExperienceResponse): Experience =
            with(experienceResponse) {
                Experience(
                    count = this.count ?: 0,
                    next = this.next ?: "",
                    previous = this.previous ?: "",
                    results = this.results?.map {
                        ExperienceResults.from(it)
                    } ?: emptyList()
                )
            }

    }
}

@Parcelize
data class ExperienceResults(
    val format: String,
    override val id: Int,
    val is_visible: Boolean,
    val movement_type: String,
    val tagline: String,
    val title: String,
    val type: String,
    var isChecked: Boolean = false
) : DiffUtilModel<Int>(), Parcelable {
    companion object {
        fun from(experienceResultResponseModel: ExperienceTitleResponseModel): ExperienceResults =
            with(experienceResultResponseModel) {
                ExperienceResults(
                    format = this.format ?: "",
                    id = this.id ?: 0,
                    is_visible = this.is_visible ?: false,
                    movement_type = this.movement_type ?: "",
                    tagline = this.tagline ?: "",
                    title = this.title ?: "",
                    type = this.type ?: ""
                )
            }

        fun allOrders(): ExperienceResults = ExperienceResults(
            "", 0, true, "", "", "Все заказы", "", false
        )

    }
}


