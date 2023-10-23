package ru.tripster.domain.model

import ru.tripster.core.DiffUtilModel

data class DataOrder(
    override val id: String,
    val type: String?,
    val status: String?,
    val number: Int?,
    val location: String?,
    val date: String?,
    val avatar: String?,
    val name: String?,
    val participants: Int?,
    val coast: Int?,
    val messageCount: Int?,
    val message: String?,
    val countPeople:Int?,
    val countNotPaid:Int?,
    val countPaid:Int?
) : DiffUtilModel<String>()
