package com.zybooks.carloan

import android.content.res.Configuration
import android.os.Bundle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zybooks.carloan.ui.theme.CarLoanTheme
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarLoanTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PetApp(carLoanViewModel = CarLoanViewModel())
                }
            }
        }
    }
}

sealed class Routes {
    @Serializable
    data object Buy

    @Serializable
    data object Main
}

@Composable
fun PetApp(
    carLoanViewModel: CarLoanViewModel
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.Main
    ) {
        composable<Routes.Buy> {
            BuyScreen(
                modifier = Modifier,
                carLoanViewModel = carLoanViewModel,
                onUpClick = {
                    navController.navigateUp()
                }
            )
        }
        composable<Routes.Main> {
            CarLoanScreen(
                onUpClick = {navController.navigate(Routes.Buy)},
                modifier = Modifier
            )
        }
    }
}

@Composable
fun BuyScreen(modifier: Modifier, carLoanViewModel: CarLoanViewModel, onUpClick: () -> Unit) {
    Scaffold (
        topBar = {
            CarAppBar(
                "Confirmation of purchase",
                canNavigateBack = true,
                onUpClick = onUpClick,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.aston),
                contentDescription = "M4",
                modifier = Modifier.size(400.dp, height = 200.dp)
            )
            Text(
                text = "You have successfully bought this car!",
                modifier = Modifier.padding(start = 55.dp, top = 15.dp)
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarAppBar(
    title: String,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = false,
    onUpClick: () -> Unit = { }
) {
    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onUpClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                }
            }
        }
    )
}

@Composable
fun Checkout(onUpClick: () -> Unit) {
    Button( onClick = onUpClick)
    {
        Text("CHECKOUT")
    }
}

@Composable
fun CarLoanScreen(onUpClick: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold (
        topBar = {
            CarAppBar("Car Loan App", modifier)
        }
    ) { innerPadding ->
        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT)
            CarLoanScreenP(modifier, paddingValues = innerPadding, onUpClick = onUpClick)
        else
            CarLoanScreenL(onUpClick, modifier, paddingValues = innerPadding)
    }
}

@Composable
fun CarLoanScreenL(onUpClick: () -> Unit, modifier: Modifier = Modifier, carLoanViewModel: CarLoanViewModel = viewModel(), paddingValues: PaddingValues) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(
                modifier = modifier.weight(1f),
                verticalArrangement = Arrangement.Top
            ) {
                Image(
                    painter = painterResource(id = R.drawable.m4),
                    contentDescription = "M4",
                    modifier = Modifier.size(400.dp, height = 200.dp).padding(top = 5.dp)
                )
            }
            Column(
                modifier = modifier.weight(1f).padding(12.dp)
            ) {
                InputBox(carLoanViewModel.price, "Loan Amount (Car Price)", { carLoanViewModel.price = it }, modifier)
                InputBox(carLoanViewModel.down, "Down Payment", { carLoanViewModel.down = it }, modifier)
                InputBox(carLoanViewModel.interest, "Interest", { carLoanViewModel.interest = it }, modifier)
                PaymentRow(carLoanViewModel)
            }
            Column(
                modifier = modifier.weight(1f).padding(8.dp)
            ) {
                RadioGroupL(
                    labelText = "How many months?",
                    radioOptions = listOf("24 months", "36 months", "48 months", "60 months"),
                    selectedOption = carLoanViewModel.months,
                    onSelected = { carLoanViewModel.months = it }
                )
                PaymentButton(carLoanViewModel, "CALCULATE")
                Checkout(onUpClick)
            }
        }
    }
}

