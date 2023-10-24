package com.name.domain.model.profile

import com.name.core.DiffUtilModel

data class PurchaseHistoryData(
    override val id: Long,
    val title: String,
    val countOfUnlockChapters: Int,
    val date: String,
    val cover: String,
) : DiffUtilModel<Long>()