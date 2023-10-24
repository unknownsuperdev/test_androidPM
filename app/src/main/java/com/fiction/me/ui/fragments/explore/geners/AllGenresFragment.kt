package com.fiction.me.ui.fragments.explore.geners

import androidx.recyclerview.widget.GridLayoutManager
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentAllGenresBinding
import com.fiction.me.utils.Space
import com.fiction.me.utils.Events.Companion.GENRE_CLICKED
import com.fiction.me.utils.Events.Companion.GENRE_NAME
import com.fiction.me.utils.Events.Companion.GENRE_SCREEN
import com.fiction.me.utils.Events.Companion.PLACEMENT
import com.fiction.me.utils.cacheImages
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllGenresFragment : FragmentBaseNCMVVM<AllGenresViewModel, FragmentAllGenresBinding>() {

    override val binding: FragmentAllGenresBinding by viewBinding()
    private var allGenresAdapter = AllGenresAdapter { direction, title ->
        viewModel.trackEvents(
            GENRE_CLICKED,
            hashMapOf(GENRE_NAME to title, PLACEMENT to GENRE_SCREEN)
        )
        navigateFragment(direction)
    }
    override val viewModel: AllGenresViewModel by viewModel()

    override fun onView() {
        val numberOfColumns = 3
        with(binding) {
            mainToolbar.run {
                setTitleText(resources.getString(R.string.genres))
                setBackBtnIcon(R.drawable.ic_back)
            }
            allGenreRv.apply {
                context?.let {
                    layoutManager =
                        GridLayoutManager(context, numberOfColumns)
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
            it?.forEach { context?.let { it1 -> cacheImages(it1, it.icon) } }
            allGenresAdapter.submitList(it)
        }
    }
}