package com.example.tekmooc.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tekmooc.R
import com.example.tekmooc.data.models.Result
import com.example.tekmooc.databinding.FragmentHomeBinding
import com.example.tekmooc.domain.viewmodels.CourseViewModel
import com.example.tekmooc.ui.adapters.CoursesAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private lateinit var viewModel: CourseViewModel

    private var mCoursesAdapter: CoursesAdapter? = null
    private lateinit var mCourses: MutableLiveData<List<Result>>


    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        Timber.d("Called ViewModelProvider")
        viewModel = ViewModelProvider(this)[CourseViewModel::class.java]

        val courseRCV = binding.recyclerView
        courseRCV.layoutManager = LinearLayoutManager(requireContext())
        mCoursesAdapter = CoursesAdapter(requireContext())
        courseRCV.adapter = mCoursesAdapter

        val itemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        courseRCV.addItemDecoration(itemDecoration)

        getCoursesList()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)

        val layoutButton = menu.findItem(R.id.action_signout)
        setIcon(layoutButton)
    }

    private fun setIcon(menuItem: MenuItem?) {
        if (menuItem == null)
            return

        menuItem.icon =
                ContextCompat.getDrawable(this.requireContext(), R.drawable.three_dots)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_signout -> {
                setIcon(item)
                Firebase.auth.signOut()
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getCoursesList() {
        viewModel.mCourses.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            mCoursesAdapter?.setCourseList(it)
            mCoursesAdapter!!.notifyDataSetChanged()
        })
    }

    companion object {
        private val TAG = "HomeFragment"
    }
}