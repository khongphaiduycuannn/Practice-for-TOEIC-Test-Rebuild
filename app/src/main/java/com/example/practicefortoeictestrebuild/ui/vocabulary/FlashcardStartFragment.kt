package com.example.practicefortoeictestrebuild.ui.vocabulary

import android.app.Dialog
import android.os.Handler
import android.os.Looper
import android.widget.ArrayAdapter
import androidx.core.view.marginStart
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.practicefortoeictestrebuild.R
import com.example.practicefortoeictestrebuild.base.BaseFragment
import com.example.practicefortoeictestrebuild.databinding.FragmentFlashcardStartBinding
import com.example.practicefortoeictestrebuild.utils.PopUpNotification
import com.example.practicefortoeictestrebuild.utils.startLoading

class FlashcardStartFragment :
    BaseFragment<FragmentFlashcardStartBinding>(FragmentFlashcardStartBinding::inflate) {

    private val viewModel by lazy {
        activity?.let {
            ViewModelProvider(it)[VocabularyViewModel::class.java]
        }
    }

    private val loadingDialog by lazy { context?.let { Dialog(it) } }

    override fun onStart() {
        super.onStart()
        initDropdownMenu()
    }

    override fun initData() {
        viewModel?.getData()
    }

    override fun handleEvent() {
        binding.seekbar.seekbar.isEnabled = false

        binding.btnLearn.setOnClickListener {
            if (viewModel?.listCard?.value.isNullOrEmpty()) {
                showDialog()
            } else findNavController().navigate(R.id.action_flashcardStartFragment_to_flashcardLearnFragment)
        }

        binding.btnPlayGame.setOnClickListener {
            findNavController().navigate(R.id.action_flashcardStartFragment_to_flashcardGameFragment)
        }

        binding.dropTextCategory.setOnItemClickListener { parent, _, position, _ ->
            val item = parent.getItemAtPosition(position).toString()
            viewModel?.setListCard(item)
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

        viewModel?.topicName?.observe(viewLifecycleOwner) {
            binding.toolbar.title = it
        }

        viewModel?.allCard?.observe(viewLifecycleOwner) {
            binding.txtTotal.text = "Total ${it.size} cards"
        }

        viewModel?.newCard?.observe(viewLifecycleOwner) {
            binding.informationArea.txtNewAmount.text = "${it.size}"
        }

        viewModel?.memorizedCard?.observe(viewLifecycleOwner) {
            binding.informationArea.txtMemorizedAmount.text = "${it.size}"
            setSeekbar(it.size, viewModel?.allCard?.value!!.size)
        }

        viewModel?.unmemorizedCard?.observe(viewLifecycleOwner) {
            binding.informationArea.txtUnmemorizedAmount.text = "${it.size}"
        }
    }

    private fun initDropdownMenu() {
        val learns = resources.getStringArray(R.array.vocabulary_learn)
        val category = resources.getStringArray(R.array.vocabulary_category)
        val learnAdapter =
            ArrayAdapter(requireContext(), R.layout.item_dropdown_vocabulary_learn, learns)
        val categoryAdapter =
            ArrayAdapter(requireContext(), R.layout.item_dropdown_vocabulary_category, category)

        binding.dropTextLearn.setAdapter(learnAdapter)
        binding.dropTextCategory.setAdapter(categoryAdapter)
    }

    private fun showDialog() {
        val popUpNotification = PopUpNotification(requireContext())
        popUpNotification.title.text = "Notification"
        popUpNotification.message.text = "This topic doesn't have any question!"
        popUpNotification.show(requireView())
    }

    private fun setSeekbar(amount: Int, total: Int) {
        if (total <= 0) return
        val seekbar = binding.seekbar.seekbar
        seekbar.max = total
        seekbar.progress = amount
        Handler(Looper.getMainLooper()).postDelayed({
            val width = seekbar.width - 2 * seekbar.paddingStart
            val thumbPos =
                binding.linearLayout.marginStart + seekbar.paddingStart + 1.0 * width * amount / total - 85
            binding.seekbar.progress.text = "${(100.0 * amount / total).toInt()}%"
            binding.seekbar.clThumb.x = thumbPos.toFloat()
        }, 100)
    }
}