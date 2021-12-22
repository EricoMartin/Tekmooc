package com.example.tekmooc.ui.onboarding

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.viewpager2.widget.ViewPager2
import com.example.tekmooc.R
import com.example.tekmooc.ui.MainActivity
import com.example.tekmooc.ui.onboarding.adapter.WelcomeAdapter
import com.example.tekmooc.ui.onboarding.model.OnBoardingItem
import com.google.android.material.button.MaterialButton
import java.util.ArrayList

class WelcomeActivity : AppCompatActivity() {

    private var onboardingAdapter: WelcomeAdapter? = null
    private var layoutOnboardingIndicator: LinearLayout? = null
    private lateinit var buttonOnboardingAction: MaterialButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_welcome)

        layoutOnboardingIndicator = findViewById(R.id.layoutOnboardingIndicators)
        buttonOnboardingAction = findViewById(R.id.buttonOnBoardingAction)
        setOnboardingItem()
        val onboardingViewPager = findViewById<ViewPager2>(R.id.onboardingViewPager)
        onboardingViewPager.adapter = onboardingAdapter
        setOnboadingIndicator()
        setCurrentOnboardingIndicators(0)
        onboardingViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentOnboardingIndicators(position)
            }
        })
        buttonOnboardingAction.setOnClickListener(View.OnClickListener {
            if (onboardingViewPager.currentItem + 1 < onboardingAdapter!!.itemCount) {
                onboardingViewPager.currentItem = onboardingViewPager.currentItem + 1
            } else {
                startActivity(Intent(applicationContext, HomeActivity::class.java))
                finish()
            }
        })

    }

    private fun setOnboadingIndicator() {
        val indicators = arrayOfNulls<ImageView>(
            onboardingAdapter!!.itemCount
        )
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i]!!.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext, R.drawable.icons8_education_100
                )
            )
            indicators[i]!!.layoutParams = layoutParams
            layoutOnboardingIndicator!!.addView(indicators[i])
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCurrentOnboardingIndicators(index: Int) {
        val childCount = layoutOnboardingIndicator!!.childCount
        for (i in 0 until childCount) {
            val imageView = layoutOnboardingIndicator!!.getChildAt(i) as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_baseline_trending_flat_24
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_baseline_indicator
                    )
                )
            }
        }
        if (index == onboardingAdapter!!.itemCount - 1) {
            buttonOnboardingAction!!.text = "Start"
        } else {
            buttonOnboardingAction!!.text = "Next"
        }
    }

    private fun setOnboardingItem() {
        val onBoardingItems: MutableList<OnBoardingItem> = ArrayList()
        val itemStudy = OnBoardingItem(
            R.drawable.icons8_education_64_1,
            "Study at your own leisure",
            "Get the knowledge you require on the go"
        )
        val itemPaidOffline = OnBoardingItem(
            R.drawable.icons8_education_64_3,
            "The Digital world has brought education to your palm",
            "Enjoy your free and paid courses, all in one app. Don’t forget to rate us!"
        )
        val itemGetJob = OnBoardingItem(
            R.drawable.icons8_education_64,
            "No College/ University Degree, No problem",
            "Just keep learning offline or online, and become a freelancer or get a Job!"
        )
        onBoardingItems.add(itemStudy)
        onBoardingItems.add(itemPaidOffline)
        onBoardingItems.add(itemGetJob)
        onboardingAdapter = WelcomeAdapter(onBoardingItems)
    }

}