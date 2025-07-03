package com.example.a2zcare.presentation.screens.tracker
/*
@Composable
fun EmergencyUsersSection(
    users: List<EmergencyUser>,
    onRemoveUser: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Emergency Contacts", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        if (users.isEmpty()) {
            Text("No emergency contacts added.", style = MaterialTheme.typography.bodySmall)
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
                                Icon(Icons.Default.Delete, contentDescription = "Remove Emergency Contact")
                            }
                        }
                    }
                }
            }
        }
    }
}

 */