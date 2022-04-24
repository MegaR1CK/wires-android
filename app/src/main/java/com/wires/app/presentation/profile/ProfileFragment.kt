package com.wires.app.presentation.profile

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.appbar.AppBarLayout
import com.wires.app.R
import com.wires.app.data.model.User
import com.wires.app.databinding.FragmentProfileBinding
import com.wires.app.domain.paging.PagingLoadStateAdapter
import com.wires.app.extensions.addVerticalDividerItemDecoration
import com.wires.app.extensions.fitTopInsetsWithPadding
import com.wires.app.extensions.getColorAttribute
import com.wires.app.extensions.load
import com.wires.app.presentation.base.BaseFragment
import com.wires.app.presentation.edituser.EditUserFragment
import com.wires.app.presentation.feed.feedchild.PostsAdapter
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel: ProfileViewModel by appViewModels()
    private val args: ProfileFragmentArgs by navArgs()

    override val showBottomNavigationView = true

    @Inject lateinit var postsAdapter: PostsAdapter

    override fun callOperations() {
        viewModel.getUser(args.userId)
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        appBarLayoutProfile.fitTopInsetsWithPadding()
        toolbarProfile.setNavigationOnClickListener { findNavController().popBackStack() }
        stateViewFlipperProfile.setRetryMethod { callOperations() }
        buttonProfileEdit.setOnClickListener { viewModel.openEditUser() }
        buttonProfileSettings.setOnClickListener { viewModel.openSettings() }
        setupAppbar()
        setupPostsList()
        setFragmentResultListener(EditUserFragment.USER_UPDATED_RESULT_KEY) { _, _ ->
            callOperations()
        }
    }

    override fun onBindViewModel() = with(viewModel) {
        userLiveData.observe { result ->
            if (!result.isSuccess) binding.stateViewFlipperProfile.setStateFromResult(result)
            result.doOnSuccess { wrapper ->
                wrapper.user?.let { user ->
                    setupUser(user)
                    if (postsAdapter.itemCount == 0) getUserPosts(user.id)
                }
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }
        userPostsLiveData.observe { data ->
            postsAdapter.submitData(lifecycle, data)
        }
        userPostsStateLiveData.observe { result ->
            binding.stateViewFlipperProfile.setStateFromResult(result)
        }
        openPostLiveEvent.observe { postId ->
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToPostGraph(postId))
        }
        openProfileLiveEvent.observe { userId ->
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToProfileGraph(userId))
        }
        openEditUserLiveEvent.observe {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToEditUserFragment())
        }
        openSettingsLiveEvent.observe {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSettingsFragment())
        }
    }

    private fun setupUser(user: User) = with(binding) {
        imageViewProfileAvatar.load(
            imageUrl = user.avatar?.url,
            placeHolderRes = R.drawable.ic_avatar_placeholder_inv,
            isCircle = true
        )
        textViewPostName.text = if (user.firstName != null && user.lastName != null) {
            getString(R.string.user_full_name, user.firstName, user.lastName)
        } else {
            user.username
        }
        textViewPostUsername.text = getString(R.string.profile_username_text, user.username).lowercase(Locale.getDefault())
        if (user.interests.isNotEmpty()) {
            textViewPostInterests.isVisible = true
            textViewPostInterests.text =
                getString(R.string.profile_interests_text, user.interests.sorted().joinToString { it.value })
        } else {
            textViewPostInterests.isVisible = false
        }
        if (!viewModel.isCurrentUserProfile) {
            buttonProfileEdit.isVisible = false
            buttonProfileSettings.isVisible = false
        }
    }

    private fun setupPostsList() = with(binding.recyclerViewProfilePosts) {
        adapter = postsAdapter.apply {
            onPostClick = viewModel::openPost
            onAuthorClick = viewModel::openProfile
            addLoadStateListener(viewModel::bindLoadingState)
        }.withLoadStateFooter(PagingLoadStateAdapter { postsAdapter.retry() })
        addVerticalDividerItemDecoration()
    }

    private fun setupAppbar() = with(binding) {
        if (args.userId == 0) toolbarProfile.navigationIcon = null
        appBarLayoutProfile.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
                val lightMode = verticalOffset < -resources.getDimensionPixelSize(R.dimen.profile_background_height) / 2
                val requiredColor = requireContext()
                    .getColorAttribute(if (lightMode) R.attr.colorOnSurface else R.attr.iconColorOnContrast)
                collapsingToolbarLayoutProfile.setScrimsShown(lightMode)
                buttonProfileEdit.setColorFilter(requiredColor)
                buttonProfileSettings.setColorFilter(requiredColor)
                with(toolbarProfile) {
                    title = if (lightMode) textViewPostName.text else null
                    setTitleTextColor(requiredColor)
                    setNavigationIconTint(requiredColor)
                }
            }
        )
    }
}