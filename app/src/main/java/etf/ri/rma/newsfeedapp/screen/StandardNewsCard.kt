package etf.ri.rma.newsfeedapp.screen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import etf.ri.rma.newsfeedapp.model.NewsItem
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import etf.ri.rma.newsfeedapp.R

@Composable
fun StandardNewsCard(newsItem: NewsItem, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {

            // Prikaz slike preko Coil-a ako postoji URL
            if (!newsItem.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = newsItem.imageUrl,
                    contentDescription = "News image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            } else {
                // Fallback slika ako URL nije validan
                Image(
                    painter = painterResource(id = R.drawable.slikavijesti),
                    contentDescription = "Default news image",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = newsItem.title,
                    maxLines = 2,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = newsItem.snippet,
                    maxLines = 2,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${newsItem.source} | ${newsItem.publishedDate}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}
