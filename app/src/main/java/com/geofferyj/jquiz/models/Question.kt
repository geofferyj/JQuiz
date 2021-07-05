package com.geofferyj.jquiz.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Question(
    val id:String = "",
    val courseId:String = "",
    val question: String = "",
    val answer: String = "",
    val options: List<String> = listOf("")
) : Parcelable
