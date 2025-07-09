package com.example.a2zcare.presentation.screens.tracker
/*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.a2zcare.domain.entities.SavedUser

@Composable
fun SavedUsersSection(
    users: List<SavedUser>,
    onRemoveUser: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Saved Users", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        if (users.isEmpty()) {
            Text("No saved users.", style = MaterialTheme.typography.bodySmall)
        } else {
            LazyColumn {
                items(users) { user ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(user.name, style = MaterialTheme.typography.titleSmall)
                                Text(user.email, style = MaterialTheme.typography.bodySmall)
                            }
                            IconButton(onClick = { onRemoveUser(user.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Remove User")
                            }
                        }
                    }
                }
            }
        }
    }
}
 */