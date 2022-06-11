package com.wires.app.presentation.settings

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.data.model.User
import com.wires.app.databinding.FragmentSettingsBinding
import com.wires.app.extensions.copyToClipboard
import com.wires.app.extensions.createLoadableResultDialog
import com.wires.app.extensions.fitTopInsetsWithPadding
import com.wires.app.extensions.navigateBack
import com.wires.app.extensions.navigateTo
import com.wires.app.extensions.showAlertDialog
import com.wires.app.extensions.showSnackbar
import com.wires.app.extensions.showToast
import com.wires.app.managers.LocaleManager
import com.wires.app.presentation.base.BaseFragment
import timber.log.Timber
import javax.inject.Inject

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    companion object {
        private const val MAIL_URL_PREFIX = "mailto:"
    }

    private val binding by viewBinding(FragmentSettingsBinding::bind)
    private val viewModel: SettingsViewModel by appViewModels()

    @Inject lateinit var localeManager: LocaleManager

    override fun callOperations() {
        viewModel.getUser()
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitTopInsetsWithPadding()
        frameLayoutSettingsChangePassword.setOnClickListener { viewModel.openChangePassword() }
        registerForContextMenu(frameLayoutSettingsLocale)
        frameLayoutSettingsLocale.setOnClickListener { it.showContextMenu() }
        frameLayoutSettingsReport.setOnClickListener { viewModel.openMailClient() }
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
        userLiveData.observe { result ->
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }
        openMailClientLiveEvent.observe { wrapper ->
            val intent = createMailIntent(wrapper.user)
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            } else {
                requireContext().copyToClipboard(getString(R.string.settings_report_email))
                showToast(getString(R.string.settings_report_copied))
            }
        }
        openChangePasswordLiveEvent.observe {
            navigateTo(SettingsFragmentDirections.actionSettingsFragmentToChangePasswordFragment())
        }
        val loadableResultDialog = createLoadableResultDialog()
        logoutLiveEvent.observe { result ->
            loadableResultDialog.setState(result)
            result.doOnSuccess {
                navigateTo(SettingsFragmentDirections.actionSettingsFragmentToNavGraph(needSkipAnimation = true))
            }
            result.doOnFailure { error ->
                showSnackbar(error.message)
                Timber.e(error.message)
            }
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        requireActivity().menuInflater.inflate(R.menu.menu_change_locale, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        localeManager.setLocale(
            requireActivity(),
            when (item.itemId) {
                R.id.locale_russian -> LocaleManager.AvailableLocale.RUSSIAN
                R.id.locale_english -> LocaleManager.AvailableLocale.ENGLISH
                else -> return false
            }
        )
        return true
    }

    private fun createMailIntent(user: User?): Intent {
        return Intent(Intent.ACTION_SENDTO).apply {
            val messageBody = StringBuilder(getString(R.string.settings_report_warning))
            if (user != null && user.username.isNotEmpty()) {
                messageBody.append(getString(R.string.settings_report_username, user.username))
            }
            messageBody.append(getString(R.string.settings_report_version, Build.VERSION.SDK_INT))
            messageBody.append(getString(R.string.settings_report_device, Build.MODEL))
            messageBody.append(getString(R.string.settings_report_description))
            data = android.net.Uri.parse(MAIL_URL_PREFIX)
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.settings_report_email)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.settings_report_title))
            putExtra(Intent.EXTRA_TEXT, messageBody.toString())
        }
    }
}
