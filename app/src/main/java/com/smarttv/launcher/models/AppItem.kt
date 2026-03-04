package com.smarttv.launcher.models

import android.graphics.drawable.Drawable

data class AppItem(
    val packageName: String,
    val label: String,
    val icon: Drawable,
    val category: String = "Other"
)
