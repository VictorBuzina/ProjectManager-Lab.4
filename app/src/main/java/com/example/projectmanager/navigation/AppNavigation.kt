package com.example.projectmanager.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projectmanager.data.ProjectViewModel
import com.example.projectmanager.ui.screens.AddProjectScreen
import com.example.projectmanager.ui.screens.DetailsProjectScreen
import com.example.projectmanager.ui.screens.ProjectsListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: ProjectViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "projects_list"
    ) {
        composable("projects_list") {
            ProjectsListScreen(
                viewModel = viewModel,
                onProjectClick = { projectId ->
                    navController.navigate("details/$projectId")
                },
                onAddClick = {
                    navController.navigate("add_project")
                }
            )
        }

        composable("add_project") {
            AddProjectScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "details/{projectId}",
            arguments = listOf(navArgument("projectId") { type = NavType.IntType })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getInt("projectId") ?: -1
            DetailsProjectScreen(
                projectId = projectId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
