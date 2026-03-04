package com.smarttv.launcher.adapters

import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smarttv.launcher.R
import com.smarttv.launcher.models.AppItem

class AppAdapter(
    private val onAppClick: (AppItem) -> Unit
) : ListAdapter<AppItem, AppAdapter.AppViewHolder>(AppDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivIcon: ImageView = itemView.findViewById(R.id.iv_app_icon)
        private val tvLabel: TextView = itemView.findViewById(R.id.tv_app_label)

        fun bind(app: AppItem) {
            ivIcon.setImageDrawable(app.icon)
            tvLabel.text = app.label

            itemView.setOnClickListener {
                onAppClick(app)
            }

            itemView.setOnFocusChangeListener { view, hasFocus ->
                view.isSelected = hasFocus
                if (hasFocus) {
                    view.animate()
                        .scaleX(1.15f)
                        .scaleY(1.15f)
                        .setDuration(150)
                        .start()
                } else {
                    view.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(150)
                        .start()
                }
            }

            itemView.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                    onAppClick(app)
                    true
                } else {
                    false
                }
            }

            itemView.isFocusable = true
            itemView.isFocusableInTouchMode = false
        }
    }

    class AppDiffCallback : DiffUtil.ItemCallback<AppItem>() {
        override fun areItemsTheSame(oldItem: AppItem, newItem: AppItem): Boolean =
            oldItem.packageName == newItem.packageName

        override fun areContentsTheSame(oldItem: AppItem, newItem: AppItem): Boolean =
            oldItem.label == newItem.label
    }
}
