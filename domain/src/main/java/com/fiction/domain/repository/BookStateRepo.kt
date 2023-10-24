package com.fiction.domain.repository

import com.fiction.core.ActionResult
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.reader.ResponseMessage

interface BookStateRepo {

    suspend fun addBookToLibrary(bookId: Long): ActionResult<BaseResultModel<ResponseMessage>>

    suspend fun removeBookFromLibrary(bookId: Long): ActionResult<BaseResultModel<ResponseMessage>>
}