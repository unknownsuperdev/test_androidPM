package com.fiction.me.ui.fragments.profile.purchasehistory

import androidx.lifecycle.viewModelScope
import com.fiction.domain.model.profile.PurchaseHistoryData
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PurchaseHistoryViewModel : BaseViewModel() {
    private val _purchaseHistoryBooks = MutableStateFlow<List<PurchaseHistoryData>?>(null)
    val purchaseHistoryBooks = _purchaseHistoryBooks.asStateFlow()

    fun getPurchaseHistoryList() {
        val bookList = listOf(
            PurchaseHistoryData(
                90,
                "Hungry wolves",
                32,
                "Apr 16, 2022",
                cover = "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
            ),
            PurchaseHistoryData(
                91,
                "Hungry wolves",
                32,
                "Apr 16, 2022",
                cover = "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
            ),
            PurchaseHistoryData(
                92,
                "Hungry wolves",
                32,
                "Apr 16, 2022",
                cover = "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
            ),
            PurchaseHistoryData(
                93,
                "Hungry wolves",
                32,
                "Apr 16, 2022",
                cover = "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
            ),
            PurchaseHistoryData(
                94,
                "Hungry wolves",
                32,
                "Apr 16, 2022",
                cover = "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
            ), PurchaseHistoryData(
                92,
                "Hungry wolves",
                32,
                "Apr 16, 2022",
                cover = "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
            ),
            PurchaseHistoryData(
                93,
                "Hungry wolves",
                32,
                "Apr 16, 2022",
                cover = "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
            ),
            PurchaseHistoryData(
                94,
                "Hungry wolves",
                32,
                "Apr 16, 2022",
                cover = "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
            )
        )
        viewModelScope.launch {
            _purchaseHistoryBooks.emit(bookList)
        }
    }

}