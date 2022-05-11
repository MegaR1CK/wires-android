package com.wires.app.presentation.settings

import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.databinding.FragmentSettingsBinding
import com.wires.app.extensions.fitTopInsetsWithPadding
import com.wires.app.extensions.navigateBack
import com.wires.app.extensions.navigateTo
import com.wires.app.extensions.restartApp
import com.wires.app.extensions.showAlertDialog
import com.wires.app.presentation.base.BaseFragment
import com.yariksoffice.lingver.Lingver

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    companion object {
        private const val LOCALE_ENGLISH = "en"
        private const val LOCALE_ENGLISH_COUNTRY = "EN"
        private const val LOCALE_RUSSIAN = "ru"
        private const val LOCALE_RUSSIAN_COUNTRY = "RU"
    }

    private val binding by viewBinding(FragmentSettingsBinding::bind)
    private val viewModel: SettingsViewModel by appViewModels()

    override fun callOperations() = Unit

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitTopInsetsWithPadding()
        frameLayoutSettingsChangePassword.setOnClickListener { viewModel.openChangePassword() }
        registerForContextMenu(frameLayoutSettingsLocale)
        frameLayoutSettingsLocale.setOnClickListener { it.showContextMenu() }
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

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        requireActivity().menuInflater.inflate(R.menu.menu_change_locale, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.locale_english -> Lingver.getInstance().setLocale(requireContext(), LOCALE_ENGLISH, LOCALE_ENGLISH_COUNTRY)
            R.id.locale_russian -> Lingver.getInstance().setLocale(requireContext(), LOCALE_RUSSIAN, LOCALE_RUSSIAN_COUNTRY)
            else -> return false
        }
        requireActivity().restartApp()
        return true
    }
}
