package com.example.practicefortoeictestrebuild.ui.vocabulary

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Path
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.practicefortoeictestrebuild.R
import com.example.practicefortoeictestrebuild.base.BaseFragment
import com.example.practicefortoeictestrebuild.databinding.FragmentFlashcardLearnBinding
import com.example.practicefortoeictestrebuild.databinding.ViewVocabularyFlashcardFirstBinding
import com.example.practicefortoeictestrebuild.databinding.ViewVocabularyFlashcardSecondBinding
import com.example.practicefortoeictestrebuild.databinding.ViewVocabularyFlashcardThirdBinding
import com.example.practicefortoeictestrebuild.model.FlashCard

class FlashcardLearnFragment :
    BaseFragment<FragmentFlashcardLearnBinding>(FragmentFlashcardLearnBinding::inflate) {

    private val viewModel by lazy {
        activity?.let {
            ViewModelProvider(it)[VocabularyViewModel::class.java]
        }
    }

    private var isFirstFront = true
    private var isAllFront = true

    private lateinit var animatorFront: AnimatorSet
    private lateinit var animatorBack: AnimatorSet
    private lateinit var animatorAppear: AnimatorSet
    private lateinit var animatorDisappear: AnimatorSet

    private lateinit var cardFirst: ViewVocabularyFlashcardFirstBinding
    private lateinit var cardSecond: ViewVocabularyFlashcardSecondBinding
    private lateinit var cardThird: ViewVocabularyFlashcardThirdBinding

    private var screenWidth = 0F
    private var dX = 0F
    private var dY = 0F
    private var oX = 0F
    private var oY = 0F

    private var index = -1
    private var countCard = -1

    private var memorizedCount = 0
    private var unmemorizedCount = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cardFirst = binding.cardFirst
        cardSecond = binding.cardSecond
        cardThird = binding.cardThird
        viewModel?.listCard?.observe(viewLifecycleOwner) {
            index = it.size - 1
            countCard = it.size
            setSeekbar()
            loadNewCard()
        }

        setAnimator()
        setPosition()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initData() {

    }

    override fun handleEvent() {
        binding.seekbar.isEnabled = false
        cardThird.btnNextTopic.isEnabled = false
        onCardTouchListener()
        onSoundClickListener()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel?.topicName?.value != "Flashcard Daily")
                    findNavController().popBackStack()
                else findNavController().navigate(R.id.action_flashcardLearnFragment2_to_calendarFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.toolbar.setNavigationOnClickListener {
            if (viewModel?.topicName?.value != "Flashcard Daily")
                findNavController().popBackStack()
            else findNavController().navigate(R.id.action_flashcardLearnFragment2_to_calendarFragment)
        }

        binding.cardThird.btnNextTopic.setOnClickListener {
            if (viewModel?.topicName?.value != "Flashcard Daily")
                findNavController().popBackStack()
            else findNavController().navigate(R.id.action_flashcardLearnFragment2_to_calendarFragment)
        }
    }

    override fun bindData() {
        viewModel?.topicName?.observe(viewLifecycleOwner) {
            binding.toolbar.title = it
        }

        viewModel?.memorizedCard?.observe(viewLifecycleOwner) {
            binding.txtMemorizedCount.text = "$memorizedCount"
        }

        viewModel?.unmemorizedCard?.observe(viewLifecycleOwner) {
            binding.txtUnmemorizedCount.text = "$unmemorizedCount"
        }
    }

    private fun setSeekbar() {
        binding.seekbar.max = countCard
        binding.seekbar.progress = countCard - index
        binding.txtCountAnswer.text = "${(countCard - index).coerceAtMost(countCard)}/$countCard"
    }

    private fun setAnimator() {
        val scale: Float = requireContext().resources.displayMetrics.density
        cardFirst.cardFirst.cameraDistance = 5000 * scale

        animatorFront = AnimatorInflater.loadAnimator(
            requireContext(), R.animator.animator_front
        ) as AnimatorSet

        animatorBack = AnimatorInflater.loadAnimator(
            requireContext(), R.animator.animator_back
        ) as AnimatorSet

        animatorDisappear = AnimatorInflater.loadAnimator(
            requireContext(), R.animator.animator_disappear
        ) as AnimatorSet
        animatorDisappear.setTarget(cardFirst.cardFirst)

        animatorAppear = AnimatorInflater.loadAnimator(
            requireContext(), R.animator.animator_appear
        ) as AnimatorSet
        animatorAppear.setTarget(cardFirst.cardFirst)
        animatorAppear.startDelay = 700
    }

    private fun setPosition() {
        delay(100) {
            oX = cardFirst.cardFirst.x
            oY = cardFirst.cardFirst.y
            screenWidth = binding.flashCardScreen.width.toFloat()
        }
    }

    private fun loadNewCard() {
        val listCard = viewModel?.listCard?.value!!
        isFirstFront = !isAllFront
        flipCard()
        if (index < 0) {
            cardFirst.cardFirst.isEnabled = false
            cardThird.btnNextTopic.isEnabled = true

            moveCardTo(cardFirst.cardFirst, 0F, 0F, -500F, -500F, 400)
            cardFirst.cardFirst.alpha = 0F
            cardSecond.cardSecond.alpha = 0F
            return
        }

        if (index == 0) {
            delay(800) {
                cardFirst.cardFirst.alpha = 1F
                cardSecond.cardSecond.alpha = 0F
            }
            delay(300) {
                loadFirstCard(listCard[index])
            }
            return
        }

        cardFirst.cardFirst.alpha = 1F
        cardSecond.cardSecond.alpha = 1F
        delay(300) {
            loadFirstCard(listCard[index])
        }
        delay(800) {
            val x = index
            loadSecondCard(listCard[index - 1])
        }
    }

    private fun loadFirstCard(card: FlashCard) {
        cardFirst.txtStatusBar.background = ContextCompat.getDrawable(
            requireContext(), R.drawable.bg_status_normal
        )
        cardFirst.txtStatusBar.text = ""
        Glide.with(cardFirst.imgImage.context).load(card.image).into(cardFirst.imgImage)

        cardFirst.txtFront.text = card.textFront
        cardFirst.txtBack.text = card.textBack
        cardFirst.txtIpa.text = card.ipa
        cardFirst.txtHint.text = card.hint
    }

    private fun loadSecondCard(card: FlashCard) {
        Glide.with(cardSecond.imgImage.context).load(card.image).into(binding.cardSecond.imgImage)

        cardSecond.txtFront.text = card.textFront
        cardSecond.txtBack.text = card.textBack
        cardSecond.txtIpa.text = card.ipa
        cardSecond.txtHint.text = card.hint
    }

    private fun flipCard() {
        if (isFirstFront) {
            animatorFront.setTarget(cardFirst.clFrontSide)
            animatorBack.setTarget(cardFirst.clBackSide)
        } else {
            animatorFront.setTarget(cardFirst.clBackSide)
            animatorBack.setTarget(cardFirst.clFrontSide)
        }

        cardFirst.clFrontSide.elevation = 0F
        cardFirst.clBackSide.elevation = 0F
        animatorFront.start()
        animatorBack.start()

        delay(480) {
            cardFirst.clFrontSide.elevation = 6F
            cardFirst.clBackSide.elevation = 6F
        }
        isFirstFront = !isFirstFront
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onCardTouchListener() {
        cardFirst.cardFirst.setOnTouchListener { view, event ->

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = view.x - event.rawX
                    dY = view.y - event.rawY
                }

                MotionEvent.ACTION_UP -> {
                    var delay = 0L
                    if (view.x - 5 <= oX && oX <= view.x + 5 && view.y - 5 <= oY && oY <= view.y + 5) {
                        flipCard()
                    } else {
                        if (view.x >= oX + screenWidth / 2.5 || view.x <= oX - screenWidth / 2.5) {
                            delay = 300
                            animatorDisappear.start()
                            animatorAppear.start()

                            if (index >= 0) {
                                val listCard = viewModel?.listCard?.value!!
                                if (view.x >= oX + screenWidth / 2.5) {
                                    memorizedCount++
                                    viewModel?.updateCard(listCard[index].id, "true")
                                    viewModel?.memorizeCard(listCard[index])
                                } else {
                                    unmemorizedCount++
                                    viewModel?.updateCard(listCard[index].id, "false")
                                    viewModel?.unMemorizeCard(listCard[index])
                                }
                                index--
                                loadNewCard()
                                setSeekbar()
                            }
                        }
                        cardFirst.cardFirst.isEnabled = false
                        moveCardTo(view, view.x, view.y, oX, oY, delay)
                    }
                }

                MotionEvent.ACTION_MOVE -> {
                    view.animate().x(event.rawX + dX).y(event.rawY + dY).setDuration(0).start()
                    setStatusBar(view.x)
                }
            }
            true
        }
    }

    private fun moveCardTo(
        view: View, fromX: Float, fromY: Float, toX: Float, toY: Float, delay: Long
    ) {
        val path = Path().apply {
            moveTo(fromX, fromY)
            lineTo(toX, toY)
        }

        ObjectAnimator.ofFloat(view, View.X, View.Y, path).apply {
            startDelay = delay
            duration = 400
            start()
        }

        delay(500 + delay) {
            cardFirst.cardFirst.isEnabled = true
        }
    }

    private fun setStatusBar(posX: Float) {
        if (posX >= oX + screenWidth / 2.5) {
            cardFirst.txtStatusBar.background = ContextCompat.getDrawable(
                requireContext(), R.drawable.bg_status_memorized
            )
            cardFirst.txtStatusBar.text = "GOT IT"
            return
        }

        if (posX <= oX - screenWidth / 2.5) {
            cardFirst.txtStatusBar.background = ContextCompat.getDrawable(
                requireContext(), R.drawable.bg_status_unmemorized
            )
            cardFirst.txtStatusBar.text = "STUDY AGAIN"
            return
        }

        binding.cardFirst.txtStatusBar.background = ContextCompat.getDrawable(
            requireContext(), R.drawable.bg_status_normal
        )
        cardFirst.txtStatusBar.text = ""
    }

    private fun onSoundClickListener() {
        cardFirst.imgSound.setOnClickListener {
            val listCard = viewModel?.listCard?.value!!
            if (index >= 0) {
                val card = listCard[index]
                cardFirst.imgSound.setImageResource(R.drawable.ic_sound_max)
                cardFirst.imgSound.isEnabled = false

                startMediaPlayer(card.sound) {
                    binding.cardFirst.imgSound.setImageResource(R.drawable.ic_sound_min)
                    binding.cardFirst.imgSound.isEnabled = true
                }
            }
        }
    }

    private fun startMediaPlayer(sound: String, onCompleted: () -> Unit) {
        val mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(sound)
            prepare()
            start()
        }.setOnCompletionListener {
            onCompleted()
        }
    }

    private fun delay(time: Long, func: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            func()
        }, time)
    }
}