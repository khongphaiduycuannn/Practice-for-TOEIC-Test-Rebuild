package com.example.practicefortoeictestrebuild.ui.vocabulary

import android.animation.ValueAnimator
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.practicefortoeictestrebuild.R
import com.example.practicefortoeictestrebuild.base.BaseFragment
import com.example.practicefortoeictestrebuild.databinding.DialogInternetErrorBinding
import com.example.practicefortoeictestrebuild.databinding.FragmentFlashcardGameBinding
import com.example.practicefortoeictestrebuild.model.FlashcardQuestion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class FlashcardGameFragment :
    BaseFragment<FragmentFlashcardGameBinding>(FragmentFlashcardGameBinding::inflate) {

    private val viewModel by lazy {
        activity?.let {
            ViewModelProvider(it)[VocabularyViewModel::class.java]
        }
    }

    private val gameViewModel by lazy {
        activity?.let {
            ViewModelProvider(it)[FlashcardGameViewModel::class.java]
        }
    }

    private val internetDialog by lazy { Dialog(requireContext()) }

    private val dialogBinding by lazy { DialogInternetErrorBinding.inflate(layoutInflater) }

    private var index = 0

    private var mediaPlayer: MediaPlayer? = null

    private var targetHeight = 0

    override fun initData() {
        initDialog()
        gameViewModel?.startTime = LocalDateTime.now()
    }

    override fun handleEvent() {
        clearCard()
        viewModel?.resetListQuestions()

        binding.questionCard.result.btnContinue.setOnClickListener {
            destroyCurrentCard()
            index++
            val size = viewModel?.questions?.value!!.size
            if (index < size) {
                viewModel?.resetListQuestions()
                setSeekbar()
                clearCard()
            } else {
                findNavController().navigate(R.id.action_flashcardGameFragment_to_flashcardGameResultFragment)
            }
            index = index.coerceAtMost(size - 1)
            viewModel?.resetListQuestions()
        }

        binding.questionCard.btnPlay.setOnClickListener {
            binding.questionCard.imgPlay.setImageResource(R.drawable.ic_stop_button)
            binding.questionCard.btnPlay.isEnabled = false

            mediaPlayer?.apply {
                start()
            }?.setOnCompletionListener {
                binding.questionCard.imgPlay.setImageResource(R.drawable.ic_play_button)
                binding.questionCard.btnPlay.isEnabled = true
            }

            val handler = Handler(Looper.getMainLooper())

            handler.post(object : Runnable {
                override fun run() {
                    val curDuration = mediaPlayer?.currentPosition?.toLong()
                    if (curDuration != null) {
                        val time = String.format(
                            "%02d:%02d ",
                            TimeUnit.MILLISECONDS.toMinutes(curDuration),
                            TimeUnit.MILLISECONDS.toSeconds(curDuration) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(
                                    curDuration
                                )
                            )
                        )
                        binding.questionCard.txtStartTime.text = time
                        binding.questionCard.playbarProgress.progress =
                            TimeUnit.MILLISECONDS.toSeconds(curDuration).toInt()
                    }

                    handler.postDelayed(this, 1000)
                }
            })
        }

        binding.questionCard.answer.aAnswer.setOnClickListener {
            chooseAnswer("A")
            enableChoiceButton(false)
        }

        binding.questionCard.answer.bAnswer.setOnClickListener {
            chooseAnswer("B")
            enableChoiceButton(false)
        }

        binding.questionCard.answer.cAnswer.setOnClickListener {
            chooseAnswer("C")
            enableChoiceButton(false)
        }

        binding.questionCard.answer.dAnswer.setOnClickListener {
            chooseAnswer("D")
            enableChoiceButton(false)
        }

        dialogBinding.btnRetry.setOnClickListener {
            internetDialog.dismiss()
            viewModel?.resetListQuestions()
        }
    }

    override fun bindData() {
        viewModel?.topicName?.observe(viewLifecycleOwner) {
            binding.toolbar.title = it
        }

        binding.seekBar.isEnabled = false
        setSeekbar()

        viewModel?.questions?.observe(viewLifecycleOwner) {
            if (index < viewModel?.questions?.value!!.size) {
                fillCardData(it[index])
                fillCardSize()
            }
        }
    }

    private fun initDialog() {
        internetDialog.setContentView(dialogBinding.root)
        internetDialog.setCancelable(false)
        internetDialog.window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            attributes.apply {
                gravity = Gravity.CENTER
            }
            setDimAmount(0.5F)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    private fun clearCard() {
        val view = binding.questionCard.lnQuestionCard

        val initialHeight = view.height
        targetHeight = initialHeight

        val animator = ValueAnimator.ofInt(initialHeight, 0)

        animator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            val layoutParams = view.layoutParams
            layoutParams.height = value
            view.layoutParams = layoutParams
            view.requestLayout()
        }
        animator.duration = 200
        animator.start()
    }

    private fun fillCardSize() {
        Handler(Looper.getMainLooper()).postDelayed({
            val view = binding.questionCard.lnQuestionCard

            if (targetHeight < 1) {
                view.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
            } else {
                val animator = ValueAnimator.ofInt(0, targetHeight)
                animator.addUpdateListener { valueAnimator ->
                    val value = valueAnimator.animatedValue as Int
                    val layoutParams = view.layoutParams
                    layoutParams.height = value
                    view.layoutParams = layoutParams
                    view.requestLayout()
                }
                animator.duration = 200
                animator.start()
            }
        }, 210)
    }

    private fun fillCardData(card: FlashcardQuestion) {
        binding.questionCard.btnPlay.isEnabled = false
        binding.questionCard.txtTitle.text = "Question ${index + 1}"
        enableChoiceButton(true)
        fillBlankAnswer()

        val image = card.image
        val sound = card.sound
        val content = card.word
        val userChoice = card.userChoice
        var correct = card.correct
        val choices = card.choices

        if (correct == choices[0])
            correct = "A"
        if (correct == choices[1])
            correct = "B"
        if (correct == choices[2])
            correct = "C"
        if (correct == choices[3])
            correct = "D"

        binding.questionCard.result.txtTitleExplanation.visibility = View.GONE
        binding.questionCard.result.txtExplanation.visibility = View.GONE

        if (image.isNullOrEmpty())
            setViewHeight(binding.questionCard.imgImage, 0)
        else {
            fillImage(image)
            setViewHeight(binding.questionCard.imgImage, 500)
        }

        if (sound.isNullOrEmpty())
            setViewHeight(binding.questionCard.lnPlayBar, 0)
        else {
            fillSound(sound)
            setViewHeight(binding.questionCard.lnPlayBar, LinearLayout.LayoutParams.WRAP_CONTENT)
        }

        if (userChoice == 0)
            setViewHeight(binding.questionCard.result.resultArea, 0)
        else {
            enableChoiceButton(false)
            coloredAnswer(userChoice, correct)
            showResult()
        }

        binding.questionCard.txtContent.text = content

        val answer = binding.questionCard.answer
        answer.txtAAnswer.text = choices[0]
        if (choices.size > 1)
            answer.txtBAnswer.text = choices[1]
        if (choices.size > 2)
            answer.txtCAnswer.text = choices[2]
        if (choices.size > 3)
            answer.txtDAnswer.text = choices[3]
    }

    private fun fillImage(link: String) {
        val image = binding.questionCard.imgImage
        Glide.with(image.context).load(link).into(image)
    }

    private fun fillSound(link: String) {
        binding.questionCard.txtStartTime.text = "00:00"
        binding.questionCard.txtEndTime.text = "00:00"

        val media = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA).build()
            )
            setDataSource(link)
            binding.questionCard.lnLoading.alpha = 1.0F
            CoroutineScope(Dispatchers.Default).launch {
                prepare()
            }
        }.setOnPreparedListener {
            binding.questionCard.btnPlay.isEnabled = true
            mediaPlayer = it

            val duration: Long = it.duration.toLong()
            binding.questionCard.playbarProgress.max =
                TimeUnit.MILLISECONDS.toSeconds(duration).toInt()

            binding.questionCard.txtEndTime.text = String.format(
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(duration)
                )
            )
            binding.questionCard.lnLoading.alpha = 0.0F
        }
    }

    private fun fillBlankAnswer() {
        binding.questionCard.answer.aAnswer.setBackgroundResource(R.drawable.ic_choice_unchecked)
        binding.questionCard.answer.bAnswer.setBackgroundResource(R.drawable.ic_choice_unchecked)
        binding.questionCard.answer.cAnswer.setBackgroundResource(R.drawable.ic_choice_unchecked)
        binding.questionCard.answer.dAnswer.setBackgroundResource(R.drawable.ic_choice_unchecked)
    }

    private fun fillResult(flag: Boolean, correct: String) {
        val result = binding.questionCard.result

        result.txtAnswer.text = "Answer: $correct"
        if (flag) {
            result.imgImage.setImageResource(R.drawable.img_hehe)
            result.txtResult.text = "Correct"
            result.txtResult.setTextColor(requireContext().getColor(R.color.green_200))
        } else {
            result.imgImage.setImageResource(R.drawable.img_huhu)
            result.txtResult.text = "Incorrect"
            result.txtResult.setTextColor(requireContext().getColor(carbon.R.color.carbon_red_400))
        }
        showResult()
    }

    private fun showResult() {
        setViewHeight(
            binding.questionCard.result.resultArea,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }

    private fun destroyCurrentCard() {
        mediaPlayer?.reset()
        mediaPlayer?.stop()
    }

    private fun setSeekbar() {
        val size = viewModel?.questions?.value!!.size
        binding.seekBar.max = size
        binding.seekBar.progress = index + 1
        binding.txtProgress.text = "${index + 1}/${size}"
    }

    private fun setViewHeight(view: View, height: Int) {
        view.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, height
        )
    }

    private fun enableChoiceButton(flag: Boolean) {
        binding.questionCard.answer.aAnswer.isEnabled = flag
        binding.questionCard.answer.bAnswer.isEnabled = flag
        binding.questionCard.answer.cAnswer.isEnabled = flag
        binding.questionCard.answer.dAnswer.isEnabled = flag
    }

    private fun chooseAnswer(userChoice: String) {
        val card = viewModel?.questions?.value!![index]
        val choices = card.choices
        var correct = card.correct

        if (correct == choices[0])
            correct = "A"
        if (correct == choices[1])
            correct = "B"
        if (correct == choices[2])
            correct = "C"
        if (correct == choices[3])
            correct = "D"

        when (userChoice) {
            "A" -> card.userChoice = 1
            "B" -> card.userChoice = 2
            "C" -> card.userChoice = 3
            "D" -> card.userChoice = 4
        }

        coloredAnswer(card.userChoice, correct)

        if (userChoice != correct) {
            gameViewModel?.incorrectCount?.value = gameViewModel?.incorrectCount?.value!! + 1
        } else {
            gameViewModel?.correctCount?.value = gameViewModel?.correctCount?.value!! + 1
        }
    }

    private fun coloredAnswer(userChoice: Int, correct: String) {
        var choice = ""
        when (userChoice) {
            1 -> {
                choice = "A"
                binding.questionCard.answer.aAnswer.setBackgroundResource(R.drawable.ic_choice_wrong)
            }
            2 -> {
                choice = "B"
                binding.questionCard.answer.bAnswer.setBackgroundResource(R.drawable.ic_choice_wrong)
            }
            3 -> {
                choice = "C"
                binding.questionCard.answer.cAnswer.setBackgroundResource(R.drawable.ic_choice_wrong)
            }
            4 -> {
                choice = "D"
                binding.questionCard.answer.dAnswer.setBackgroundResource(R.drawable.ic_choice_wrong)
            }
        }

        when (correct) {
            "A" -> binding.questionCard.answer.aAnswer.setBackgroundResource(R.drawable.ic_choice_correct)
            "B" -> binding.questionCard.answer.bAnswer.setBackgroundResource(R.drawable.ic_choice_correct)
            "C" -> binding.questionCard.answer.cAnswer.setBackgroundResource(R.drawable.ic_choice_correct)
            "D" -> binding.questionCard.answer.dAnswer.setBackgroundResource(R.drawable.ic_choice_correct)
        }

        fillResult(correct == choice, correct)
    }
}