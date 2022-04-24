package com.wires.app.presentation.settings

import android.os.Bundle
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
        frameLayoutSettingsChangePassword.setOnClickListener {}
        frameLayoutSettingsLogout.setOnClickListener {}
    }

    override fun onBindViewModel() = with(viewModel) {
    }
}
