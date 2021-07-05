package com.geofferyj.jquiz.ui.fragments.courses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.geofferyj.jquiz.databinding.FragmentCourseListBinding
import com.geofferyj.jquiz.models.Course
import com.google.firebase.firestore.FirebaseFirestore

class CourseListFragment : Fragment() {

    private lateinit var binding: FragmentCourseListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCourseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val coursesAdapter = CourseRVAdapter()
        coursesAdapter.setOnItemClickListener { position, item ->
            val action = CourseListFragmentDirections.actionCourseListFragmentToInstructionsFragment(item)
            findNavController().navigate(action)

        }

        binding.coursesRv.adapter = coursesAdapter

        FirebaseFirestore.getInstance().collection("courses")
            .addSnapshotListener { value, error ->
                val currentList = mutableListOf<Course>()
                if (error != null) {
                    Log.d("TAG", "Error Getting Trips: ${error.message}")
                    return@addSnapshotListener
                }

                value?.forEach {
                    val course = it.toObject(Course::class.java)
                    currentList.add(course)
                }


                currentList.listIterator()
                coursesAdapter.differ.submitList(currentList)
            }

    }

}