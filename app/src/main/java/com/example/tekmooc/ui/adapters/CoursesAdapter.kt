package com.example.tekmooc.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tekmooc.R
import com.example.tekmooc.data.models.Result
import com.squareup.picasso.Picasso

class CoursesAdapter(private val context: Context, private val mCourses: List<Result>)
    : RecyclerView.Adapter<CoursesAdapter.CourseViewHolder>() {

    inner class CourseViewHolder(item: View) : RecyclerView.ViewHolder(item){
        val courseTitle = item.findViewById<TextView>(R.id.text_chat_username)
        val courseAuthor = item.findViewById<TextView>(R.id.text_chat_last_message)
        val courseImage = item. findViewById<ImageView>(R.id.image_chat_user)
        val coursePrice = item.findViewById<TextView>(R.id.text_chat_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val context = parent.context
        val courseView = LayoutInflater.from(context)
            .inflate(R.layout.course_list_item,parent, false)
        return CourseViewHolder(courseView)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {

        val result: Result = mCourses[position]
        holder.courseTitle.text = result.title.toString()
        holder.courseAuthor.text = result.instructor_name
        Picasso.with(context).load(result.image_125_H).into(holder.courseImage)
        holder.coursePrice.text = result.price.toString()
    }

    override fun getItemCount(): Int = mCourses.size
}