package com.geofferyj.jquiz.ui.fragments.questions

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.geofferyj.jquiz.R
import com.geofferyj.jquiz.databinding.FragmentQuestionListBinding
import com.geofferyj.jquiz.models.Course
import com.geofferyj.jquiz.models.Question
import com.google.firebase.firestore.FirebaseFirestore

class QuestionListFragment : Fragment() {

    private lateinit var binding: FragmentQuestionListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.textView.text = args.course.name
        val questionRVAdapter = QuestionRVAdapter()
        questionRVAdapter.setOnItemClickListener { position, item ->

        }

        binding.questionRv.adapter = questionRVAdapter

        FirebaseFirestore.getInstance().collection("questions")
//            .whereEqualTo("courseId", args.course.id)
            .addSnapshotListener { value, error ->
                val currentList = mutableListOf<Question>()
                if (error != null) {
                    Log.d("TAG", "Error Getting Trips: ${error.message}")
                    return@addSnapshotListener
                }

                value?.forEach {
                    val course = it.toObject(Question::class.java)
                    currentList.add(course)
                }

                questionRVAdapter.differ.submitList(currentList)
            }
    }
}