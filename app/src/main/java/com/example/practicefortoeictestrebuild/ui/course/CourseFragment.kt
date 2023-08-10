package com.example.practicefortoeictestrebuild.ui.course

import android.app.Dialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practicefortoeictestrebuild.adapter.CourseAdapter
import com.example.practicefortoeictestrebuild.base.BaseFragment
import com.example.practicefortoeictestrebuild.databinding.FragmentCourseBinding
import com.example.practicefortoeictestrebuild.utils.startLoading

class CourseFragment : BaseFragment<FragmentCourseBinding>(FragmentCourseBinding::inflate) {

    private val viewModel by lazy {
        activity?.let {
            ViewModelProvider(it)[CourseViewModel::class.java]
        }
    }

    private val loadingDialog by lazy { context?.let { Dialog(it) } }

    private val courseAdapter = CourseAdapter(this)

    override fun initData() {
        viewModel?.setTitle(requireActivity().intent.getStringExtra("title"))
        viewModel?.setGroup(requireActivity().intent.getStringExtra("group"))
        viewModel?.getData()
    }

    override fun handleEvent() {
        binding.listCourse.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun bindData() {
        viewModel?.isLoading?.observe(viewLifecycleOwner) {
            if (it) {
                loadingDialog?.startLoading(false)
            } else {
                loadingDialog?.dismiss()
            }
        }

        viewModel?.title?.observe(viewLifecycleOwner) {
            binding.toolbar.title = it
        }

        viewModel?.listCourse?.observe(viewLifecycleOwner) {
            courseAdapter.setData(it)
            binding.listCourse.adapter = courseAdapter
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel?.getData()
    }
}