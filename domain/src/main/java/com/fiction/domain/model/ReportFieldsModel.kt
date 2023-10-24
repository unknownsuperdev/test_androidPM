package com.fiction.domain.model

import com.fiction.core.DiffUtilModel

data class ReportFieldsModel(
    override val id: Int,
    val text: String,
    val isSelected: Boolean = false
) : DiffUtilModel<Int>()
