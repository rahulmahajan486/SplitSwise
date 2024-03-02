package com.example.myapplication

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainActivityViewModel : ViewModel() {


    val transactionList = ArrayList<Transaction>()
    var idCount = 0

    private val _totalAmount = MutableStateFlow<String>("")
    val totalAmount = _totalAmount.asStateFlow()

    private val _tabIndex = MutableStateFlow(0)
    val tabIndex = _tabIndex.asStateFlow()

    private val _expense = MutableStateFlow("")
    val expense = _expense.asStateFlow()

    private val _paidBy = MutableStateFlow("")
    val paidBy = _paidBy.asStateFlow()

    private val balanceList = mutableStateListOf<Pair<UserEnum, Double>>()

    val participantList = mutableStateListOf<UserEnum>()


    fun updateTabSelectedIndex(tabIndex: Int) {
        _tabIndex.value = tabIndex
        if (tabIndex == 1) {
            calculateBalance()
        }
    }

    fun validateData(): Boolean {
        if (_paidBy.value.isNullOrEmpty() || !UserEnum.isUserPaidByIsFromList(_paidBy.value)) {
            return false
        }

        if (_totalAmount.value.toDouble() <= 0) {
            //Please Enter amount Greater than zero
            return false
        }
        return true

    }


    fun addTransaction() {

        if (!validateData()) {
            //Show toast some information is wrong
            return
        }

        val paidBy = UserEnum.getParticipantId(_paidBy.value)
        transactionList.add(
            Transaction(
                id = ++idCount,
                paidBy = paidBy,
                participantList = UserEnum.getParticipantListExceptPaidBy(paidBy),
                totalAmount = totalAmount.value.toDouble()
            )
        )

        _expense.value = ""
        _totalAmount.value = ""
        _paidBy.value = ""
        participantList.clear()

    }

    fun simpliFySplit() {

    }

    // neha , harsh and yash

    // neha paid 51 and split among harsh and yash (17,17,17)
    //yash paid 51 and split among yash and neha (17,17,17)

    //total neha =51/3 = 51-17 = 34 owed by = y, h
    //total yash  = 51/3 = 51-17 = 34

    // simplify, settle and add

    // when add split among all three

    fun calculateBalance() {


        //if all have total equal no need to pay anything anyone
        // if 6,45,6
        //50,0,50
        //51/3 , 20,0

        //30  = 10-u1 , 10-u2 u0 =20 = 3 u2
        //51 = 17-u0 , 17-u2  u1 = 34-10 = 24+7=31 u2
        val list = transactionList.groupBy { it.paidBy }.map {
            var totalAmountPaid = 0.0
            it.value.forEach { user ->
                totalAmountPaid += user.totalAmount
            }
            val amountOwedPerUser = totalAmountPaid / 3
            Pair(it.key, amountOwedPerUser)
        }.toMutableList()

        for (user in list) {
            for(inneruser in list) {
                if (user == inneruser)
                    continue

                if (user.second>0) {
                    //user.second =
                }
            }
        }

    }

    fun settleBalance(owedBy: Int, owe: Int, transactionId: Int) {

    }

    fun updateExpenseType(it: String) {
        _expense.value = it
    }

    fun updateTotalAmount(it: String) {
        _totalAmount.value = it
    }

    fun updatePaidBy(it: String) {
        _paidBy.value = it
        val id = UserEnum.getParticipantId(it)
        if (id > 0) {
            val list = UserEnum.getParticipantListExceptPaidBy(id)
            participantList.clear()
            participantList.addAll(list)
        }

    }


}

enum class UserEnum(val id: Int, val userName: String) {
    HARSH(1, "Harsh"), YASH(2, "Yash"), NEHA(3, "Neha");

    companion object {
        fun getParticipantListExceptPaidBy(paidBy: Int): List<UserEnum> {
            val list = ArrayList<UserEnum>()
            UserEnum.values().forEach {
                if (it.id != paidBy) {
                    list.add(it)
                }
            }
            return list
        }

        fun getParticipantId(name: String): Int {
            UserEnum.values().forEach {
                if (it.userName.equals(name, ignoreCase = true)) {
                    return it.id
                }
            }
            return -1
        }

        fun isUserPaidByIsFromList(paidBy: String): Boolean {
            UserEnum.values().forEach {
                if (it.userName.equals(paidBy, ignoreCase = true)) {
                    return true
                }
            }
            return false
        }
    }
}

class Owe(
    val paidBy: Int,
    val amount: Double,
    val owedBy: Pair<Int, Double>,
)

data class User(
    val users: UserEnum,
    val transaction: ArrayList<Transaction>,
)

data class Transaction(
    val id: Int,//auto increment in case of db
    val participantList: List<UserEnum>,
    val paidBy: Int,
    val totalAmount: Double,
)