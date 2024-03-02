package com.example.myapplication.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.myapplication.MainActivityViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme


class SplitwiseLayout(private val mainActivityViewModel: MainActivityViewModel) {

    @Composable
    fun LayoutUi() {
        MyApplicationTheme {
            Surface(
                modifier = Modifier.fillMaxSize(), color = Color.White
            ) {
                TabScreen()
            }
        }
    }


    @Composable
    fun TabScreen() {

        val selectedTabIndex = mainActivityViewModel.tabIndex.collectAsState().value
        val tabs = listOf("ADD", "View Balance")

        Column(modifier = Modifier.fillMaxWidth()) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(text = { Text(title) }, selected = selectedTabIndex == index, onClick = {
                        mainActivityViewModel.updateTabSelectedIndex(index)

                    })
                }
            }
            when (selectedTabIndex) {
                0 -> AddTransaction()
                1 -> ViewBalance()
            }
        }
    }


    @Composable
    fun AddTransaction() {
        Column(
            Modifier
                .padding(top = 24.dp)
                .padding(horizontal = 16.dp)
        ) {
            Row {
                Text(text = "Expense")
                Spacer(modifier = Modifier.width(8.dp))
                TextField(
                    value = mainActivityViewModel.expense.collectAsState().value, onValueChange = {
                        mainActivityViewModel.updateExpenseType(it)
                    }, singleLine = true, minLines = 2, maxLines = 4
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text(text = "Total")
                Spacer(modifier = Modifier.width(8.dp))
                TextField(
                    value = mainActivityViewModel.totalAmount.collectAsState().value,
                    onValueChange = {
                        mainActivityViewModel.updateTotalAmount(it)
                    },
                    singleLine = true,
                    minLines = 2,
                    maxLines = 4,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Paid By")

            TextField(
                value = mainActivityViewModel.paidBy.collectAsState().value, onValueChange = {
                    mainActivityViewModel.updatePaidBy(it)
                }, singleLine = true, minLines = 2, maxLines = 4
            )


            Spacer(modifier = Modifier.width(8.dp))
            val list = mainActivityViewModel.participantList

            if (list.isNotEmpty()) {
                Text(text = "Participants")
                list.forEach {
                    TextField(
                        value = it.userName, onValueChange = {

                        }, singleLine = true, minLines = 2, maxLines = 4
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.width(32.dp))
            Button(onClick = {
                mainActivityViewModel.addTransaction()
            }) {
                Text(text = "ADD")
            }
        }


    }

    @Composable
    fun ViewBalance() {

        Column {



        }

    }
}