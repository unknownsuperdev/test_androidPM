package com.name.jat.ui.fragments.explore.geners

import androidx.recyclerview.widget.GridLayoutManager
import com.name.jat.R
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentAllGenresBinding
import com.name.jat.utils.Space
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllGenresFragment : FragmentBaseNCMVVM<AllGenresViewModel, FragmentAllGenresBinding>() {

    override val binding: FragmentAllGenresBinding by viewBinding()
    private var allGenresAdapter = AllGenresAdapter {
        navigateFragment(it)
    }
    override val viewModel: AllGenresViewModel by viewModel()

    override fun onView() {
        val numberOfColumns = 3
        with(binding) {
            mainToolbar.setTitleText(resources.getString(R.string.genres))
            allGenreRv.apply {
                context?.let {
                    layoutManager =
                        GridLayoutManager(context, numberOfColumns)
                    setHasFixedSize(true)
                    adapter = allGenresAdapter
                }
                addItemDecoration(Space(24))
            }
        }
        viewModel.getGenreDataList()
    }

    override fun onViewClick() {
        with(binding) {
            mainToolbar.setOnBackBtnClickListener {
                popBackStack()
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.genreDataList) {
            allGenresAdapter.submitList(it)
        }
    }
}