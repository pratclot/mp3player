package com.pratclot.mp3player.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pratclot.mp3player.R
import com.pratclot.mp3player.databinding.FragmentTrackListBinding
import com.pratclot.mp3player.fragments.utils.AudioListener
import com.pratclot.mp3player.fragments.utils.MyTrackRecyclerViewAdapter
import com.pratclot.mp3player.viewmodels.MainViewModel

/**
 * A fragment representing a list of Items.
 */
class FragmentTrackList : Fragment() {

    private var columnCount = 1

    private val viewModel by activityViewModels<MainViewModel>()

    private lateinit var adapter: MyTrackRecyclerViewAdapter
    private lateinit var binding: FragmentTrackListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_track_list, container, false)

        val layoutManager = LinearLayoutManager(context)
        adapter = MyTrackRecyclerViewAdapter(
            AudioListener {
                viewModel.updateExoPlayerPlayList(it)
                findNavController().navigate(FragmentTrackListDirections.actionFragmentTrackListToFragmentExo())
            })

        binding.list.layoutManager = layoutManager
        binding.list.adapter = adapter
        binding.lifecycleOwner = this

        adapter.submitList(viewModel.audioList.value)

        return binding.root
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            FragmentTrackList().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
