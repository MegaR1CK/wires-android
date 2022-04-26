package com.wires.app.presentation.settings

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.databinding.FragmentSettingsBinding
import com.wires.app.extensions.fitTopInsetsWithPadding
import com.wires.app.presentation.base.BaseFragment

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private val binding by viewBinding(FragmentSettingsBinding::bind)
    private val viewModel: SettingsViewModel by appViewModels()

    override fun callOperations() = Unit

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitTopInsetsWithPadding()
        frameLayoutSettingsChangePassword.setOnClickListener { viewModel.openChangePassword() }
        frameLayoutSettingsLogout.setOnClickListener { showLogoutDialog() }
        toolbarSettings.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    override fun onBindViewModel() = with(viewModel) {
        openChangePasswordLiveEvent.observe {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToChangePasswordFragment())
        }
        logoutLiveEvent.observe {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToAuthGraph())
        }
    }

    private fun showLogoutDialog() = AlertDialog.Builder(requireContext())
        .setTitle(getString(R.string.settings_logout))
        .setMessage(getString(R.string.settings_logout_message))
        .setPositiveButton(getString(R.string.settings_logout_positive_text)) { _, _ ->
            viewModel.logout()
        }
        .setNegativeButton(getString(R.string.settings_logout_negative_text)) { _, _ -> }
        .create()
        .show()
}
