package ru.tripster.domain.model

import ru.tripster.core.DiffUtilModel

data class DataChip(
    override val id:Int,
    val name: String,
    val count: Int?
) : DiffUtilModel<Int>()
