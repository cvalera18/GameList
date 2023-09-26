package com.example.gamelista

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.gamelista.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    var myGameMutableList: MutableList<Game> = MyGameProvider.myGameList.toMutableList()
//
//    private val mOnNavMenu = BottomNavigationView.OnNavigationItemSelectedListener { item ->
//
//        when (item.itemId) {
//            R.id.listFragment -> {
//                supportFragmentManager.commit {
//                    replace<ListFragment>(R.id.frameContainer)
//                    setReorderingAllowed(true)
//                    addToBackStack("replacement")
//                }
//                return@OnNavigationItemSelectedListener true
//            }
//
//            R.id.favFragment -> {
//                supportFragmentManager.commit {
//                    replace<FavFragment>(R.id.frameContainer)
//                    setReorderingAllowed(true)
//                    addToBackStack("replacement")
//                }
//                return@OnNavigationItemSelectedListener true
//            }
//
//            R.id.myListFragment -> {
//                supportFragmentManager.commit {
//                    replace<MyListFragment>(R.id.frameContainer)
//                    setReorderingAllowed(true)
//                    addToBackStack("replacement")
//                }
//                return@OnNavigationItemSelectedListener true
//            }
//        }
//        false
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = binding.navMenu
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.frameContainer) as NavHostFragment
        bottomNavigationView.setupWithNavController(navHostFragment.navController)
//        navigation.setOnNavigationItemSelectedListener(mOnNavMenu)


//
//        supportFragmentManager.commit {
//            replace<ListFragment>(R.id.frameContainer)
//            setReorderingAllowed(true)
//            addToBackStack("replacement")
//        }
    }
}