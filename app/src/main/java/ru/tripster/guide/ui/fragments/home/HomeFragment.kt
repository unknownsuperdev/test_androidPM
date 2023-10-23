package ru.tripster.guide.ui.fragments.home

import androidx.recyclerview.widget.LinearLayoutManager
import ru.tripster.guide.appbase.*
import ru.tripster.guide.appbase.utils.*
import ru.tripster.guide.databinding.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : FragmentBaseNCMVVM<HomeViewModel, FragmentHomeBinding>() {

    override val viewModel: HomeViewModel by viewModel()
    override val binding: FragmentHomeBinding by viewBinding()

    private var imagesAdapter = DataInfoListAdapter()

    override fun onView() {
        with(binding) {
            rvData.apply {
                context?.let {
                    layoutManager = LinearLayoutManager(it)
                    setHasFixedSize(true)
                    adapter = imagesAdapter
                }
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.list) {
            imagesAdapter.submitList(it)
        }

        onEach(viewModel.goToFragment) {

        }
    }

    override fun onViewClick() {
        binding.submit.setOnClickListener {
            viewModel.setValue()
            // navigateFragment(R.id.navigation_test)
        }
    }
}