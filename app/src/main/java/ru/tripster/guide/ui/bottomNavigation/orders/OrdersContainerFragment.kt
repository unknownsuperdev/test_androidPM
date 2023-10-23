package ru.tripster.guide.ui.bottomNavigation.orders

import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.tripster.guide.appbase.FragmentBaseNCMVVM
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.databinding.FragmentOrdersContainerBinding

class OrdersContainerFragment : FragmentBaseNCMVVM<OrdersContainerViewModel, FragmentOrdersContainerBinding>() {

    override val viewModel: OrdersContainerViewModel by viewModel()
    override val binding: FragmentOrdersContainerBinding by viewBinding()

}