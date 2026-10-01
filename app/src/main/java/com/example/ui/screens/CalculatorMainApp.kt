package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.AppMode
import com.example.ui.CalculatorViewModel
import com.example.ui.components.CalculatorTopBar
import com.example.ui.components.CurrencyPickerSheet
import com.example.ui.components.HistoryDrawerContent
import com.example.ui.components.MenuDrawerContent
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorMainApp(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    val currentMode by viewModel.currentMode.collectAsStateWithLifecycle()
    val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()

    val stdDisplayValue by viewModel.stdDisplayValue.collectAsStateWithLifecycle()
    val stdCurrentExpression by viewModel.stdCurrentExpression.collectAsStateWithLifecycle()

    val sciDisplayValue by viewModel.sciDisplayValue.collectAsStateWithLifecycle()
    val sciCurrentExpression by viewModel.sciCurrentExpression.collectAsStateWithLifecycle()
    val isRadMode by viewModel.isRadMode.collectAsStateWithLifecycle()

    val curr1 by viewModel.curr1.collectAsStateWithLifecycle()
    val curr2 by viewModel.curr2.collectAsStateWithLifecycle()
    val currVal1 by viewModel.currVal1.collectAsStateWithLifecycle()
    val currVal2 by viewModel.currVal2.collectAsStateWithLifecycle()
    val currActiveBox by viewModel.currActiveBox.collectAsStateWithLifecycle()
    val currRates by viewModel.currRates.collectAsStateWithLifecycle()
    val lastRatesUpdateTime by viewModel.lastRatesUpdateTime.collectAsStateWithLifecycle()

    val massActiveUnit by viewModel.massActiveUnit.collectAsStateWithLifecycle()
    val massValues by viewModel.massValues.collectAsStateWithLifecycle()

    val tipActiveField by viewModel.tipActiveField.collectAsStateWithLifecycle()
    val tipValues by viewModel.tipValues.collectAsStateWithLifecycle()
    val tipType by viewModel.tipType.collectAsStateWithLifecycle()
    val taxType by viewModel.taxType.collectAsStateWithLifecycle()
    val noTipOnTax by viewModel.noTipOnTax.collectAsStateWithLifecycle()
    val tipTotal by viewModel.tipTotal.collectAsStateWithLifecycle()
    val tipPerPerson by viewModel.tipPerPerson.collectAsStateWithLifecycle()

    val discActiveField by viewModel.discActiveField.collectAsStateWithLifecycle()
    val discValues by viewModel.discValues.collectAsStateWithLifecycle()
    val discSaved by viewModel.discSaved.collectAsStateWithLifecycle()
    val discTotal by viewModel.discTotal.collectAsStateWithLifecycle()

    val standardHistory by viewModel.standardHistory.collectAsStateWithLifecycle()
    val scientificHistory by viewModel.scientificHistory.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var isHistoryDrawerOpen by remember { mutableStateOf(false) }

    var currencyPickerTargetBox by remember { mutableStateOf<Int?>(null) }

    // Android back button handling
    BackHandler(enabled = isHistoryDrawerOpen || drawerState.isOpen || currentMode != AppMode.STANDARD) {
        when {
            isHistoryDrawerOpen -> isHistoryDrawerOpen = false
            drawerState.isOpen -> coroutineScope.launch { drawerState.close() }
            currentMode != AppMode.STANDARD -> viewModel.switchMode(AppMode.STANDARD)
        }
    }

    val bgGradient = if (isDarkMode) DarkBgGradient else LightBgGradient
    val cardContainerBg = if (isDarkMode) Color(0x9912121A) else Color(0xCCFFFFFF)
    val cardBorder = if (isDarkMode) DarkBorder else LightBorder

    // Currency picker modal bottom sheet
    if (currencyPickerTargetBox != null) {
        CurrencyPickerSheet(
            targetBox = currencyPickerTargetBox!!,
            isDarkMode = isDarkMode,
            onCurrencySelected = { code, box ->
                viewModel.selectCurrency(code, box)
            },
            onDismiss = { currencyPickerTargetBox = null }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = !isHistoryDrawerOpen,
        drawerContent = {
            MenuDrawerContent(
                currentMode = currentMode,
                isDarkMode = isDarkMode,
                onSelectMode = { mode ->
                    viewModel.switchMode(mode)
                },
                onToggleDarkMode = {
                    viewModel.toggleDarkMode()
                },
                onCloseDrawer = {
                    coroutineScope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Brush.linearGradient(bgGradient)),
            contentAlignment = Alignment.Center
        ) {
            // Calculator Body (max width for tablets/desktop)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 440.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                ) {
                    // Top Bar
                    CalculatorTopBar(
                        currentMode = currentMode,
                        isDarkMode = isDarkMode,
                        onMenuClick = {
                            coroutineScope.launch { drawerState.open() }
                        },
                        onHistoryClick = {
                            isHistoryDrawerOpen = true
                        }
                    )

                    // Active screen content
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        AnimatedContent(
                            targetState = currentMode,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "calc_mode_transition"
                        ) { mode ->
                            when (mode) {
                                AppMode.STANDARD -> StandardCalcScreen(
                                    viewModel = viewModel,
                                    displayValue = stdDisplayValue,
                                    currentExpression = stdCurrentExpression,
                                    isDarkMode = isDarkMode
                                )
                                AppMode.SCIENTIFIC -> ScientificCalcScreen(
                                    viewModel = viewModel,
                                    displayValue = sciDisplayValue,
                                    currentExpression = sciCurrentExpression,
                                    isRadMode = isRadMode,
                                    isDarkMode = isDarkMode
                                )
                                AppMode.CURRENCY -> CurrencyConverterScreen(
                                    viewModel = viewModel,
                                    curr1 = curr1,
                                    curr2 = curr2,
                                    val1 = currVal1,
                                    val2 = currVal2,
                                    activeBox = currActiveBox,
                                    rates = currRates,
                                    updateTime = lastRatesUpdateTime,
                                    isDarkMode = isDarkMode,
                                    onOpenPicker = { box -> currencyPickerTargetBox = box }
                                )
                                AppMode.MASS -> MassConverterScreen(
                                    viewModel = viewModel,
                                    activeUnit = massActiveUnit,
                                    massValues = massValues,
                                    isDarkMode = isDarkMode
                                )
                                AppMode.TIP -> TipCalculatorScreen(
                                    viewModel = viewModel,
                                    activeField = tipActiveField,
                                    tipValues = tipValues,
                                    tipType = tipType,
                                    taxType = taxType,
                                    noTipOnTax = noTipOnTax,
                                    tipTotal = tipTotal,
                                    tipPerPerson = tipPerPerson,
                                    isDarkMode = isDarkMode
                                )
                                AppMode.DISCOUNT -> DiscountCalculatorScreen(
                                    viewModel = viewModel,
                                    activeField = discActiveField,
                                    discValues = discValues,
                                    savedAmount = discSaved,
                                    totalPrice = discTotal,
                                    isDarkMode = isDarkMode
                                )
                            }
                        }
                    }
                }

                // History Drawer Sliding overlay from right
                if (isHistoryDrawerOpen) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        HistoryDrawerContent(
                            currentMode = currentMode,
                            historyList = if (currentMode == AppMode.SCIENTIFIC) scientificHistory else standardHistory,
                            isDarkMode = isDarkMode,
                            onClearHistory = { viewModel.clearCurrentHistory() },
                            onCloseDrawer = { isHistoryDrawerOpen = false },
                            onSelectHistoryItem = { result ->
                                if (currentMode == AppMode.STANDARD) {
                                    viewModel.stdClearAll()
                                    viewModel.stdAppendNumber(result)
                                } else if (currentMode == AppMode.SCIENTIFIC) {
                                    viewModel.sciClearAll()
                                    viewModel.sciAppendNum(result)
                                }
                                isHistoryDrawerOpen = false
                            }
                        )
                    }
                }
            }
        }
    }
}
