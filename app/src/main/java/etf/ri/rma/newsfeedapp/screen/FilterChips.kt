package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun FilterChips(
    selectedCategory: String,
    onSelected: (String) -> Unit,
    onMoreFiltersClicked: () -> Unit
) {
    val firstRowCategories = listOf("All", "Politika", "Sport")
    val secondRowCategories = listOf("Nauka/tehnologija", "Vremenska prognoza")
    val tags = listOf("filter_chip_all", "filter_chip_pol", "filter_chip_spo", "filter_chip_sci", "filter_chip_wea")

    Column(modifier = Modifier.padding(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            firstRowCategories.forEachIndexed { index, category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { if (selectedCategory != category) onSelected(category) },
                    label = { Text(category) },
                    modifier = Modifier.testTag(tags[index])
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            secondRowCategories.forEachIndexed { index, category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { if (selectedCategory != category) onSelected(category) },
                    label = { Text(category) },
                    modifier = Modifier.testTag(tags[index + 3])
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Treći red: "Više filtera ..."
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = false,
                onClick = onMoreFiltersClicked,
                label = { Text("Više filtera ...") },
                modifier = Modifier.testTag("filter_chip_more")
            )
        }
    }
}
