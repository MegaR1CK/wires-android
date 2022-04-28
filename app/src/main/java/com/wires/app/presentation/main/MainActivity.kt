package com.wires.app.presentation.main

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.wires.app.R
import com.wires.app.databinding.ActivityMainBinding
import com.wires.app.extensions.doOnApplyWindowInsets
import com.wires.app.extensions.setMargin
import com.wires.app.managers.BottomNavigationViewManager
import dagger.android.AndroidInjection

// TODO: post edit delete
// TODO: create channels
class MainActivity : AppCompatActivity(), BottomNavigationViewManager {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomBarInsetsUpdate()
        val navController = (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment).navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun setNavigationViewVisibility(isVisible: Boolean) {
        binding.bottomNavigationView.isVisible = isVisible
    }

    private fun bottomBarInsetsUpdate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            binding.bottomNavigationView.doOnApplyWindowInsets { view, insets, _ ->
                view.setMargin(
                    bottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom,
                    top = 0
                )
                insets
            }
        }
    }
}
