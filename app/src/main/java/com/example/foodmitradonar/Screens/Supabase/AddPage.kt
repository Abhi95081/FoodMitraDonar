package com.example.foodmitra.Screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.foodmitradonar.Screens.Supabase.SupabaseManager
import kotlinx.coroutines.launch

@Composable
fun AddPage(onUploadSuccess: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var quantity by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        imageUri = it
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { imagePickerLauncher.launch("image/*") }) {
            Text("Select Image")
        }

        imageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 8.dp)
            )
        }

        OutlinedTextField(value = quantity, onValueChange = { quantity = it }, label = { Text("Quantity") })
        OutlinedTextField(value = duration, onValueChange = { duration = it }, label = { Text("Duration") })
        OutlinedTextField(value = mobile, onValueChange = { mobile = it }, label = { Text("Mobile Number") })
        OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Location") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (imageUri == null || quantity.isBlank() || duration.isBlank() || mobile.isBlank()) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@Button
            }

            scope.launch {
                val success = SupabaseManager.uploadDonation(
                    imageUri!!,
                    quantity,
                    duration,
                    mobile,
                    location,
                    context
                )
                if (success) {
                    Toast.makeText(context, "Uploaded successfully", Toast.LENGTH_SHORT).show()
                    onUploadSuccess()
                } else {
                    Toast.makeText(context, "Upload failed", Toast.LENGTH_SHORT).show()
                }
            }
        }) {
            Text("Submit")
        }
    }
}
