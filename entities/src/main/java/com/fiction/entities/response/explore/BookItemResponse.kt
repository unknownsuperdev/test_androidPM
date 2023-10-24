package com.fiction.entities.response.explore


import com.google.gson.annotations.SerializedName

data class BookItemResponse(
    @SerializedName("achievement")
    val achievement: Any?,
    @SerializedName("age_restriction")
    val ageRestriction: Int?,
    @SerializedName("author")
    val author: AuthorResponse?,
    @SerializedName("chapters")
    val chapters: List<ChapterResponse>?,
    @SerializedName("covers")
    val covers: CoversResponse?,
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
    @SerializedName("reading_progress")
    val readingProgress: ReadingProgressResponse?,
    @SerializedName("tags")
    val tags: List<TagResponse>?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("views")
    val views: Int?,
    @SerializedName("is_finished")
    val isFinished: Boolean?,
    @SerializedName("is_esclusive")
    val isEsclusive: Boolean?
)