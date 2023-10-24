package com.name.jat.ui.fragments.profile.editprofile

import androidx.lifecycle.viewModelScope
import com.name.core.ActionResult
import com.name.domain.interactors.GetProfileInfoUseCase
import com.name.domain.interactors.RemoveProfileAvatarUseCase
import com.name.domain.interactors.UpdateProfileUseCase
import com.name.domain.model.profile.ProfileInfo
import com.name.jat.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val getProfileInfoUseCase: GetProfileInfoUseCase,
    private val removeProfileAvatarUseCase: RemoveProfileAvatarUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : BaseViewModel() {

    var imageUri: String = ""
    var userName: String = ""
    private val _editedUserName = MutableSharedFlow<Unit?>()
    val editedUserName = _editedUserName.asSharedFlow()

    private val _avatarRemoved = MutableSharedFlow<Unit?>()
    val avatarRemoved = _avatarRemoved.asSharedFlow()

    private val _updateProfile = MutableSharedFlow<Unit?>()
    val updateProfile = _updateProfile.asSharedFlow()

    private val _profileInfo = MutableStateFlow<ProfileInfo?>(null)
    val profileInfo = _profileInfo.asStateFlow()

    fun getProfileInfo() {
        viewModelScope.launch {
            when (val result = getProfileInfoUseCase()) {
                is ActionResult.Success -> {
                    userName = result.result.name
                    _profileInfo.emit(result.result)
                }
                is ActionResult.Error -> {
                    // Error handling
                }
            }
        }
    }

    fun removeAvatar() {
        viewModelScope.launch {
            when (removeProfileAvatarUseCase()) {
                is ActionResult.Success -> {
                    _avatarRemoved.emit(Unit)
                }
                is ActionResult.Error -> {
                    // Error Handling
                }
            }
        }
    }

    fun updateProfile() {
        viewModelScope.launch {
            if (imageUri.isEmpty() && userName == profileInfo.value?.name) return@launch

            val result = updateProfileUseCase(name = userName.ifEmpty { null },
                avatarUri = imageUri.ifEmpty { null })

            when (result) {
                is ActionResult.Success -> {
                    _updateProfile.emit(Unit)
                }
                is ActionResult.Error -> {
                    TODO()
                }
            }
        }

    }

    fun changeUserNameIfEdited(name: String) {
        if (name != userName)
            userName = name
    }

}