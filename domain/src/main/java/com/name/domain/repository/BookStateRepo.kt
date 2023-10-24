package com.name.domain.repository

import com.name.core.ActionResult
import com.name.entities.responce.BaseResultModel

interface BookStateRepo {

    suspend fun addBookToLibrary(bookId: Long): ActionResult<BaseResultModel<String>>

    suspend fun removeBookFromLibrary(bookId: Long): ActionResult<BaseResultModel<String>>
}