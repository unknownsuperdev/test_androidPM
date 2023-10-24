package com.fiction.data.repository

import android.util.Log
import com.core.preference.PreferenceStore
import com.fiction.data.BuildConfig
import com.fiction.domain.repository.AppInfoRepository
import kotlinx.coroutines.flow.first
import java.util.*

class AppInfoRepositoryImpl(
    private val preferenceStore: PreferenceStore
) : AppInfoRepository {

    private val START_APP_VERSION_FIELD = "start_app_version_field"
    private val COHORT_DAY_FIELD = "cohort_day_field"
    private val COHORT_WEEK_FIELD = "cohort_week_field"
    private val COHORT_MONTH_FIELD = "cohort_month_field"
    private val COHORT_YEAR_FIELD = "cohort_year_field"

    override suspend fun getStartAppVersion(): String {
        val saveAppVersion = preferenceStore.getString(START_APP_VERSION_FIELD).first()
        return if (saveAppVersion == null) {
            preferenceStore.setValue(START_APP_VERSION_FIELD, BuildConfig.VERSION_NAME)
            BuildConfig.VERSION_NAME
        }else {
            saveAppVersion
        }
    }

    override suspend fun getCohortDay(): Int {
        var cohortDay = preferenceStore.getInt(COHORT_DAY_FIELD).first()
        return if (cohortDay == null) {
            cohortDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
            preferenceStore.setValue(COHORT_DAY_FIELD, cohortDay)
            cohortDay
        }else {
            cohortDay
        }
    }

    override suspend fun getCohortWeek(): Int {
        var cohortWeek = preferenceStore.getInt(COHORT_WEEK_FIELD).first()
        return if (cohortWeek == null) {
            cohortWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)
            preferenceStore.setValue(COHORT_WEEK_FIELD, cohortWeek)
            cohortWeek
        }else {
            cohortWeek
        }
    }

    override suspend fun getCohortMonth(): Int {
        var cohortMonth = preferenceStore.getInt(COHORT_MONTH_FIELD).first()
        return if (cohortMonth == null) {
            Log.d("cohortMonth", "null $cohortMonth")
            cohortMonth = Calendar.getInstance().get(Calendar.MONTH)
            preferenceStore.setValue(COHORT_MONTH_FIELD, cohortMonth)
            cohortMonth
        }else {
            Log.d("cohortMonth", "notnull $cohortMonth")
            cohortMonth
        }
    }

    override suspend fun getCohortYear(): Int {
        var cohortYear = preferenceStore.getInt(COHORT_YEAR_FIELD).first()
        return if (cohortYear == null) {
            cohortYear = Calendar.getInstance().get(Calendar.YEAR)
            preferenceStore.setValue(COHORT_YEAR_FIELD, cohortYear)
            cohortYear
        }else {
            cohortYear
        }
    }

}