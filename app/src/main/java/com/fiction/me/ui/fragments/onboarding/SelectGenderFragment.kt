package com.fiction.me.ui.fragments.onboarding

import android.content.Context
import android.util.Log
import androidx.activity.addCallback
import androidx.navigation.fragment.navArgs
import com.analytics.api.Events.USER_PROPERTY__USER_GENDER
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentSelectGenderBinding
import com.fiction.me.ui.MainActivity
import com.fiction.me.utils.Events.Companion.FEMALE
import com.fiction.me.utils.Events.Companion.GENDER
import com.fiction.me.utils.Events.Companion.GENDER_SCREEN_CONTINUE_BUTTON_CLICKED
import com.fiction.me.utils.Events.Companion.GENDER_SCREEN_SHOWN
import com.fiction.me.utils.Events.Companion.MALE
import com.fiction.me.utils.Events.Companion.OTHER
import com.google.android.play.core.review.ReviewManagerFactory
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectGenderFragment :
    FragmentBaseNCMVVM<SelectGenderViewModel, FragmentSelectGenderBinding>() {
    override val binding: FragmentSelectGenderBinding by viewBinding()
    override val viewModel: SelectGenderViewModel by viewModel()
    var selectedGenreId: Int? = null
    val args: SelectGenderFragmentArgs by navArgs()
    private val maleId = 1
    private val femaleId = 2
    private val otherId = 3

    override fun onView() {
        context?.let { showRateDialog(it) }
    }

    override fun onViewClick() {
        viewModel.run {
            getGenders()
            getOnBoardingSetting()
            trackEvents(GENDER_SCREEN_SHOWN)
        }
        with(binding) {
            male.setOnClickListener {
                selectGenre(Genre.MALE)
            }
            female.setOnClickListener {
                selectGenre(Genre.FEMALE)
            }
            other.setOnClickListener {
                selectGenre(Genre.OTHER)
            }
            continueBtn.setOnClickListener {
                sendEvent()
                val screens = args.screens.filterIndexed { index, s -> index != 0 }.toTypedArray()
                viewModel.updateGenderSelection(selectedGenreId)
                val directions =
                    SelectGenderFragmentDirections.actionSelectGenderFragmentToPickReadingTimeFragment(
                        screens
                    )
                navigateFragment(directions)
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(this) {
            //activity?.finish()
        }
    }

    override fun onEach() {
        onEach(viewModel.genders) { genders ->
            with(binding) {
                genders?.let { gender ->
                    gender.forEach {
                        when (it.id) {
                            maleId -> maleTxt.text = it.name
                            femaleId -> femaleTxt.text = it.name
                            otherId -> otherTxt.text = it.name
                        }
                    }
                }
            }
        }

        onEach(viewModel.selectedGenderId) {
            with(binding) {
                selectedGenreId = it
                when (it) {
                    maleId -> {
                        changeGenderSelection(isMale = true)
                    }
                    femaleId -> changeGenderSelection(isFemale = true)
                    otherId -> changeGenderSelection(isOther = true)
                }
            }
        }
    }

    private fun selectGenre(genre: Genre) {
        when (genre) {
            Genre.MALE -> {
                changeGenderSelection(isMale = true)
                selectedGenreId = maleId
                viewModel.insertOnBoardingSetting(maleId)
            }
            Genre.FEMALE -> {
                changeGenderSelection(isFemale = true)
                selectedGenreId = femaleId
                viewModel.insertOnBoardingSetting(femaleId)
            }
            Genre.OTHER -> {
                changeGenderSelection(isOther = true)
                selectedGenreId = otherId
                viewModel.insertOnBoardingSetting(otherId)
            }
        }
    }

    private fun changeGenderSelection(
        isMale: Boolean = false,
        isFemale: Boolean = false,
        isOther: Boolean = false
    ) {
        with(binding) {
            male.isSelected = isMale
            female.isSelected = isFemale
            other.isSelected = isOther

            if (!continueBtn.isEnabled) {
                continueBtn.isEnabled = true
            }
        }
    }

    private fun sendEvent() {
        val selectedGender = when (selectedGenreId) {
            maleId -> MALE
            femaleId -> FEMALE
            else -> OTHER
        }
        viewModel.setUserPropertyEvent(mapOf(USER_PROPERTY__USER_GENDER to selectedGender))
        viewModel.trackEvents(
            GENDER_SCREEN_CONTINUE_BUTTON_CLICKED,
            hashMapOf(GENDER to selectedGender)
        )
    }

    private fun showRateDialog(context: Context) {
        val manager = ReviewManagerFactory.create(context)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                val flow = manager.launchReviewFlow(context as MainActivity, reviewInfo)
                flow.addOnCompleteListener { _ ->
                    Log.d("RateFailed", "The review flow has finished $reviewInfo ")
                }
            } else {
                Log.d("RateFailed", "Failed to request review flow")
            }
        }
    }
}