package com.example.gamelista.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.gamelista.R
import com.example.gamelista.databinding.ActivityMainBinding
import com.example.gamelista.model.Game
import com.example.gamelista.model.FavGameProvider
import com.example.gamelista.viewmodel.GameViewModel

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = binding.navMenu
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.frameContainer) as NavHostFragment
        bottomNavigationView.setupWithNavController(navHostFragment.navController)
    }
}