package com.example.tekmooc.ui.onboarding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tekmooc.ui.onboarding.model.OnBoardingItem
import android.view.View
import android.widget.ImageView

import androidx.annotation.NonNull

import android.widget.TextView
import com.example.tekmooc.R


class WelcomeAdapter() : RecyclerView.Adapter<WelcomeAdapter.WelcomeViewHolder>() {
    private var onBoardingItems: List<OnBoardingItem>? = null
    constructor(onBoardingItem: List<OnBoardingItem>?) : this() {
        this.onBoardingItems = onBoardingItem
    }
    inner class WelcomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textTitle: TextView
        private val textDescription: TextView
        private val imageOnboarding: ImageView
        fun setOnBoardingData(onBoardingItem: OnBoardingItem) {
            textTitle.text = onBoardingItem.title
            textDescription.text = onBoardingItem.description
            imageOnboarding.setImageResource(onBoardingItem.image)
        }

        init {
            textTitle = itemView.findViewById(com.example.tekmooc.R.id.textTitle)
            textDescription = itemView.findViewById(com.example.tekmooc.R.id.textDescription)
            imageOnboarding = itemView.findViewById(com.example.tekmooc.R.id.imageOnboarding)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WelcomeAdapter.WelcomeViewHolder {
        var inflated = LayoutInflater.from(parent.context).inflate(
            R.layout.welcome_item_container, parent, false
        )
        return WelcomeViewHolder(inflated)

    }

    override fun onBindViewHolder(holder: WelcomeAdapter.WelcomeViewHolder, position: Int) {
        onBoardingItems?.let { holder.setOnBoardingData(it.get(position)) };
    }

    override fun getItemCount(): Int {
        return onBoardingItems!!.size;
    }
}