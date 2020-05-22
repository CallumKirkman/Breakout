package com.example.breakout.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.breakout.R

class SettingsAdapter(private val setting: List<String>, var clickListener: OnSettingClickListener) : RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {

    interface OnSettingClickListener {
        fun onSettingClick(setting: String, position: Int) {
        }
    }


    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {

        val settingText: TextView = itemView.findViewById(R.id.settingName)

        fun clickAction(setting: String, action: OnSettingClickListener) {
            settingText.text = setting

            itemView.setOnClickListener {
                action.onSettingClick(setting, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val settingView = inflater.inflate(R.layout.item_settings, parent, false)
        // Return a new holder instance
        return ViewHolder(settingView)
    }

    override fun getItemCount(): Int {
        return setting.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.clickAction(setting[position], clickListener)
    }
}