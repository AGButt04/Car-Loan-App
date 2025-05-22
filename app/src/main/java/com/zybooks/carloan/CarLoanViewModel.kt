package com.zybooks.carloan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CarLoanViewModel : ViewModel() {

    var down by mutableStateOf("")
    var price by mutableStateOf("")
    var interest by mutableStateOf("")
    var months by mutableStateOf("36 months")
    var payment by mutableDoubleStateOf(0.00)


}