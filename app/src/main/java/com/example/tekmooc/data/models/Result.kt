package com.example.tekmooc.data.models

import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("_class") val _class : String,
    @SerializedName("id") val id : Int,
    @SerializedName("title") val title : String,
    @SerializedName("url") val url : String,
    @SerializedName("is_paid") val is_paid : Boolean,
    @SerializedName("price") val price : String,
    @SerializedName("price_detail") val price_detail : String,
    @SerializedName("price_serve_tracking_id") val price_serve_tracking_id : String,
    @SerializedName("visible_instructors") val visible_instructors : List<VisibleInstructor>,
    @SerializedName("image_125_H") val image_125_H : String,
    @SerializedName("image_240x135") val image_240x135 : String,
    @SerializedName("is_practice_test_course") val is_practice_test_course : Boolean,
    @SerializedName("image_480x270") val image_480x270 : String,
    @SerializedName("published_title") val published_title : String,
    @SerializedName("tracking_id") val tracking_id : String,
    @SerializedName("predictive_score") val predictive_score : String,
    @SerializedName("relevancy_score") val relevancy_score : String,
    @SerializedName("input_features") val input_features : String,
    @SerializedName("lecture_search_result") val lecture_search_result : String,
    @SerializedName("curriculum_lectures") val curriculum_lectures : List<String>,
    @SerializedName("order_in_results") val order_in_results : String,
    @SerializedName("curriculum_items") val curriculum_items : List<String>,
    @SerializedName("headline") val headline : String,
    @SerializedName("instructor_name") val instructor_name : String
    )
