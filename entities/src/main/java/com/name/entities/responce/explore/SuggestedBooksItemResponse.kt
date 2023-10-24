package com.name.entities.responce.explore


import com.google.gson.annotations.SerializedName

data class SuggestedBooksItemResponse(
    @SerializedName("achievement")
    val achievement: String?,
    @SerializedName("age_restriction")
    val ageRestriction: Int?,
    @SerializedName("author")
    val author: AuthorResponse?,
    @SerializedName("chapters")
    val chapters: List<ChapterResponse>?,
    @SerializedName("cover")
    val cover: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("genres")
    val genres: List<GenreResponse>?,
    @SerializedName("id")
    val id: Long?,
    @SerializedName("is_added_to_library")
    val isAddedToLibrary: Boolean?,
    @SerializedName("is_completed")
    val isCompleted: Boolean?,
    @SerializedName("is_liked")
    val isLiked: Boolean?,
    @SerializedName("likes")
    val likes: Int?,
    @SerializedName("tags")
    val tags: List<TagResponse>?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("views")
    val views: Int?
)