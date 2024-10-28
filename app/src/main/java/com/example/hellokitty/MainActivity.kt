package com.example.hellokitty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hellokitty.ui.BreedDetail
import com.example.hellokitty.ui.CatBreedsList
import com.example.hellokitty.ui.compsables.CatBreedDetailScreen
import com.example.hellokitty.ui.compsables.CatBreedsListScreen
import com.example.hellokitty.ui.theme.BackgroundColor
import com.example.hellokitty.ui.theme.HelloKittyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloKittyTheme {
                val navController = rememberNavController()
                NavHost(
                    navController,
                    startDestination = CatBreedsList,
                    modifier = Modifier.background(BackgroundColor)
                ) {
                    composable<CatBreedsList> {
                        CatBreedsListScreen { navController.navigate(BreedDetail(it)) }
                    }
                    composable<BreedDetail> {
                        CatBreedDetailScreen()
                    }
                }
            }
        }
    }
}