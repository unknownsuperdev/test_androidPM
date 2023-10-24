package com.name.domain.model

import com.name.core.DiffUtilModel

data class ReportFieldsModel(
    override val id: Long,
    val text: String
) : DiffUtilModel<Long>()
