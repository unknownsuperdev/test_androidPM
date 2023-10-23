package ru.tripster.guide.ui.fragments.cancel.model

import ru.tripster.core.DiffUtilModel
import ru.tripster.guide.ui.fragments.cancel.model.SubDataType

data class OrderCancelReasonData(
    override val id: Long,
    val text: String,
    val type: SubDataType?
) : DiffUtilModel<Long>()


