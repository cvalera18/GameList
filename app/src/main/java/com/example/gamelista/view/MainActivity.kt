package com.example.gamelista.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.gamelista.R
import com.example.gamelista.databinding.ActivityMainBinding
import com.example.gamelista.model.Game
import com.example.gamelista.model.MyGameProvider
import com.example.gamelista.viewmodel.GameViewModel

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    var myGameMutableList: MutableList<Game> = MyGameProvider.myGameList.toMutableList()

    private val gameViewModel : GameViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = binding.navMenu
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.frameContainer) as NavHostFragment
        bottomNavigationView.setupWithNavController(navHostFragment.navController)

//        gameViewModel.gameList.observe(this, Observer {
//            //Aquí se va a ejecutar lo que yo quiera cuando haya un cambio en LiveData
//        })

    }
}