package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

/**
 * Sada korištenje trojki: (prikazniNaziv, apiKod, testTag).
 * UI prikazuje 'displayName', a kada se klikne, prosljeđujemo 'apiCode' u ViewModel.
 */
@Composable
fun CategoryFilters(
    selectedCategory: String,                    // Očekuje se engleski apiKod
    onCategorySelected: (String) -> Unit         // Prima engleski apiKod
) {
    val categories = listOf(
        Triple("Sve",        "general",       "filter_chip_general"),
        Triple("Zdravlje",   "health",        "filter_chip_health"),
        Triple("Nauka",      "science",       "filter_chip_science"),
        Triple("Sport",      "sports",        "filter_chip_sports"),
        Triple("Tehnologija","technology",    "filter_chip_technology")
    )

    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            categories.take(3).forEach { (displayName, apiCode, tag) ->
                CategoryChip(
                    selected = selectedCategory,
                    categoryCode = apiCode,
                    displayName = displayName,
                    testTag = tag,
                    onSelected = onCategorySelected
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            categories.drop(3).forEach { (displayName, apiCode, tag) ->
                CategoryChip(
                    selected = selectedCategory,
                    categoryCode = apiCode,
                    displayName = displayName,
                    testTag = tag,
                    onSelected = onCategorySelected
                )
            }
        }
    }
}

@Composable
private fun CategoryChip(
    selected: String,                // Trenutno odabrani apiKod
    categoryCode: String,            // apiKod koji šaljemo u ViewModel (npr. "science")
    displayName: String,             // Prikazni naziv korisniku (npr. "Nauka")
    testTag: String,
    onSelected: (String) -> Unit     // Poziva se s apiKod-om
) {
    FilterChip(
        selected = (selected == categoryCode),
        onClick = { onSelected(categoryCode) },
        label = { Text(displayName) },
        modifier = Modifier.testTag(testTag)
    )
}
