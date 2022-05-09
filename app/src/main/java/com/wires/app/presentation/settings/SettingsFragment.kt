package com.wires.app.presentation.settings

import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.databinding.FragmentSettingsBinding
import com.wires.app.extensions.fitTopInsetsWithPadding
import com.wires.app.extensions.navigateBack
import com.wires.app.extensions.navigateTo
import com.wires.app.extensions.showAlertDialog
import com.wires.app.presentation.base.BaseFragment

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private val binding by viewBinding(FragmentSettingsBinding::bind)
    private val viewModel: SettingsViewModel by appViewModels()

    override fun callOperations() = Unit

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitTopInsetsWithPadding()
        frameLayoutSettingsChangePassword.setOnClickListener { viewModel.openChangePassword() }
        frameLayoutSettingsLogout.setOnClickListener {
            showAlertDialog(
                titleRes = R.string.settings_logout,
                messageRes = R.string.settings_logout_message,
                positiveButtonTextRes = R.string.settings_logout_positive_text,
                negativeButtonTextRes = R.string.settings_logout_negative_text,
                positiveButtonListener = { _, _ ->
                    viewModel.logout()
                }
            )
        }
        toolbarSettings.setNavigationOnClickListener { navigateBack() }
    }

    override fun onBindViewModel() = with(viewModel) {
        openChangePasswordLiveEvent.observe {
            navigateTo(SettingsFragmentDirections.actionSettingsFragmentToChangePasswordFragment())
        }
        logoutLiveEvent.observe {
            navigateTo(SettingsFragmentDirections.actionSettingsFragmentToAuthGraph())
        }
    }
}
