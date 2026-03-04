package com.smarttv.launcher.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.smarttv.launcher.models.AppItem

object AppUtils {

    private val HIDDEN_PACKAGES = setOf(
        "com.android.settings",
        "com.android.systemui"
    )

    fun getInstalledApps(context: Context): List<AppItem> {
        val pm = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
        }

        var apps = pm.queryIntentActivities(mainIntent, 0)
            .filter { it.activityInfo.packageName !in HIDDEN_PACKAGES }
            .map { resolveInfo ->
                AppItem(
                    packageName = resolveInfo.activityInfo.packageName,
                    label = resolveInfo.loadLabel(pm).toString(),
                    icon = resolveInfo.loadIcon(pm),
                    category = getCategory(resolveInfo.activityInfo.packageName)
                )
            }
            .sortedBy { it.label }

        // Fallback: if no TV apps found, get all launcher apps
        if (apps.isEmpty()) {
            val fallbackIntent = Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            apps = pm.queryIntentActivities(fallbackIntent, 0)
                .filter { it.activityInfo.packageName !in HIDDEN_PACKAGES }
                .map { resolveInfo ->
                    AppItem(
                        packageName = resolveInfo.activityInfo.packageName,
                        label = resolveInfo.loadLabel(pm).toString(),
                        icon = resolveInfo.loadIcon(pm),
                        category = getCategory(resolveInfo.activityInfo.packageName)
                    )
                }
                .sortedBy { it.label }
        }

        return apps
    }

    private fun getCategory(packageName: String): String {
        return when {
            packageName.contains("netflix") ||
            packageName.contains("youtube") ||
            packageName.contains("prime") ||
            packageName.contains("hulu") ||
            packageName.contains("disney") ||
            packageName.contains("twitch") ||
            packageName.contains("video") ||
            packageName.contains("stream") -> "Streaming"

            packageName.contains("game") ||
            packageName.contains("play") -> "Games"

            packageName.contains("music") ||
            packageName.contains("spotify") ||
            packageName.contains("podcast") -> "Music"

            packageName.contains("settings") ||
            packageName.contains("system") -> "Settings"

            else -> "Apps"
        }
    }
}
