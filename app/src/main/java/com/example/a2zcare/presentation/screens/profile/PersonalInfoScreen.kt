//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavController
//import com.a2zcare.pro.domain.model.Gender
//import com.a2zcare.pro.presentation.viewmodel.AccountViewModel
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun PersonalInfoScreen(
//    navController: NavController,
//    viewModel: AccountViewModel = hiltViewModel()
//) {
//    val user by viewModel.user.collectAsState()
//    var fullName by remember { mutableStateOf(user.fullName) }
//    var email by remember { mutableStateOf(user.email) }
//    var phoneNumber by remember { mutableStateOf(user.phoneNumber) }
//    var selectedGender by remember { mutableStateOf(user.gender) }
//    var dateOfBirth by remember { mutableStateOf(user.dateOfBirth) }
//    var showGenderDialog by remember { mutableStateOf(false) }
//
//    if (showGenderDialog) {
//        AlertDialog(
//            onDismissRequest = { showGenderDialog = false },
//            title = { Text("Select Gender") },
//            text = {
//                Column {
//                    Gender.values().forEach { gender ->
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .clickable {
//                                    selectedGender = gender
//                                    showGenderDialog = false
//                                }
//                                .padding(vertical = 8.dp),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            RadioButton(
//                                selected = selectedGender == gender,
//                                onClick = {
//                                    selectedGender = gender
//                                    showGenderDialog = false
//                                }
//                            )
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text(gender.name.lowercase().replaceFirstChar { it.uppercase() })
//                        }
//                    }
//                }
//            },
//            confirmButton = {
//                TextButton(onClick = { showGenderDialog = false }) {
//                    Text("OK")
//                }
//            }
//        )
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Personal Information") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.navigateUp() }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                },
//                actions = {
//                    TextButton(
//                        onClick = {
//                            viewModel.updateUser(
//                                user.copy(
//                                    fullName = fullName,
//                                    email = email,
//                                    phoneNumber = phoneNumber,
//                                    gender = selectedGender,
//                                    dateOfBirth = dateOfBirth
//                                )
//                            )
//                            navController.navigateUp()
//                        }
//                    ) {
//                        Text("Save", color = Color(0xFFE91E63))
//                    }
//                }
//            )
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .background(MaterialTheme.colorScheme.background)
//        ) {
//            // Profile Picture
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(32.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                Box(
//                    modifier = Modifier
//                        .size(120.dp)
//                        .clip(CircleShape)
//                        .background(Color(0xFFE91E63))
//                        .clickable { /* Handle profile picture change */ },
//                    contentAlignment = Alignment.Center
//                ) {
//                    if (user.profileImageUrl.isEmpty()) {
//                        Text(
//                            user.fullName.split(" ").take(2).joinToString("") { it.first().toString() },
//                            color = Color.White,
//                            fontSize = 36.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//                }
//
//                // Edit Button
//                Box(
//                    modifier = Modifier
//                        .size(36.dp)
//                        .clip(CircleShape)
//                        .background(Color(0xFFE91E63))
//                        .align(Alignment.BottomEnd)
//                        .clickable { /* Handle edit */ },
//                    contentAlignment = Alignment.Center
//                ) {
//                    Icon(
//                        Icons.Default.Edit,
//                        contentDescription = "Edit",
//                        tint = Color.White,
//                        modifier = Modifier.size(20.dp)
//                    )
//                }
//            }
//
//            // Form Fields
//            Column(
//                modifier = Modifier.padding(16.dp)
//            ) {
//                OutlinedTextField(
//                    value = fullName,
//                    onValueChange = { fullName = it },
//                    label = { Text("Full Name") },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                OutlinedTextField(
//                    value = email,
//                    onValueChange = { email = it },
//                    label = { Text("Email") },
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                OutlinedTextField(
//                    value = phoneNumber,
//                    onValueChange = { phoneNumber = it },
//                    label = { Text("Phone Number") },
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                OutlinedTextField(
//                    value = selectedGender.name.lowercase().replaceFirstChar { it.uppercase() },
//                    onValueChange = { },
//                    label = { Text("Gender") },
//                    readOnly = true,
//                    trailingIcon = {
//                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable { showGenderDialog = true }
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                OutlinedTextField(
//                    value = dateOfBirth,
//                    onValueChange = { dateOfBirth = it },
//                    label = { Text("Date of Birth") },
//                    placeholder = { Text("DD/MM/YYYY") },
//                    trailingIcon = {
//                        Icon(Icons.Default.DateRange, contentDescription = null)
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }
//        }
//    }
//}
