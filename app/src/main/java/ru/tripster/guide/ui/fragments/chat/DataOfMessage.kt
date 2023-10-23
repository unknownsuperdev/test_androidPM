package ru.tripster.guide.ui.fragments.chat

import ru.tripster.core.DiffUtilModel

data class DataOfMessage(
    val text: String,
    val type: Int,
    override val id: String,
    val image: Int?,
    val name: String?,
) : DiffUtilModel<String>()
