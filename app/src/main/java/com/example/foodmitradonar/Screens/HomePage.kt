package com.example.foodmitra.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.foodmitradonar.Screens.Supabase.Donation
import com.example.foodmitradonar.Screens.Supabase.SupabaseManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HomePage() {
    var donations by remember { mutableStateOf<List<Donation>>(emptyList()) }

    LaunchedEffect(Unit) {
        // Fetch donations asynchronously
        CoroutineScope(Dispatchers.IO).launch {
            donations = SupabaseManager.getDonations()
        }
    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(donations.size) { index ->
            val item = donations[index]
            val image = item.image_url
            val qty = item.quantity
            val loc = item.location

            Column(modifier = Modifier.padding(8.dp)) {
                if (image.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(image),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                }
                Text("Quantity: $qty")
                Text("Location: $loc")
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}
