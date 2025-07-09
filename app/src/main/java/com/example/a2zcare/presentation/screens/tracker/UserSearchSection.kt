package com.example.a2zcare.presentation.screens.tracker
/*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.a2zcare.domain.entities.User

@Composable
fun UserSearchSection(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    searchResults: List<User>,
    onSaveUser: (String) -> Unit,
    onShareLocation: (String) -> Unit,
    onAddEmergencyUser: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search by username or email") },
            singleLine = true,
            keyboardActions = KeyboardActions(onDone = { onSearch() }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (searchResults.isEmpty()) {
            Text("No users found.", style = MaterialTheme.typography.bodySmall)
        } else {
            LazyColumn {
                items(searchResults) { user ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Name: ${user.name}", style = MaterialTheme.typography.titleSmall)
                            Text("Email: ${user.email}", style = MaterialTheme.typography.bodySmall)

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(onClick = { onSaveUser(user.id) }) {
                                    Text("Save")
                                }
                                Button(onClick = { onShareLocation(user.id) }) {
                                    Text("Share")
                                }
                                Button(onClick = { onAddEmergencyUser(user.id) }) {
                                    Text("Add Emergency")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
 */
