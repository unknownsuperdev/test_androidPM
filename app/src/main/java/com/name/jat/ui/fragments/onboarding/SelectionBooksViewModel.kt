package com.name.jat.ui.fragments.onboarding

import androidx.lifecycle.viewModelScope
import com.name.domain.interactors.SetIsOpenedWelcomeScreenUseCase
import com.name.jat.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SelectionBooksViewModel(
    private val setIsOpenedWelcomeScreenUseCase: SetIsOpenedWelcomeScreenUseCase
) : BaseViewModel() {
    private val _images = MutableStateFlow<List<String>?>(null)
    val images = _images.asStateFlow()

    fun getSliderImages() {
        viewModelScope.launch {
            val sliderImages = listOf(
                "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
                "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
                "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
                "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612"
            )
            _images.emit(sliderImages)
        }
    }

    fun setOpenedWelcomeScreens() {
        viewModelScope.launch {
            setIsOpenedWelcomeScreenUseCase(true)
        }
    }
}