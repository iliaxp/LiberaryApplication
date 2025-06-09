package com.iliaxp.liberaryapplication.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    totalAmount: Double,
    onBackClick: () -> Unit,
    onPaymentComplete: () -> Unit
) {
    var cardNumber by remember { mutableStateOf("") }
    var expiryYear by remember { mutableStateOf("") }
    var expiryMonth by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    
    // Validation states
    val isCardNumberValid = cardNumber.length == 16 && cardNumber.all { it.isDigit() }
    
    // Year validation - accept any 2-digit number
    val isExpiryYearValid = cardNumber.length == 16 && cardNumber.all { it.isDigit() }
    
    // Month validation - must be 01-12
    val isExpiryMonthValid = expiryMonth.length == 2 && 
        expiryMonth.all { it.isDigit() } && 
        expiryMonth.toIntOrNull()?.let { it in 1..12 } ?: false
    
    // Only validate the actual date if both year and month are complete
    val isExpiryDateValid = if (isExpiryYearValid && isExpiryMonthValid) {
        val year = expiryYear.toIntOrNull() ?: 0
        val month = expiryMonth.toIntOrNull() ?: 0
        validateExpiryDate(month, year)
    } else {
        // If either field is incomplete, consider it valid for UI purposes
        true
    }
    
    val isCvvValid = cvv.length in 3..4 && cvv.all { it.isDigit() }
    
    // Enable pay button only when all fields are valid
    val isPayButtonEnabled = isCardNumberValid && 
        isExpiryYearValid && 
        isExpiryMonthValid && 
        isExpiryDateValid && 
        isCvvValid

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Total Amount Display
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Total Amount",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "$${String.format("%.2f", totalAmount)}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Card Details Form
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Card Number
                    OutlinedTextField(
                        value = cardNumber,
                        onValueChange = { newValue ->
                            if (newValue.length <= 16 && newValue.all { it.isDigit() }) {
                                cardNumber = newValue
                            }
                        },
                        label = { Text("Card Number") },
                        placeholder = { Text("1234 5678 9012 3456") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = cardNumber.isNotEmpty() && !isCardNumberValid,
                        supportingText = {
                            if (cardNumber.isNotEmpty() && !isCardNumberValid) {
                                Text("Please enter a valid 16-digit card number")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Expiry Date (YY/MM format)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Year Field (First)
                        OutlinedTextField(
                            value = expiryYear,
                            onValueChange = { newValue ->
                                if (newValue.length <= 2 && newValue.all { it.isDigit() }) {
                                    expiryYear = newValue
                                }
                            },
                            label = { Text("YY") },
                            placeholder = { Text("YY") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = expiryYear.isNotEmpty() && !isExpiryYearValid,
                            supportingText = {
                                if (expiryYear.isNotEmpty() && !isExpiryYearValid) {
                                    Text("Invalid year")
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "/",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(top = 16.dp)
                        )

                        // Month Field (Second)
                        OutlinedTextField(
                            value = expiryMonth,
                            onValueChange = { newValue ->
                                if (newValue.length <= 2 && newValue.all { it.isDigit() }) {
                                    val month = newValue.toIntOrNull() ?: 0
                                    if (month <= 12 || newValue.isEmpty()) {
                                        expiryMonth = newValue
                                    }
                                }
                            },
                            label = { Text("MM") },
                            placeholder = { Text("MM") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = expiryMonth.isNotEmpty() && !isExpiryMonthValid,
                            supportingText = {
                                if (expiryMonth.isNotEmpty() && !isExpiryMonthValid) {
                                    Text("Invalid month")
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // CVV
                    OutlinedTextField(
                        value = cvv,
                        onValueChange = { newValue ->
                            if (newValue.length <= 4 && newValue.all { it.isDigit() }) {
                                cvv = newValue
                            }
                        },
                        label = { Text("CVV") },
                        placeholder = { Text("123") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = cvv.isNotEmpty() && !isCvvValid,
                        supportingText = {
                            if (cvv.isNotEmpty() && !isCvvValid) {
                                Text("Please enter a valid CVV (3-4 digits)")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Pay Button
            Button(
                onClick = onPaymentComplete,
                //enabled = isPayButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    text = "Pay Now",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun validateExpiryDate(month: Int, year: Int): Boolean {
    val currentYear = LocalDate.now().year % 100
    val currentMonth = LocalDate.now().monthValue
    
    // If year is less than current year, it's invalid
    if (year < currentYear) return false
    
    // If year is greater than current year, it's valid
    if (year > currentYear) return true
    
    // If year is current year, check month
    return month >= currentMonth
} 