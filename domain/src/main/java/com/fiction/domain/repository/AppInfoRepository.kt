package com.fiction.domain.repository

interface AppInfoRepository {

    suspend fun getStartAppVersion(): String

    suspend fun getCohortDay(): Int

    suspend fun getCohortWeek(): Int

    suspend fun getCohortMonth(): Int

    suspend fun getCohortYear(): Int

}