package com.geofferyj.jquiz.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Course(val id: String = "", val name: String = "") : Parcelable