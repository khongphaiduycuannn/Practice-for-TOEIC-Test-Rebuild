package com.example.practicefortoeictestrebuild.ui.lesson

import android.app.Dialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practicefortoeictestrebuild.adapter.DocumentAdapter
import com.example.practicefortoeictestrebuild.base.BaseFragment
import com.example.practicefortoeictestrebuild.databinding.FragmentLessonBinding
import com.example.practicefortoeictestrebuild.ui.course.CourseViewModel
import com.example.practicefortoeictestrebuild.utils.startLoading

class LessonFragment : BaseFragment<FragmentLessonBinding>(FragmentLessonBinding::inflate) {

    private val viewModel by lazy {
        activity?.let {
            ViewModelProvider(it)[LessonViewModel::class.java]
        }
    }

    private val courseViewModel by lazy {
        activity?.let {
            ViewModelProvider(it)[CourseViewModel::class.java]
        }
    }

    private val loadingDialog by lazy { context?.let { Dialog(it) } }

    private val documentAdapter = DocumentAdapter()

    override fun initData() {
        viewModel?.getData()
        viewModel?.updateProgressLesson()
    }

    override fun handleEvent() {
        binding.listDocument.layoutManager = LinearLayoutManager(requireContext())

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun bindData() {
        viewModel?.isLoading?.observe(viewLifecycleOwner) {
            if (it) {
                loadingDialog?.startLoading(false)
            } else {
                loadingDialog?.dismiss()
            }
        }

        viewModel?.documents?.observe(viewLifecycleOwner) {
            documentAdapter.setData(it)
            binding.listDocument.adapter = documentAdapter
        }

        courseViewModel?.updateLesson(viewModel?.lessonId!!.value!!, 1)
    }
}