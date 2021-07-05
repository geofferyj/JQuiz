package com.geofferyj.jquiz.ui.fragments.instruction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.geofferyj.jquiz.databinding.FragmentInstructionsBinding

class InstructionsFragment : Fragment() {

    private lateinit var binding: FragmentInstructionsBinding
    private val args:InstructionsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentInstructionsBinding.inflate(inflater, container, false)
        binding.textView.text = args.course.name
        binding.materialButton.setOnClickListener {
            val action = InstructionsFragmentDirections.actionInstructionsFragmentToQuestionListFragment(args.course)
            findNavController().navigate(action)
        }


        val timeItems = listOf(
            "0:30 - Quiz",
            "01:00 - Quiz",
            "03:00 - Quiz",
            "10:00 - CBT",
            "20:00 - CBT",
            "30:00 - CBT"
        )

        val questionsNoItems = listOf(
            "5",
            "10",
            "20",
            "30",
            "50"
        )

        val timeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, timeItems)

        val questionsNoAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, questionsNoItems)
        (binding.selectTime.editText as? AutoCompleteTextView)?.setAdapter(timeAdapter)
        (binding.selectQuestions.editText as? AutoCompleteTextView)?.setAdapter(questionsNoAdapter)

        return binding.root
    }

}