@Composable
fun CarLoanScreenP(
    modifier: Modifier = Modifier,
    carLoanViewModel: CarLoanViewModel = viewModel(),
    paddingValues: PaddingValues,
    onUpClick: () -> Unit
    ) {
    Column (
        modifier = modifier.padding(paddingValues)
    ) {
        Image(
            painter = painterResource(R.drawable.m4),
            contentDescription = "M4",
            modifier = modifier.fillMaxWidth().size(195.dp)
        )
        InputBox(carLoanViewModel.price, "Loan Amount (Car Price)",
            {carLoanViewModel.price = it},
            modifier.padding(start = 10.dp, top = 16.dp, bottom = 14.dp, end = 10.dp).fillMaxWidth())
        Row (
            modifier = modifier.padding(6.dp).fillMaxWidth(),
        ){
            InputBox(carLoanViewModel.down, "Down Payment", {carLoanViewModel.down = it}, modifier.weight(1f))
            Spacer(modifier = modifier.width(16.dp))
            InputBox(carLoanViewModel.interest, "Interest",
                {carLoanViewModel.interest = it},
                modifier.weight(1f).padding(bottom = 10.dp))
        }
        Row(
            modifier = modifier.fillMaxWidth().padding(start = 10.dp)
        ) {
            RadioGroup(
                labelText = "How many months?",
                radioOptions = listOf("24 months", "36 months"),
                selectedOption = carLoanViewModel.months,
                onSelected = { carLoanViewModel.months = it },
            )
        }
        Row (modifier = modifier.fillMaxWidth().padding(start = 10.dp)) {
            RadioGroup(
                labelText = "",
                radioOptions = listOf("48 months", "60 months"),
                selectedOption = carLoanViewModel.months,
                onSelected = { carLoanViewModel.months = it },
            )
        }
        PaymentRow(carLoanViewModel)
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PaymentButton(carLoanViewModel, "CALCULATE")
            Checkout(onUpClick)
        }
    }
}

@Composable
fun InputBox(boxValue: String, labelText: String, onChange: (String) -> Unit, modifier: Modifier) {
    TextField(
        value = boxValue,
        onValueChange = {onChange(it)},
        singleLine = true,
        label = { Text(labelText) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
    )
}

@Composable
fun PaymentButton(carLoanViewModel: CarLoanViewModel, label: String) {
    Button(
        onClick = {
            val interestVal = (carLoanViewModel.interest.toDouble()/100.00)/12.00
            val priceVal = carLoanViewModel.price.toDouble() - carLoanViewModel.down.toDouble()
            carLoanViewModel.payment = calculatePayment(interestVal, priceVal, carLoanViewModel.months)
        },
        modifier = Modifier
    ) {
        Text(label)
    }
}

fun calculatePayment(interest: Double, price: Double, months: String) : Double {
    var cal = 0.0
    val month = when (months) {
        "24 months" -> 24
        "36 months" -> 36
        "48 months" -> 48
        else -> 60
    }
    cal = interest * price/ (1 - (1 + interest).pow(-month))
    return cal
}

@Composable
fun PaymentRow(carLoanViewModel: CarLoanViewModel) {
    Text(
        text = "Monthly Payment: $" + "%.2f".format(carLoanViewModel.payment),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.DarkGray,
        modifier = Modifier.padding(start = 10.dp, top = 8.dp, bottom = 8.dp)
    )
}

@Composable
fun RadioGroupL(
    labelText: String,
    radioOptions: List<String>,
    selectedOption: String,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isSelectedOption: (String) -> Boolean = { selectedOption == it}
    Column(modifier = modifier) {
        Text(
            text = labelText, fontWeight = FontWeight.Bold, fontSize = 20.sp
        )
        radioOptions.forEach { option ->
            Row(
                modifier = Modifier
                    .selectable(
                        selected = isSelectedOption(option),
                        onClick = { onSelected(option) },
                        role = Role.RadioButton
                    )
                    .padding(start = 10.dp, end = 6.dp),
            ) {
                RadioButton(
                    selected = isSelectedOption(option),
                    onClick = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = option)
                Spacer(modifier = Modifier.width(70.dp))
            }
        }
    }
}

@Composable
fun RadioGroup(
    labelText: String,
    radioOptions: List<String>,
    selectedOption: String,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isSelectedOption: (String) -> Boolean = { selectedOption == it}
    Column(modifier = modifier) {
        Text(
            text = labelText, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Row {
            radioOptions.forEach { option ->
                Row(
                    modifier = Modifier
                        .selectable(
                            selected = isSelectedOption(option),
                            onClick = { onSelected(option) },
                            role = Role.RadioButton
                        )
                        .padding(start = 10.dp, end = 6.dp),
                ) {
                    RadioButton(
                        selected = isSelectedOption(option),
                        onClick = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text = option)
                    Spacer(modifier = Modifier.width(70.dp))
                }
            }
        }
    }
}