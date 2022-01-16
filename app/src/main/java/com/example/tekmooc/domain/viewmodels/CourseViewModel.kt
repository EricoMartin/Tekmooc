package com.example.tekmooc.domain.viewmodels

import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tekmooc.data.api.APIService
import com.example.tekmooc.data.api.ApiClient
import com.example.tekmooc.data.models.CourseList
import com.example.tekmooc.data.models.Result
import com.example.tekmooc.ui.adapters.CoursesAdapter
import com.example.tekmooc.ui.fragments.HomeFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.ArrayList

class CourseViewModel: ViewModel() {
    var mAPIService: APIService? = null
    private var _mCourses = MutableLiveData<List<Result>>()
    val mCourses: LiveData<List<Result>> get() = _mCourses
    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() =  _errorMessage

    init {
        Timber.d("CourseViewModel Created")
        mAPIService = ApiClient.client.create(APIService::class.java)
        getCoursesList()
    }

    fun getCoursesList() {
        val call = mAPIService?.getFreeCourses("Free")
        call?.enqueue(object : Callback<CourseList> {
            override fun onResponse(call: Call<CourseList>, response: Response<CourseList>) {
                Timber.d("Total courses: " + response.body()!!.count)
                val courses = response.body()
                if (courses != null) {
                    _mCourses.postValue(courses.results)
                }

            }

            override fun onFailure(call: Call<CourseList>, t: Throwable) {
                Log.e(TAG, "Error in Request : " + t.localizedMessage)
                _errorMessage.postValue(t.localizedMessage)
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        Timber.d("CourseViewModel Destroyed")
    }
    companion object {
        private val TAG = "CourseViewModel"
    }
}