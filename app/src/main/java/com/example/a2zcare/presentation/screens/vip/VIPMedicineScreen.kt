package com.example.a2zcare.presentation.screens.vip
/*
package com.example.a2zcare.presentation.screens.vip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.a2zcare.data.MockDataRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VIPMedicineOrdersScreen2(navController: NavController) {
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var showCart by remember { mutableStateOf(false) }
    var cartItems by remember { mutableStateOf(MockDataRepository.cartItems) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("VIP Medicine Orders", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    BadgedBox(
                        badge = {
                            if (cartItems.isNotEmpty()) {
                                Badge {
                                    Text("${cartItems.size}")
                                }
                            }
                        }
                    ) {
                        IconButton(onClick = { showCart = true }) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // VIP Benefits Banner
            VIPMedicineBenefitsBanner()

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search medicines...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp)
            )

            // Category Filter
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(MockDataRepository.medicineCategories) { category ->
                    FilterChip(
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        selected = selectedCategory == category,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            // Medicine Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(MockDataRepository.medicines.filter {
                    (selectedCategory == "All" || it.category == selectedCategory) &&
                            (searchQuery.isEmpty() || it.name.contains(searchQuery, ignoreCase = true))
                }) { medicine ->
                    MedicineCard(
                        medicine = medicine,
                        onAddToCart = { quantity ->
                            MockDataRepository.addToCart(medicine, quantity)
                            cartItems = MockDataRepository.cartItems.toMutableList()
                        }
                    )
                }
            }
        }
    }

    if (showCart) {
        CartBottomSheet(
            cartItems = cartItems,
            onDismiss = { showCart = false },
            onCheckout = {
                showCart = false
                navController.navigate("checkout")
            },
            onUpdateQuantity = { medicineId, quantity ->
                MockDataRepository.updateCartItemQuantity(medicineId, quantity)
                cartItems = MockDataRepository.cartItems.toMutableList()
            }
        )
    }
}

@Composable
private fun VIPMedicineBenefitsBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1976D2))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "VIP Medicine Benefits",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                VIPBenefitItem("🚚 Free Delivery", "Within 2 hours")
                VIPBenefitItem("💊 Verified", "100% Authentic")
                VIPBenefitItem("💰 Discounts", "Up to 25% off")
            }
        }
    }
}

@Composable
private fun VIPBenefitItem(title: String, subtitle: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            title,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            subtitle,
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 10.sp
        )
    }
}

@Composable
private fun MedicineCard(
    medicine: MockDataRepository.Medicine,
    onAddToCart: (Int) -> Unit
) {
    var quantity by remember { mutableStateOf(0) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // Medicine Image Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        Color(0xFFF5F5F5),
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.MedicalServices,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                medicine.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                medicine.company,
                fontSize = 12.sp,
                color = Color.Gray
            )

            if (medicine.prescriptionRequired) {
                Text(
                    "Prescription Required",
                    fontSize = 10.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "₹${medicine.price}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )
                    if (medicine.originalPrice > medicine.price) {
                        Text(
                            "₹${medicine.originalPrice}",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }

                if (quantity == 0) {
                    Button(
                        onClick = {
                            quantity = 1
                            onAddToCart(1)
                        },
                        modifier = Modifier.size(36.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                if (quantity > 0) {
                                    quantity--
                                    onAddToCart(-1)
                                }
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Default.Remove,
                                contentDescription = "Remove",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            quantity.toString(),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        IconButton(
                            onClick = {
                                quantity++
                                onAddToCart(1)
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add",
                                modifier = Modifier.size(16.dp)
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
private fun CartBottomSheet(
    cartItems: List<MockDataRepository.CartItem>,
    onDismiss: () -> Unit,
    onCheckout: () -> Unit,
    onUpdateQuantity: (String, Int) -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "Your Cart",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (cartItems.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Your cart is empty",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(cartItems) { item ->
                        CartItemCard(
                            item = item,
                            onUpdateQuantity = onUpdateQuantity
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                val subtotal = cartItems.sumOf { it.medicine.price * it.quantity }
                val discount = subtotal * 0.15
                val total = subtotal - discount

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Subtotal: ₹${subtotal.toInt()}",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Text(
                            "VIP Discount: -₹${discount.toInt()}",
                            fontSize = 14.sp,
                            color = Color(0xFF4CAF50)
                        )
                        Text(
                            "Total: ₹${total.toInt()}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = onCheckout,
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text("Checkout")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun CartItemCard(
    item: MockDataRepository.CartItem,
    onUpdateQuantity: (String, Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.MedicalServices,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    item.medicine.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    item.medicine.company,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (item.quantity > 1) {
                            onUpdateQuantity(item.medicine.id, item.quantity - 1)
                        } else {
                            onUpdateQuantity(item.medicine.id, 0)
                        }
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        if (item.quantity > 1) Icons.Default.Remove else Icons.Default.Delete,
                        contentDescription = if (item.quantity > 1) "Remove" else "Delete",
                        modifier = Modifier.size(16.dp)
                    )
                }

                Text(
                    "${item.quantity}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                IconButton(
                    onClick = { onUpdateQuantity(item.medicine.id, item.quantity + 1) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                "₹${(item.medicine.price * item.quantity).toInt()}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

object MockDataRepository {

    data class Medicine(
        val id: String,
        val name: String,
        val company: String,
        val category: String,
        val price: Double,
        val originalPrice: Double,
        val description: String,
        val inStock: Boolean = true,
        val prescriptionRequired: Boolean = false
    )

    data class CartItem(
        val medicine: Medicine,
        val quantity: Int
    )

    val medicines = listOf(
        Medicine(
            id = "1",
            name = "Paracetamol 500mg",
            company = "Sun Pharma",
            category = "Pain Relief",
            price = 25.0,
            originalPrice = 30.0,
            description = "Effective pain relief and fever reducer"
        ),
        Medicine(
            id = "2",
            name = "Amoxicillin 250mg",
            company = "Cipla",
            category = "Antibiotics",
            price = 120.0,
            originalPrice = 150.0,
            description = "Broad-spectrum antibiotic",
            prescriptionRequired = true
        ),
        Medicine(
            id = "3",
            name = "Cetirizine 10mg",
            company = "Dr. Reddy's",
            category = "Allergy",
            price = 35.0,
            originalPrice = 45.0,
            description = "Antihistamine for allergies"
        ),
        Medicine(
            id = "4",
            name = "Omeprazole 20mg",
            company = "Lupin",
            category = "Digestive",
            price = 85.0,
            originalPrice = 100.0,
            description = "Proton pump inhibitor for acid reflux"
        ),
        Medicine(
            id = "5",
            name = "Metformin 500mg",
            company = "Torrent",
            category = "Diabetes",
            price = 45.0,
            originalPrice = 60.0,
            description = "Blood sugar control medication",
            prescriptionRequired = true
        ),
        Medicine(
            id = "6",
            name = "Ibuprofen 400mg",
            company = "Abbott",
            category = "Pain Relief",
            price = 40.0,
            originalPrice = 50.0,
            description = "Anti-inflammatory pain reliever"
        ),
        Medicine(
            id = "7",
            name = "Vitamin D3 1000 IU",
            company = "Mankind",
            category = "Vitamins",
            price = 180.0,
            originalPrice = 220.0,
            description = "Vitamin D supplement"
        ),
        Medicine(
            id = "8",
            name = "Azithromycin 500mg",
            company = "Zydus",
            category = "Antibiotics",
            price = 95.0,
            originalPrice = 120.0,
            description = "Macrolide antibiotic",
            prescriptionRequired = true
        ),
        Medicine(
            id = "9",
            name = "Loratadine 10mg",
            company = "Glenmark",
            category = "Allergy",
            price = 55.0,
            originalPrice = 70.0,
            description = "Non-drowsy antihistamine"
        ),
        Medicine(
            id = "10",
            name = "Pantoprazole 40mg",
            company = "Alkem",
            category = "Digestive",
            price = 65.0,
            originalPrice = 80.0,
            description = "Proton pump inhibitor"
        ),
        Medicine(
            id = "11",
            name = "Aspirin 75mg",
            company = "Bayer",
            category = "Cardiovascular",
            price = 30.0,
            originalPrice = 40.0,
            description = "Low-dose aspirin for heart health"
        ),
        Medicine(
            id = "12",
            name = "Vitamin B Complex",
            company = "Revital",
            category = "Vitamins",
            price = 150.0,
            originalPrice = 180.0,
            description = "Complete B-vitamin complex"
        )
    )

    val medicineCategories = listOf(
        "All",
        "Pain Relief",
        "Antibiotics",
        "Allergy",
        "Digestive",
        "Diabetes",
        "Vitamins",
        "Cardiovascular"
    )

    // Sample cart items (you can modify this as needed)
    val cartItems = mutableListOf<CartItem>()

    fun addToCart(medicine: Medicine, quantity: Int) {
        val existingItem = cartItems.find { it.medicine.id == medicine.id }
        if (existingItem != null) {
            cartItems.remove(existingItem)
            cartItems.add(existingItem.copy(quantity = existingItem.quantity + quantity))
        } else {
            cartItems.add(CartItem(medicine, quantity))
        }
    }

    fun removeFromCart(medicineId: String) {
        cartItems.removeAll { it.medicine.id == medicineId }
    }

    fun updateCartItemQuantity(medicineId: String, quantity: Int) {
        val existingItem = cartItems.find { it.medicine.id == medicineId }
        if (existingItem != null) {
            cartItems.remove(existingItem)
            if (quantity > 0) {
                cartItems.add(existingItem.copy(quantity = quantity))
            }
        }
    }

    fun clearCart() {
        cartItems.clear()
    }
}
*/