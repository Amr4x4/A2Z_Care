package com.example.a2zcare.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.a2zcare.domain.entities.BottomNavItem
import com.example.a2zcare.presentation.theme.*
import kotlin.collections.forEachIndexed

@Composable
fun BottomNavigationBar(
    selectedIndex: Int,
    items: List<BottomNavItem>,
    onItemSelected: (Int) -> Unit,
    unreadNotificationCount: Int = 0 // Pass unread count here
) {
    NavigationBar(containerColor = fieldColor) {
        items.forEachIndexed { index, item ->
            val showBadge = item.title == "Notification" && unreadNotificationCount > 0

            NavigationBarItem(
                selected = index == selectedIndex,
                onClick = { onItemSelected(index) },
                icon = {
                    Box {
                        Icon(imageVector = item.icon, contentDescription = item.title)

                        if (showBadge) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .align(Alignment.TopEnd)
                                    .background(
                                        color = MaterialTheme.colorScheme.error,
                                        shape = CircleShape
                                    )
                            )
                        }
                    }
                },
                label = {
                    Text(item.title, style = MaterialTheme.typography.labelSmall)
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = navBarBackground,
                    selectedIconColor = selectedColor,
                    unselectedIconColor = unselectedColor,
                    selectedTextColor = selectedColor,
                    unselectedTextColor = unselectedColor
                )
            )
        }
    }
}
