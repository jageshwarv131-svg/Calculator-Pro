package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.CalculatorDatabase
import com.example.data.model.CalcHistoryItem
import com.example.data.model.DEFAULT_CURRENCIES
import com.example.data.model.FALLBACK_RATES
import com.example.data.network.CurrencyRepository
import com.example.util.ExpressionEvaluator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.round

enum class AppMode {
    STANDARD,
    SCIENTIFIC,
    CURRENCY,
    MASS,
    TIP,
    DISCOUNT
}

class CalculatorViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("smart_calc_prefs", Context.MODE_PRIVATE)
    private val historyDao = CalculatorDatabase.getDatabase(application).calcHistoryDao()
    private val currencyRepo = CurrencyRepository()

    // Active App Mode
    private val _currentMode = MutableStateFlow(AppMode.STANDARD)
    val currentMode: StateFlow<AppMode> = _currentMode.asStateFlow()

    // Dark Mode (persisted)
    private val _isDarkMode = MutableStateFlow(prefs.getBoolean("dark_mode", false))
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    fun toggleDarkMode() {
        val next = !_isDarkMode.value
        _isDarkMode.value = next
        prefs.edit().putBoolean("dark_mode", next).apply()
    }

    fun switchMode(mode: AppMode) {
        _currentMode.value = mode
        when (mode) {
            AppMode.CURRENCY -> calculateCurrency()
            AppMode.MASS -> calculateMass()
            AppMode.TIP -> calculateTip()
            AppMode.DISCOUNT -> calculateDiscount()
            else -> {}
        }
    }

    // ==========================================
    // History
    // ==========================================
    val standardHistory: StateFlow<List<CalcHistoryItem>> = historyDao.getHistoryByMode("STANDARD")
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val scientificHistory: StateFlow<List<CalcHistoryItem>> = historyDao.getHistoryByMode("SCIENTIFIC")
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun clearCurrentHistory() {
        viewModelScope.launch {
            val modeName = if (_currentMode.value == AppMode.SCIENTIFIC) "SCIENTIFIC" else "STANDARD"
            historyDao.clearHistoryByMode(modeName)
        }
    }

    // ==========================================
    // STANDARD CALCULATOR
    // ==========================================
    private val _stdDisplayValue = MutableStateFlow("0")
    val stdDisplayValue: StateFlow<String> = _stdDisplayValue.asStateFlow()

    private val _stdCurrentExpression = MutableStateFlow("")
    val stdCurrentExpression: StateFlow<String> = _stdCurrentExpression.asStateFlow()

    private var stdMemoryValue = 0.0
    private var stdShouldReset = false

    fun stdAppendNumber(num: String) {
        if (_stdDisplayValue.value == "0" || stdShouldReset) {
            _stdDisplayValue.value = num
            stdShouldReset = false
        } else if (_stdDisplayValue.value.length < 12) {
            _stdDisplayValue.value += num
        }
    }

    fun stdAppendDot() {
        if (stdShouldReset) {
            _stdDisplayValue.value = "0"
            stdShouldReset = false
        }
        if (!_stdDisplayValue.value.contains(".")) {
            _stdDisplayValue.value += "."
        }
    }

    fun stdAppendOperator(op: String) {
        stdShouldReset = false
        _stdCurrentExpression.value += "${_stdDisplayValue.value} $op "
        _stdDisplayValue.value = "0"
    }

    fun stdClearAll() {
        _stdDisplayValue.value = "0"
        _stdCurrentExpression.value = ""
        stdShouldReset = false
    }

    fun stdDeleteLast() {
        _stdDisplayValue.value = if (_stdDisplayValue.value.length > 1) {
            _stdDisplayValue.value.dropLast(1)
        } else {
            "0"
        }
    }

    fun stdToggleSign() {
        if (_stdDisplayValue.value != "0") {
            _stdDisplayValue.value = if (_stdDisplayValue.value.startsWith("-")) {
                _stdDisplayValue.value.substring(1)
            } else {
                "-${_stdDisplayValue.value}"
            }
        }
    }

    fun stdCalculatePercentage() {
        val current = _stdDisplayValue.value.toDoubleOrNull() ?: return
        val result = current / 100.0
        _stdDisplayValue.value = ExpressionEvaluator.formatNumber(result)
    }

    fun stdCalculate() {
        if (_stdCurrentExpression.value.isEmpty()) return
        val fullExpr = _stdCurrentExpression.value + _stdDisplayValue.value
        val evalResult = ExpressionEvaluator.evaluateStandard(fullExpr)

        evalResult.onSuccess { res ->
            val formatted = ExpressionEvaluator.formatNumber(res)
            _stdDisplayValue.value = formatted
            _stdCurrentExpression.value = ""
            stdShouldReset = true

            viewModelScope.launch {
                historyDao.insertHistoryItem(
                    CalcHistoryItem(
                        mode = "STANDARD",
                        expression = fullExpr,
                        result = formatted
                    )
                )
            }
        }.onFailure {
            _stdDisplayValue.value = "Error"
            _stdCurrentExpression.value = ""
            stdShouldReset = true
        }
    }

    fun stdMemoryClear() {
        stdMemoryValue = 0.0
    }

    fun stdMemoryAdd() {
        val current = _stdDisplayValue.value.toDoubleOrNull() ?: 0.0
        stdMemoryValue += current
        stdShouldReset = true
    }

    fun stdMemorySubtract() {
        val current = _stdDisplayValue.value.toDoubleOrNull() ?: 0.0
        stdMemoryValue -= current
        stdShouldReset = true
    }

    fun stdMemoryRecall() {
        _stdDisplayValue.value = ExpressionEvaluator.formatNumber(stdMemoryValue)
        stdShouldReset = true
    }

    // ==========================================
    // SCIENTIFIC CALCULATOR
    // ==========================================
    private val _sciDisplayValue = MutableStateFlow("0")
    val sciDisplayValue: StateFlow<String> = _sciDisplayValue.asStateFlow()

    private val _sciCurrentExpression = MutableStateFlow("")
    val sciCurrentExpression: StateFlow<String> = _sciCurrentExpression.asStateFlow()

    private val _isRadMode = MutableStateFlow(true)
    val isRadMode: StateFlow<Boolean> = _isRadMode.asStateFlow()

    private var sciShouldReset = false

    fun sciToggleRadDeg() {
        _isRadMode.value = !_isRadMode.value
    }

    fun sciAppendNum(num: String) {
        if (_sciDisplayValue.value == "0" || sciShouldReset) {
            _sciDisplayValue.value = num
        } else {
            _sciDisplayValue.value += num
        }
        sciShouldReset = false
    }

    fun sciAppendDot() {
        if (sciShouldReset) {
            _sciDisplayValue.value = "0"
            sciShouldReset = false
        }
        if (!_sciDisplayValue.value.contains(".")) {
            _sciDisplayValue.value += "."
        }
    }

    fun sciAppendChar(char: String) {
        _sciDisplayValue.value = if (_sciDisplayValue.value == "0" && char != "-") {
            char
        } else {
            "${_sciDisplayValue.value} $char "
        }
        sciShouldReset = false
    }

    fun sciAppendFunc(f: String) {
        _sciDisplayValue.value = if (_sciDisplayValue.value == "0" || sciShouldReset) {
            f
        } else {
            _sciDisplayValue.value + f
        }
        sciShouldReset = false
    }

    fun sciAppendConst(c: String) {
        _sciDisplayValue.value = if (_sciDisplayValue.value == "0" || sciShouldReset) {
            c
        } else {
            _sciDisplayValue.value + c
        }
        sciShouldReset = false
    }

    fun sciSmartBracket() {
        val current = _sciDisplayValue.value
        val openCount = current.count { it == '(' }
        val closeCount = current.count { it == ')' }

        _sciDisplayValue.value = if (current == "0" || sciShouldReset) {
            "("
        } else if (openCount > closeCount && current.last().isDigit()) {
            "$current)"
        } else {
            "$current("
        }
        sciShouldReset = false
    }

    fun sciClearAll() {
        _sciDisplayValue.value = "0"
        _sciCurrentExpression.value = ""
        sciShouldReset = false
    }

    fun sciDeleteLast() {
        val trimmed = _sciDisplayValue.value.trimEnd()
        _sciDisplayValue.value = if (trimmed.length > 1) {
            trimmed.dropLast(1).trimEnd().ifEmpty { "0" }
        } else {
            "0"
        }
    }

    fun sciCalculate() {
        val expr = _sciDisplayValue.value
        val result = ExpressionEvaluator.evaluateScientific(expr, _isRadMode.value)

        result.onSuccess { res ->
            val formatted = ExpressionEvaluator.formatNumber(res)
            _sciCurrentExpression.value = "$expr ="
            _sciDisplayValue.value = formatted
            sciShouldReset = true

            viewModelScope.launch {
                historyDao.insertHistoryItem(
                    CalcHistoryItem(
                        mode = "SCIENTIFIC",
                        expression = expr,
                        result = formatted
                    )
                )
            }
        }.onFailure {
            _sciDisplayValue.value = "Error"
            sciShouldReset = true
        }
    }

    // ==========================================
    // CURRENCY CONVERTER
    // ==========================================
    private val _curr1 = MutableStateFlow("USD")
    val curr1: StateFlow<String> = _curr1.asStateFlow()

    private val _curr2 = MutableStateFlow("EUR")
    val curr2: StateFlow<String> = _curr2.asStateFlow()

    private val _currVal1 = MutableStateFlow("100")
    val currVal1: StateFlow<String> = _currVal1.asStateFlow()

    private val _currVal2 = MutableStateFlow("0")
    val currVal2: StateFlow<String> = _currVal2.asStateFlow()

    private val _currActiveBox = MutableStateFlow(1)
    val currActiveBox: StateFlow<Int> = _currActiveBox.asStateFlow()

    private val _currRates = MutableStateFlow<Map<String, Double>>(FALLBACK_RATES)
    val currRates: StateFlow<Map<String, Double>> = _currRates.asStateFlow()

    private val _lastRatesUpdateTime = MutableStateFlow("Loading...")
    val lastRatesUpdateTime: StateFlow<String> = _lastRatesUpdateTime.asStateFlow()

    init {
        loadLiveCurrencyRates()
        calculateCurrency()
    }

    fun loadLiveCurrencyRates() {
        viewModelScope.launch {
            val result = currencyRepo.fetchRates()
            _currRates.value = result.rates
            _lastRatesUpdateTime.value = result.lastUpdateTime
            calculateCurrency()
        }
    }

    fun setActiveCurrBox(box: Int) {
        _currActiveBox.value = box
    }

    fun selectCurrency(code: String, targetBox: Int) {
        if (targetBox == 1) {
            _curr1.value = code
        } else {
            _curr2.value = code
        }
        calculateCurrency()
    }

    fun currSwapCurrencies() {
        val tempCode = _curr1.value
        _curr1.value = _curr2.value
        _curr2.value = tempCode

        val tempVal = _currVal1.value
        _currVal1.value = _currVal2.value
        _currVal2.value = tempVal

        calculateCurrency()
    }

    fun currAppendNum(num: String) {
        if (_currActiveBox.value == 1) {
            _currVal1.value = if (_currVal1.value == "0") num else _currVal1.value + num
        } else {
            _currVal2.value = if (_currVal2.value == "0") num else _currVal2.value + num
        }
        calculateCurrency()
    }

    fun currAppendDot() {
        if (_currActiveBox.value == 1) {
            if (!_currVal1.value.contains(".")) _currVal1.value += "."
        } else {
            if (!_currVal2.value.contains(".")) _currVal2.value += "."
        }
        calculateCurrency()
    }

    fun currDeleteLast() {
        if (_currActiveBox.value == 1) {
            _currVal1.value = if (_currVal1.value.length > 1) _currVal1.value.dropLast(1) else "0"
        } else {
            _currVal2.value = if (_currVal2.value.length > 1) _currVal2.value.dropLast(1) else "0"
        }
        calculateCurrency()
    }

    fun currClearAll() {
        if (_currActiveBox.value == 1) {
            _currVal1.value = "0"
        } else {
            _currVal2.value = "0"
        }
        calculateCurrency()
    }

    private fun calculateCurrency() {
        val rates = _currRates.value
        val r1 = rates[_curr1.value] ?: 1.0
        val r2 = rates[_curr2.value] ?: 1.0
        val factor = r2 / r1

        if (_currActiveBox.value == 1) {
            val v1 = _currVal1.value.toDoubleOrNull() ?: 0.0
            val v2 = (round(v1 * factor * 100.0) / 100.0)
            _currVal2.value = BigDecimal(v2).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()
        } else {
            val v2 = _currVal2.value.toDoubleOrNull() ?: 0.0
            val v1 = if (factor != 0.0) (round((v2 / factor) * 100.0) / 100.0) else 0.0
            _currVal1.value = BigDecimal(v1).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()
        }
    }

    // ==========================================
    // MASS CONVERTER
    // ==========================================
    private val massFactors = mapOf(
        "kg" to 1.0,
        "lb" to 2.20462262185,
        "g" to 1000.0,
        "oz" to 35.27396195
    )

    private val _massActiveUnit = MutableStateFlow("kg")
    val massActiveUnit: StateFlow<String> = _massActiveUnit.asStateFlow()

    private val _massValues = MutableStateFlow(
        mapOf("kg" to "1", "lb" to "2.2046", "g" to "1000", "oz" to "35.274")
    )
    val massValues: StateFlow<Map<String, String>> = _massValues.asStateFlow()

    fun setActiveMassUnit(unit: String) {
        _massActiveUnit.value = unit
    }

    fun massAppendNum(num: String) {
        val current = _massValues.value[_massActiveUnit.value] ?: "0"
        val updated = if (current == "0") num else current + num
        val newMap = _massValues.value.toMutableMap()
        newMap[_massActiveUnit.value] = updated
        _massValues.value = newMap
        calculateMass()
    }

    fun massAppendDot() {
        val current = _massValues.value[_massActiveUnit.value] ?: "0"
        if (!current.contains(".")) {
            val newMap = _massValues.value.toMutableMap()
            newMap[_massActiveUnit.value] = "$current."
            _massValues.value = newMap
        }
    }

    fun massDeleteLast() {
        val current = _massValues.value[_massActiveUnit.value] ?: "0"
        val updated = if (current.length > 1) current.dropLast(1) else "0"
        val newMap = _massValues.value.toMutableMap()
        newMap[_massActiveUnit.value] = updated
        _massValues.value = newMap
        calculateMass()
    }

    fun massClearAll() {
        val newMap = _massValues.value.toMutableMap()
        newMap[_massActiveUnit.value] = "0"
        _massValues.value = newMap
        calculateMass()
    }

    private fun calculateMass() {
        val unit = _massActiveUnit.value
        val currentVal = _massValues.value[unit]?.toDoubleOrNull() ?: 0.0
        val baseFactor = massFactors[unit] ?: 1.0
        val base = currentVal / baseFactor

        val newMap = _massValues.value.toMutableMap()
        for ((u, factor) in massFactors) {
            if (u != unit) {
                val converted = round(base * factor * 10000.0) / 10000.0
                newMap[u] = ExpressionEvaluator.formatNumber(converted)
            }
        }
        _massValues.value = newMap
    }

    // ==========================================
    // TIP CALCULATOR
    // ==========================================
    private val _tipActiveField = MutableStateFlow("bill")
    val tipActiveField: StateFlow<String> = _tipActiveField.asStateFlow()

    private val _tipValues = MutableStateFlow(
        mapOf("bill" to "150", "people" to "3", "tip" to "20.00", "tax" to "4.00")
    )
    val tipValues: StateFlow<Map<String, String>> = _tipValues.asStateFlow()

    private val _tipType = MutableStateFlow("percent") // "percent" or "value"
    val tipType: StateFlow<String> = _tipType.asStateFlow()

    private val _taxType = MutableStateFlow("percent") // "percent" or "value"
    val taxType: StateFlow<String> = _taxType.asStateFlow()

    private val _noTipOnTax = MutableStateFlow(false)
    val noTipOnTax: StateFlow<Boolean> = _noTipOnTax.asStateFlow()

    private val _tipTotal = MutableStateFlow("187.20")
    val tipTotal: StateFlow<String> = _tipTotal.asStateFlow()

    private val _tipPerPerson = MutableStateFlow("31.20")
    val tipPerPerson: StateFlow<String> = _tipPerPerson.asStateFlow()

    fun setActiveTipField(field: String) {
        _tipActiveField.value = field
    }

    fun setTipType(type: String) {
        _tipType.value = type
        calculateTip()
    }

    fun setTaxType(type: String) {
        _taxType.value = type
        calculateTip()
    }

    fun toggleNoTipOnTax() {
        _noTipOnTax.value = !_noTipOnTax.value
        calculateTip()
    }

    fun tipAppendNum(num: String) {
        val field = _tipActiveField.value
        val current = _tipValues.value[field] ?: "0"
        val updated = if (current == "0") num else current + num
        val newMap = _tipValues.value.toMutableMap()
        newMap[field] = updated
        _tipValues.value = newMap
        calculateTip()
    }

    fun tipAppendDot() {
        val field = _tipActiveField.value
        val current = _tipValues.value[field] ?: "0"
        if (!current.contains(".")) {
            val newMap = _tipValues.value.toMutableMap()
            newMap[field] = "$current."
            _tipValues.value = newMap
            calculateTip()
        }
    }

    fun tipDeleteLast() {
        val field = _tipActiveField.value
        val current = _tipValues.value[field] ?: "0"
        val updated = if (current.length > 1) current.dropLast(1) else "0"
        val newMap = _tipValues.value.toMutableMap()
        newMap[field] = updated
        _tipValues.value = newMap
        calculateTip()
    }

    fun tipClearAll() {
        val field = _tipActiveField.value
        val newMap = _tipValues.value.toMutableMap()
        newMap[field] = "0"
        _tipValues.value = newMap
        calculateTip()
    }

    private fun calculateTip() {
        val bill = _tipValues.value["bill"]?.toDoubleOrNull() ?: 0.0
        var people = _tipValues.value["people"]?.toDoubleOrNull() ?: 1.0
        if (people <= 0) people = 1.0

        val rawTip = _tipValues.value["tip"]?.toDoubleOrNull() ?: 0.0
        val rawTax = _tipValues.value["tax"]?.toDoubleOrNull() ?: 0.0

        val taxAmount = if (_taxType.value == "percent") bill * rawTax / 100.0 else rawTax
        val tipBase = if (_noTipOnTax.value) bill else bill + taxAmount
        val tipAmount = if (_tipType.value == "percent") tipBase * rawTip / 100.0 else rawTip

        val total = bill + taxAmount + tipAmount
        val perPerson = total / people

        _tipTotal.value = String.format(java.util.Locale.US, "%.2f", total)
        _tipPerPerson.value = String.format(java.util.Locale.US, "%.2f", perPerson)
    }

    // ==========================================
    // DISCOUNT CALCULATOR
    // ==========================================
    private val _discActiveField = MutableStateFlow("price")
    val discActiveField: StateFlow<String> = _discActiveField.asStateFlow()

    private val _discValues = MutableStateFlow(
        mapOf("price" to "159.99", "disc" to "20", "tax" to "10")
    )
    val discValues: StateFlow<Map<String, String>> = _discValues.asStateFlow()

    private val _discSaved = MutableStateFlow("32.00")
    val discSaved: StateFlow<String> = _discSaved.asStateFlow()

    private val _discTotal = MutableStateFlow("140.79")
    val discTotal: StateFlow<String> = _discTotal.asStateFlow()

    fun setActiveDiscField(field: String) {
        _discActiveField.value = field
    }

    fun discAppendNum(num: String) {
        val field = _discActiveField.value
        val current = _discValues.value[field] ?: "0"
        val updated = if (current == "0") num else current + num
        val newMap = _discValues.value.toMutableMap()
        newMap[field] = updated
        _discValues.value = newMap
        calculateDiscount()
    }

    fun discAppendDot() {
        val field = _discActiveField.value
        val current = _discValues.value[field] ?: "0"
        if (!current.contains(".")) {
            val newMap = _discValues.value.toMutableMap()
            newMap[field] = "$current."
            _discValues.value = newMap
            calculateDiscount()
        }
    }

    fun discDeleteLast() {
        val field = _discActiveField.value
        val current = _discValues.value[field] ?: "0"
        val updated = if (current.length > 1) current.dropLast(1) else "0"
        val newMap = _discValues.value.toMutableMap()
        newMap[field] = updated
        _discValues.value = newMap
        calculateDiscount()
    }

    fun discClearAll() {
        val field = _discActiveField.value
        val newMap = _discValues.value.toMutableMap()
        newMap[field] = "0"
        _discValues.value = newMap
        calculateDiscount()
    }

    private fun calculateDiscount() {
        val price = _discValues.value["price"]?.toDoubleOrNull() ?: 0.0
        val discPct = _discValues.value["disc"]?.toDoubleOrNull() ?: 0.0
        val taxPct = _discValues.value["tax"]?.toDoubleOrNull() ?: 0.0

        val saved = price * discPct / 100.0
        val discountedPrice = price - saved
        val taxAmount = discountedPrice * taxPct / 100.0
        val total = discountedPrice + taxAmount

        _discSaved.value = String.format(java.util.Locale.US, "%.2f", saved)
        _discTotal.value = String.format(java.util.Locale.US, "%.2f", total)
    }

    fun massToggleSign() {
        val current = _massValues.value[_massActiveUnit.value] ?: "0"
        if (current != "0") {
            val updated = if (current.startsWith("-")) current.substring(1) else "-$current"
            val newMap = _massValues.value.toMutableMap()
            newMap[_massActiveUnit.value] = updated
            _massValues.value = newMap
            calculateMass()
        }
    }

    fun tipToggleSign() {
        val field = _tipActiveField.value
        val current = _tipValues.value[field] ?: "0"
        if (current != "0") {
            val updated = if (current.startsWith("-")) current.substring(1) else "-$current"
            val newMap = _tipValues.value.toMutableMap()
            newMap[field] = updated
            _tipValues.value = newMap
            calculateTip()
        }
    }

    fun discToggleSign() {
        val field = _discActiveField.value
        val current = _discValues.value[field] ?: "0"
        if (current != "0") {
            val updated = if (current.startsWith("-")) current.substring(1) else "-$current"
            val newMap = _discValues.value.toMutableMap()
            newMap[field] = updated
            _discValues.value = newMap
            calculateDiscount()
        }
    }
}
