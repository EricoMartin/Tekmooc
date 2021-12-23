package com.example.tekmooc.data.models

data class CourseList(val count: Int, val next: String, val previous: Any? = null,
                      val results: List<Result>, val aggregations: List<Aggregations>, val search_tracking_id: String )
