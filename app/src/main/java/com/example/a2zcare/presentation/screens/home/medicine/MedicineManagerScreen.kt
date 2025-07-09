package com.example.a2zcare.presentation.screens.home.medicine


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.a2zcare.data.local.entity.Medicine
import com.example.a2zcare.data.local.entity.MedicineType
import com.example.a2zcare.presentation.theme.backgroundColor
import com.example.a2zcare.presentation.theme.fieldColor
import com.example.a2zcare.presentation.viewmodel.MedicineViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineManagerScreen(
    navController: NavController,
    viewModel: MedicineViewModel = hiltViewModel()
) {
    val activeMedicines by viewModel.activeMedicines.collectAsState(initial = emptyList())
    val finishedMedicines by viewModel.finishedMedicines.collectAsState(initial = emptyList())
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Medicine Manager",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.LightGray
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add Medicine",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = fieldColor,
                    scrolledContainerColor = fieldColor
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                backgroundColor = backgroundColor,
                contentColor = Color.White
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                        text = "Active (${activeMedicines.size})",
                        color = Color.White
                    ) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text ="Finished (${finishedMedicines.size})",
                            color = Color.White
                        ) }
                )
            }

            when (selectedTab) {
                0 -> MedicineList(
                    medicines = activeMedicines,
                    onMedicineClick = { /* Handle medicine click */ },
                    onTakeMedicine = { medicine, time ->
                        viewModel.takeMedicine(medicine.id, time)
                    },
                    isActive = true
                )

                1 -> MedicineList(
                    medicines = finishedMedicines,
                    onMedicineClick = { /* Handle medicine click */ },
                    onTakeMedicine = { _, _ -> },
                    isActive = false
                )
            }
        }
    }

    if (showAddDialog) {
        AddMedicineDialog(
            onDismiss = { showAddDialog = false },
            onAddMedicine = { medicine ->
                viewModel.addMedicine(medicine)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun MedicineList(
    medicines: List<Medicine>,
    onMedicineClick: (Medicine) -> Unit,
    onTakeMedicine: (Medicine, String) -> Unit,
    isActive: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(medicines) { medicine ->
            MedicineCard(
                medicine = medicine,
                onClick = { onMedicineClick(medicine) },
                onTakeMedicine = { time -> onTakeMedicine(medicine, time) },
                isActive = isActive
            )
        }
    }
}

@Composable
fun MedicineCard(
    medicine: Medicine,
    onClick: () -> Unit,
    onTakeMedicine: (String) -> Unit,
    isActive: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) Color(0xFFE8F5E8) else Color(0xFFF5F5F5)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = medicine.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isActive) Color(0xFF1B5E20) else Color(0xFF666666)
                    )

                    Text(
                        text = "Dose: ${medicine.dose}",
                        fontSize = 14.sp,
                        color = if (isActive) Color(0xFF2E7D32) else Color(0xFF888888)
                    )

                    if (medicine.type == MedicineType.PILLS) {
                        Text(
                            text = "Pills: ${medicine.remainingPills}/${medicine.totalPills}",
                            fontSize = 14.sp,
                            color = if (medicine.remainingPills <= 5) Color.Red else Color(
                                0xFF2E7D32
                            )
                        )
                    }
                }

                Icon(
                    imageVector = when (medicine.type) {
                        MedicineType.PILLS -> Icons.Default.Medication
                        MedicineType.LIQUID -> Icons.Default.LocalDrink
                        MedicineType.INJECTION -> Icons.Default.Healing
                        MedicineType.DROPS -> Icons.Default.Opacity
                        MedicineType.INHALER -> Icons.Default.Air
                    },
                    contentDescription = medicine.type.name,
                    tint = if (isActive) Color(0xFF4CAF50) else Color(0xFF888888),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Times: ${medicine.intakeTimes.joinToString(", ")}",
                fontSize = 14.sp,
                color = if (isActive) Color(0xFF2E7D32) else Color(0xFF888888)
            )

            if (medicine.tips.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "💡 ${medicine.tips}",
                    fontSize = 12.sp,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier
                        .background(
                            Color(0xFFE8F5E8),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            if (isActive) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    medicine.intakeTimes.forEach { time ->
                        val currentTime =
                            SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                        val isTimeNow = time == currentTime // Simplified check

                        Button(
                            onClick = { onTakeMedicine(time) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isTimeNow) Color(0xFF4CAF50) else Color(
                                    0xFF81C784
                                )
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = time,
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicineDialog(
    onDismiss: () -> Unit,
    onAddMedicine: (Medicine) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var dose by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(MedicineType.PILLS) }
    var totalPills by remember { mutableStateOf("") }
    var times by remember { mutableStateOf("") }
    var tips by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Medicine") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Medicine Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = dose,
                    onValueChange = { dose = it },
                    label = { Text("Dose (e.g., 1 tablet, 5ml)") },
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = selectedType.name,
                        onValueChange = { },
                        label = { Text("Type") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expanded
                            )
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        MedicineType.values().forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.name) },
                                onClick = {
                                    selectedType = type
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                if (selectedType == MedicineType.PILLS) {
                    OutlinedTextField(
                        value = totalPills,
                        onValueChange = { totalPills = it },
                        label = { Text("Total Pills") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                OutlinedTextField(
                    value = times,
                    onValueChange = { times = it },
                    label = { Text("Times (e.g., 08:00,14:00,20:00)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = tips,
                    onValueChange = { tips = it },
                    label = { Text("Tips (optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank() && dose.isNotBlank() && times.isNotBlank()) {
                        val pillCount = if (selectedType == MedicineType.PILLS) {
                            totalPills.toIntOrNull() ?: 0
                        } else 0

                        val medicine = Medicine(
                            name = name,
                            dose = dose,
                            type = selectedType,
                            totalPills = pillCount,
                            remainingPills = pillCount,
                            intakeTimes = times.split(",").map { it.trim() },
                            tips = tips
                        )
                        onAddMedicine(medicine)
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}