package com.example.tekmooc.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tekmooc.data.api.APIService
import com.example.tekmooc.data.api.ApiClient
import com.example.tekmooc.data.models.Result
import com.example.tekmooc.data.models.CourseList
import com.example.tekmooc.databinding.FragmentHomeBinding
import com.example.tekmooc.ui.adapters.CoursesAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private var mAPIService: APIService? = null
    private var mCoursesAdapter: CoursesAdapter? = null
    private var mCourses: MutableList<Result> = ArrayList()

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val courseRCV = binding.recyclerView
        courseRCV.layoutManager = LinearLayoutManager(requireContext())
        mCoursesAdapter = CoursesAdapter(requireContext(), mCourses)
        courseRCV.adapter = mCoursesAdapter
        mAPIService = ApiClient.client.create(APIService::class.java)

        getCoursesList()
        return binding.root
    }

    private fun getCoursesList() {
        val call = mAPIService?.getFreeCourses("Free")
        call?.enqueue(object : Callback<CourseList> {
            override fun onResponse(call: Call<CourseList>, response: Response<CourseList>) {
                Log.d(TAG, "Total courses: " + response.body()!!.count)
                val courses = response.body()
                if (courses != null) {
                    mCourses.addAll(courses.results)
                    mCoursesAdapter!!.notifyDataSetChanged()
                }

            }

            override fun onFailure(call: Call<CourseList>, t: Throwable) {
                Log.e(TAG, "Error in Request : " + t.localizedMessage)
            }
        })
    }

    companion object {
        private val TAG = "HomeFragment"
    }
}