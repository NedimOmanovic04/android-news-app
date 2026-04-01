

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import etf.ri.rma.newsfeedapp.viewmodel.DateRange
import etf.ri.rma.newsfeedapp.viewmodel.NewsFeedViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun FilterScreen(navController: NavHostController) {
    val activity = LocalActivity.current
    val viewModel: NewsFeedViewModel = viewModel(viewModelStoreOwner = activity as ViewModelStoreOwner)

    var localCategory by remember { mutableStateOf(viewModel.selectedCategory) }
    var localStartDate by remember { mutableStateOf(viewModel.selectedDateRange?.start) }
    var localEndDate by remember { mutableStateOf(viewModel.selectedDateRange?.end) }
    val localUnwantedWords = remember { viewModel.unwantedWords.toMutableStateList() }

    var showStartPicker by rememberSaveable { mutableStateOf(false) }
    var showEndPicker by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        CategoryFilters(
            selectedCategory = localCategory,
            onCategorySelected = { localCategory = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        DateRangePickerSection(
            startDate = localStartDate,
            endDate = localEndDate,
            onDateSelected = { start, end ->
                localStartDate = start
                localEndDate = end
            },
            showStartPicker = { showStartPicker = true },
            showEndPicker = { showEndPicker = true }
        )

        if (showStartPicker) {
            CustomDatePickerDialog(
                onDismissRequest = { showStartPicker = false },
                onDateSelected = {
                    localStartDate = it
                    showStartPicker = false
                    showEndPicker = true
                }
            )
        }

        if (showEndPicker) {
            CustomDatePickerDialog(
                onDismissRequest = { showEndPicker = false },
                onDateSelected = {
                    localEndDate = it
                    showEndPicker = false
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        UnwantedWordsFilter(
            words = localUnwantedWords,
            onAddWord = { word ->
                if (word.isNotBlank() && localUnwantedWords.none { it.equals(word, true) }) {
                    localUnwantedWords.add(word)
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("filter_cancel_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text("Otkaži")
            }

            Button(
                onClick = {
                    viewModel.updateFilters(
                        category = "general", // "All" je UI labela, mapiramo ručno
                        dateRange = null,
                        words = emptyList()
                    )
                    navController.popBackStack()
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("filter_reset_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text("Resetiraj sve")
            }

            Button(
                onClick = {
                    viewModel.updateFilters(
                        category = if (localCategory == "All") "general" else localCategory,
                        dateRange = if (localStartDate != null && localEndDate != null) {
                            val orderedStart = minOf(localStartDate!!, localEndDate!!)
                            val orderedEnd = maxOf(localStartDate!!, localEndDate!!)
                            DateRange(orderedStart, orderedEnd)
                        } else null,
                        words = localUnwantedWords.toList()
                    )
                    navController.popBackStack()
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("filter_apply_button")
            ) {
                Text("Primijeni")
            }
        }
    }
}

@Composable
private fun CategoryFilters(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf(
        "All" to "filter_chip_all",
        "Politika" to "filter_chip_pol",
        "Sport" to "filter_chip_spo",
        "Nauka/tehnologija" to "filter_chip_sci",
        "Vremenska prognoza" to "filter_chip_wea"
    )

    Column {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            categories.take(3).forEach { (cat, tag) ->
                CategoryFilterChip(
                    selected = selectedCategory,
                    category = cat,
                    testTag = tag,
                    onSelected = onCategorySelected
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            categories.drop(3).forEach { (cat, tag) ->
                CategoryFilterChip(
                    selected = selectedCategory,
                    category = cat,
                    testTag = tag,
                    onSelected = onCategorySelected
                )
            }
        }
    }
}

@Composable
private fun CategoryFilterChip(
    selected: String,
    category: String,
    testTag: String,
    onSelected: (String) -> Unit
) {
    FilterChip(
        selected = (selected == category),
        onClick = { onSelected(category) },
        label = { Text(category) },
        modifier = Modifier.testTag(testTag)
    )
}

@Composable
private fun DateRangePickerSection(
    startDate: LocalDate?,
    endDate: LocalDate?,
    onDateSelected: (LocalDate?, LocalDate?) -> Unit,
    showStartPicker: () -> Unit,
    showEndPicker: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val dateRangeText = remember(startDate, endDate) {
        if (startDate != null && endDate != null) {
            "${startDate.format(dateFormatter)};${endDate.format(dateFormatter)}"
        } else "Odaberite opseg datuma"
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = dateRangeText,
            modifier = Modifier
                .weight(1f)
                .testTag("filter_daterange_display")
        )
        Button(
            onClick = showStartPicker,
            modifier = Modifier.testTag("filter_daterange_button")
        ) {
            Text("Odaberi")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomDatePickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selected = Instant
                            .ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        onDateSelected(selected)
                    }
                }
            ) {
                Text("OK")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
private fun UnwantedWordsFilter(
    words: List<String>,
    onAddWord: (String) -> Unit
) {
    var currentWord by remember { mutableStateOf("") }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = currentWord,
                onValueChange = { currentWord = it },
                modifier = Modifier
                    .weight(1f)
                    .testTag("filter_unwanted_input")
            )
            Button(
                onClick = {
                    onAddWord(currentWord.trim())
                    currentWord = ""
                },
                modifier = Modifier.testTag("filter_unwanted_add_button")
            ) {
                Text("Dodaj")
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("filter_unwanted_list")
        ) {
            items(words, key = { it }) { word ->
                Text(word, modifier = Modifier.padding(8.dp))
            }
        }
    }
}
