package com.name.domain.extentions

import com.name.domain.model.enums.ColorThemeData
import com.name.domain.model.enums.FlipTypeData
import com.name.domain.model.enums.LineHeightData
import com.name.domain.model.enums.TextTypeData
import com.name.entities.roommodels.enums.ColorThemeEntity
import com.name.entities.roommodels.enums.FlipTypeEntity
import com.name.entities.roommodels.enums.LineHeightEntity
import com.name.entities.roommodels.enums.TextTypeEntity

fun ColorThemeData?.toEntity() = (
        when (this) {
            ColorThemeData.BLACK -> ColorThemeEntity.BLACK
            ColorThemeData.WHITE -> ColorThemeEntity.WHITE
            ColorThemeData.SEPIA -> ColorThemeEntity.SEPIA
            else -> ColorThemeEntity.BLACK
        })

fun LineHeightData?.toEntity() = (
        when (this) {
            LineHeightData.DEFAULT -> LineHeightEntity.DEFAULT
            LineHeightData.BIGGER  -> LineHeightEntity.BIGGER
            else -> LineHeightEntity.DEFAULT
        })

fun FlipTypeData?.toEntity() = (
        when (this) {
            FlipTypeData.TURN_PAGE ->  FlipTypeEntity.TURN_PAGE
            FlipTypeData.SHIFT ->  FlipTypeEntity.SHIFT
            FlipTypeData.SCROLL ->  FlipTypeEntity.SCROLL
            else -> FlipTypeEntity.SCROLL
        })

fun TextTypeData?.toEntity() = (
        when (this) {
            TextTypeData.PT_SERIF -> TextTypeEntity.PT_SERIF
            else -> TextTypeEntity.ROBOTO
        })

fun ColorThemeEntity?.toData() = (
        when (this) {
            ColorThemeEntity.BLACK -> ColorThemeData.BLACK
            ColorThemeEntity.WHITE -> ColorThemeData.WHITE
            ColorThemeEntity.SEPIA -> ColorThemeData.SEPIA
            else -> ColorThemeData.BLACK
        })

fun LineHeightEntity?.toData() = (
        when (this) {
            LineHeightEntity.DEFAULT -> LineHeightData.DEFAULT
            LineHeightEntity.BIGGER  -> LineHeightData.BIGGER
            else -> LineHeightData.DEFAULT
        })

fun FlipTypeEntity?.toData() = (
        when (this) {
            FlipTypeEntity.TURN_PAGE ->  FlipTypeData.TURN_PAGE
            FlipTypeEntity.SHIFT ->  FlipTypeData.SHIFT
            FlipTypeEntity.SCROLL ->  FlipTypeData.SCROLL
            else -> FlipTypeData.SCROLL
        })

fun TextTypeEntity?.toData() = (
        when (this) {
            TextTypeEntity.PT_SERIF -> TextTypeData.PT_SERIF
            else -> TextTypeData.ROBOTO
        })