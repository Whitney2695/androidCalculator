package com.example.firstapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorApp()
        }
    }
}

@Composable
fun CalculatorApp() {
    var inputText by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display Input Field
        BasicTextField(
            value = inputText,
            onValueChange = { inputText = it },
            textStyle = TextStyle(fontSize = 32.sp, color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .padding(16.dp)
        )

        // Display Result
        Text(
            text = resultText,
            fontSize = 24.sp,
            color = Color.Blue,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Calculator Buttons
        val buttons = listOf(
            listOf("7", "8", "9", "/"),
            listOf("4", "5", "6", "*"),
            listOf("1", "2", "3", "-"),
            listOf("0", "C", "=", "+"),
            listOf("⌫") // New Cancel Button
        )

        buttons.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { label ->
                    Button(
                        onClick = {
                            when (label) {
                                "=" -> resultText = evaluateExpression(inputText)
                                "C" -> {
                                    inputText = ""
                                    resultText = ""
                                }
                                "⌫" -> inputText = inputText.dropLast(1) // Remove last character
                                else -> inputText += label
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (label) {
                                "C" -> Color.Red
                                "=" -> Color.Green
                                "⌫" -> Color.Yellow
                                else -> Color.DarkGray
                            },
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                    ) {
                        Text(label, fontSize = 24.sp)
                    }
                }
            }
        }
    }
}

// Expression Evaluator (Handles Basic Arithmetic)
fun evaluateExpression(expression: String): String {
    return try {
        val sanitizedExpression = expression.replace(Regex("[^0-9+\\-*/.]"), "")
        val tokens = sanitizedExpression.split(Regex("(?<=[+\\-*/])|(?=[+\\-*/])"))
        var total = tokens[0].toDouble()
        var operator = ""

        for (token in tokens.drop(1)) {
            when {
                token in "+-*/" -> operator = token
                operator == "+" -> total += token.toDouble()
                operator == "-" -> total -= token.toDouble()
                operator == "*" -> total *= token.toDouble()
                operator == "/" -> total /= token.toDouble()
            }
        }
        total.toString()
    } catch (e: Exception) {
        "Error"
    }
}
