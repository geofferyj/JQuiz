package com.geofferyj.jquiz.ui.fragments.questions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.geofferyj.jquiz.R
import com.geofferyj.jquiz.databinding.FragmentQuestionDetailsBinding
import com.geofferyj.jquiz.models.Question
import com.geofferyj.jquiz.utils.customListIterator
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*


class QuestionDetailsFragment : Fragment() {

    private lateinit var binding: FragmentQuestionDetailsBinding
    private val args: QuestionDetailsFragmentArgs by navArgs()

    private val questionsList: MutableLiveData<List<Question>> = MutableLiveData()

    private val results: MutableMap<String, Boolean> = mutableMapOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val quizTime = toMilliseconds(args.quizTime)
        val questionCount = args.questionsCount
        val courseId = args.courseId


        FirebaseFirestore.getInstance().collection("questions")
            .whereEqualTo("courseId", courseId)
            .addSnapshotListener { value, error ->
                val questions = mutableListOf<Question>()
                if (error != null) {
                    Log.d("TAG", "Error Getting Trips: ${error.message}")
                    return@addSnapshotListener
                }

                value?.forEach {
                    val question = it.toObject(Question::class.java)
                    questions.add(question)
                    questions.shuffle()
                }

                questionsList.value = try {
                    questions.slice(0..questionCount)
                } catch (e: IndexOutOfBoundsException) {
                    questions
                }
            }


        questionsList.observe(viewLifecycleOwner) {
            it.forEach { question ->
                results[question.id] = false
            }
            val questions = it.customListIterator()
            val q = questions.current()
            binding.question.text = q.question
            createOptions(q)

            binding.previous.setOnClickListener {
                if (questions.hasPrevious()) {
                    val p = questions.previous()
                    binding.question.text = p.question
                    createOptions(p)
                }
            }

            binding.next.setOnClickListener {
                if (questions.hasNext()) {
                    val n = questions.next()
                    binding.question.text = n.question
                    createOptions(n)
                }
            }
        }

        binding.submit.setOnClickListener {
            submit()
        }

        startTimer(quizTime)
    }

    private fun startTimer(timeMillis: Long) {
        lifecycleScope.launchWhenCreated {
            for (i in timeMillis downTo 0 step 1000) {
                binding.timer.text = millisToMinSecs(i)
                delay(1000)
            }
            submit()
        }
    }

    private fun toMilliseconds(date: String): Long {

        val minutes = date.split(":")[0].trim().toInt()
        val seconds = date.split(":")[1].trim().toInt()
        val millis = ((minutes * 60) + seconds) * 1000

        return millis.toLong()
    }

    private fun millisToMinSecs(millis: Long): String {
        val sdf = SimpleDateFormat("mm:ss", Locale.US)
        return sdf.format(millis)
    }

    private fun createOptions(question: Question) {
        val options = question.options.toMutableList()
        options.shuffle()
        val radioGrp = binding.optionsGroup

        radioGrp.removeAllViews()

        options.forEachIndexed { index, option ->
            val radioButton = RadioButton(requireContext())
            radioButton.text = option
            radioButton.id = index
            radioGrp.addView(radioButton)
        }

        radioGrp.setOnCheckedChangeListener { _, checkedId ->
            val radioBtn = requireActivity().findViewById<RadioButton>(checkedId)

            results[question.id] = radioBtn.text.toString() == question.answer

        }
    }

    private fun submit() {
        val totalQuestions = args.questionsCount
        val correctQuestions = results.values.count {
            it
        }

        val action =
            QuestionDetailsFragmentDirections.actionQuestionDetailsFragmentToResultsFragment(
                totalQuestions,
                correctQuestions
            )

        if (findNavController().currentDestination?.id == R.id.questionDetailsFragment){
            findNavController().navigate(action)
        }
    }

    private companion object {
        private const val TAG = "QuestionDetailsFragment"

    }


}