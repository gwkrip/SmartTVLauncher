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

class FeaturedAdapter(
    private val onAppClick: (AppItem) -> Unit
) : ListAdapter<AppItem, FeaturedAdapter.FeaturedViewHolder>(FeaturedDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_featured_app, parent, false)
        return FeaturedViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeaturedViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FeaturedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivIcon: ImageView = itemView.findViewById(R.id.iv_featured_icon)
        private val tvLabel: TextView = itemView.findViewById(R.id.tv_featured_label)

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
                        .scaleX(1.1f)
                        .scaleY(1.1f)
                        .translationZ(8f)
                        .setDuration(200)
                        .start()
                } else {
                    view.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .translationZ(0f)
                        .setDuration(200)
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

    class FeaturedDiffCallback : DiffUtil.ItemCallback<AppItem>() {
        override fun areItemsTheSame(oldItem: AppItem, newItem: AppItem): Boolean =
            oldItem.packageName == newItem.packageName

        override fun areContentsTheSame(oldItem: AppItem, newItem: AppItem): Boolean =
            oldItem.label == newItem.label
    }
}
