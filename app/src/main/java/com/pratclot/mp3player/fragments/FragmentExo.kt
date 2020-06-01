package com.pratclot.mp3player.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.pratclot.mp3player.Mp3Player
import com.pratclot.mp3player.R
import com.pratclot.mp3player.databinding.FragmentExoBinding
import com.pratclot.mp3player.viewmodels.MainViewModel
import com.pratclot.mp3player.viewmodels.MainViewModelFactory
import javax.inject.Inject

const val PERMISSION_REQUEST_CODE = 12345

class FragmentExo : Fragment() {
    private lateinit var binding: FragmentExoBinding

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    private val viewModel by activityViewModels<MainViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        checkPermissions()

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_exo,
            container,
            false
        )
        binding.playerView.player = viewModel.exoPlayer

        return binding.root
    }

    private fun checkPermissions() {
        val check = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        when (check) {
            PackageManager.PERMISSION_GRANTED -> Unit
            PackageManager.PERMISSION_DENIED ->
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        requireActivity().isChangingConfigurations
        newConfig.orientation
        super.onConfigurationChanged(newConfig)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.reinit()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController()) ||
                super.onOptionsItemSelected(item)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as Mp3Player).appComponent.injectFragment(this)
    }
}
