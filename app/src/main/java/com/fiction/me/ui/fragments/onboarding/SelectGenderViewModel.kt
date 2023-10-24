package com.fiction.me.ui.fragments.onboarding

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.domain.interactors.GetGenderUseCase
import com.fiction.domain.interactors.GetOnBoardingSettingUseCase
import com.fiction.domain.interactors.InsertOnBoardingSettingUseCase
import com.fiction.domain.interactors.UpdateOnBoardingSettingUseCase
import com.fiction.domain.model.onboarding.Gender
import com.fiction.domain.model.onboarding.OnBoardingSettingData
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SelectGenderViewModel(
    private val getGenderUseCase: GetGenderUseCase,
    private val updateOnBoardingSettingUseCase: UpdateOnBoardingSettingUseCase,
    private val insertOnBoardingSettingUseCase: InsertOnBoardingSettingUseCase,
    private val getOnBoardingSettingUseCase: GetOnBoardingSettingUseCase,
) : BaseViewModel() {

    private val _genders = MutableStateFlow<List<Gender>?>(null)
    val genders = _genders.asStateFlow()

    private val _selectedGenderId = MutableStateFlow<Int?>(null)
    val selectedGenderId = _selectedGenderId.asStateFlow()

    private var onBoardingSettingsId = ""

    fun getGenders() {
        viewModelScope.launch {
            when (val result = getGenderUseCase()) {
                is ActionResult.Success -> {
                    _genders.emit(result.result)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    fun updateGenderSelection(genderId: Int?) {
        if (genderId == null) return
        viewModelScope.launch {
            when (val result = updateOnBoardingSettingUseCase(genderId = genderId)) {
                is ActionResult.Success -> {
                    Log.i("UpdateSettings", "updateGenderSelection: ${result.result}")
                }
                is ActionResult.Error -> {
                    Log.i("UpdateSettings", "updateGenderSelection: ${result.errors}")
                    // TODO() //Error Handling
                }
            }
        }
    }
    fun insertOnBoardingSetting(selectedGenderId: Int){
        viewModelScope.launch {
            insertOnBoardingSettingUseCase(OnBoardingSettingData(id = onBoardingSettingsId,genderId = selectedGenderId))
        }
    }

    fun getOnBoardingSetting(){
        viewModelScope.launch {
            val onBoardingSetting = getOnBoardingSettingUseCase()
            onBoardingSettingsId = onBoardingSetting.id
            onBoardingSetting.genderId?.let {
                _selectedGenderId.emit(it)
            }
        }
    }
}