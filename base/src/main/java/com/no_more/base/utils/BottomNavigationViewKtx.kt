package com.no_more.base.utils

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.core.view.forEach
import androidx.lifecycle.Lifecycle
import androidx.navigation.FloatingWindow
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.no_more.base.abs.AbsFragment
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

fun BottomNavigationView.setupWithNavControllerFlow(navController: NavController): Flow<MenuItem> {
    fun NavDestination.matchDestination(@IdRes destId: Int): Boolean =
        hierarchy.any { it.id == destId }

    this.setOnItemSelectedListener { item ->
        onNavDestinationSelected(item, navController)
    }

    return callbackFlow<MenuItem> {
        val listener = object : NavController.OnDestinationChangedListener {
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: Bundle?
            ) {
                if (destination is FloatingWindow) {
                    return
                }
                this@setupWithNavControllerFlow.menu.forEach { item ->
                    if (destination.matchDestination(item.itemId)) {
                        item.isChecked = true
                        trySend(item)
                    }
                }
            }
        }

        navController.addOnDestinationChangedListener(listener)
        awaitClose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }.distinctUntilChanged()
}

fun AbsFragment<*>.setupWithNavController(
    bottomNavigationView: BottomNavigationView,
    navController: NavController
) = bottomNavigationView.setupWithNavControllerFlow(navController)
    .collectLatestState(fragment = this, state = Lifecycle.State.CREATED) {}