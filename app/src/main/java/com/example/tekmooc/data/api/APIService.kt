package com.example.tekmooc.data.api

import com.example.tekmooc.data.models.CourseList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface APIService {
    @GET("/api-2.0/courses/?search=android&price=price-free")
    fun getFreeCourses(@Query("search") search: String): Call<CourseList>

}