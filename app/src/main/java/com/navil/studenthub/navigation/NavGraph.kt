package com.navil.studenthub.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.navil.studenthub.ui.screens.calendar.AcademicCalendarScreen
import com.navil.studenthub.ui.screens.exams.ExamScreen
import com.navil.studenthub.ui.screens.gpa.GpaScreen
import com.navil.studenthub.ui.screens.home.HomeScreen
import com.navil.studenthub.ui.screens.more.AboutScreen
import com.navil.studenthub.ui.screens.more.MoreScreen
import com.navil.studenthub.ui.screens.schedule.ScheduleScreen
import com.navil.studenthub.ui.screens.settings.SettingsScreen
import com.navil.studenthub.ui.screens.study.StudyScreen
import com.navil.studenthub.ui.screens.tasks.TasksScreen
import com.navil.studenthub.viewmodel.AppViewModelProvider
import com.navil.studenthub.viewmodel.CalendarViewModel
import com.navil.studenthub.viewmodel.ExamViewModel
import com.navil.studenthub.viewmodel.GpaViewModel
import com.navil.studenthub.viewmodel.HomeViewModel
import com.navil.studenthub.viewmodel.ScheduleViewModel
import com.navil.studenthub.viewmodel.SettingsViewModel
import com.navil.studenthub.viewmodel.StudyViewModel
import com.navil.studenthub.viewmodel.TasksViewModel

@Composable
fun StudentHubNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val scheduleViewModel: ScheduleViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val tasksViewModel: TasksViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val studyViewModel: StudyViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val gpaViewModel: GpaViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val examViewModel: ExamViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val calendarViewModel: CalendarViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val settingsViewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)

    val showBottomBar = Screen.bottomNavItems.any { it.route == currentRoute }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onNavigateToRoute = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn(animationSpec = tween(250)) },
            exitTransition = { fadeOut(animationSpec = tween(250)) }
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = homeViewModel,
                    onNavigateToSchedule = {
                        navController.navigate(Screen.Schedule.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToTasks = {
                        navController.navigate(Screen.Tasks.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToStudy = {
                        navController.navigate(Screen.Study.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToGpa = {
                        navController.navigate(Screen.Gpa.route)
                    },
                    onNavigateToExams = {
                        navController.navigate(Screen.Exams.route)
                    },
                    onAddClassClick = {
                        scheduleViewModel.openAddDialog()
                        navController.navigate(Screen.Schedule.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                        }
                    },
                    onAddTaskClick = {
                        tasksViewModel.openAddTaskDialog()
                        navController.navigate(Screen.Tasks.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                        }
                    },
                    onAddExamClick = {
                        examViewModel.openAddDialog()
                        navController.navigate(Screen.Exams.route)
                    }
                )
            }

            composable(Screen.Schedule.route) {
                ScheduleScreen(
                    viewModel = scheduleViewModel,
                    onNavigateToExams = { navController.navigate(Screen.Exams.route) }
                )
            }

            composable(Screen.Tasks.route) {
                TasksScreen(viewModel = tasksViewModel)
            }

            composable(Screen.Study.route) {
                StudyScreen(viewModel = studyViewModel)
            }

            composable(Screen.More.route) {
                MoreScreen(
                    onNavigateToCalendar = { navController.navigate(Screen.AcademicCalendar.route) },
                    onNavigateToGpa = { navController.navigate(Screen.Gpa.route) },
                    onNavigateToExams = { navController.navigate(Screen.Exams.route) },
                    onNavigateToStudy = {
                        navController.navigate(Screen.Study.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                        }
                    },
                    onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                    onNavigateToAbout = { navController.navigate(Screen.About.route) }
                )
            }

            composable(
                Screen.AcademicCalendar.route,
                enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) },
                exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)) }
            ) {
                AcademicCalendarScreen(
                    viewModel = calendarViewModel,
                    scheduleViewModel = scheduleViewModel,
                    tasksViewModel = tasksViewModel,
                    examViewModel = examViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                Screen.Gpa.route,
                enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) },
                exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)) }
            ) {
                GpaScreen(
                    viewModel = gpaViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                Screen.Exams.route,
                enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) },
                exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)) }
            ) {
                ExamScreen(
                    viewModel = examViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                Screen.Settings.route,
                enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) },
                exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)) }
            ) {
                SettingsScreen(
                    viewModel = settingsViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                Screen.About.route,
                enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) },
                exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)) }
            ) {
                AboutScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
