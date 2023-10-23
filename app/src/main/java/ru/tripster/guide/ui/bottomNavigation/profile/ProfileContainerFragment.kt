package ru.tripster.guide.ui.bottomNavigation.profile

import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.tripster.guide.appbase.FragmentBaseNCMVVM
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.databinding.FragmentProfileContainerBinding

class ProfileContainerFragment : FragmentBaseNCMVVM<ProfileContainerViewModel, FragmentProfileContainerBinding>() {

    override val viewModel: ProfileContainerViewModel by viewModel()
    override val binding: FragmentProfileContainerBinding by viewBinding()
}