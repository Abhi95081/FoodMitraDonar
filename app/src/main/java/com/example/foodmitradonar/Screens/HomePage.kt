package com.example.foodmitra.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
        CoroutineScope(Dispatchers.IO).launch {
            donations = SupabaseManager.getDonations()
        }
    }


    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(donations.size) { index ->
            val item = donations[index]
            Column(modifier = Modifier.padding(8.dp)) {
                if (item.image_url.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(item.image_url),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                }
                Text("Quantity: ${item.quantity}")
                Text("Duration: ${item.duration}")
                Text("Mobile: ${item.mobile}")
                Text("Location: ${item.location}")
                Text("Category: ${item.category}")
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}
