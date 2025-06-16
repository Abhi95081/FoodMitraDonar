package com.example.foodmitra.Screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.foodmitradonar.Screens.Supabase.SupabaseManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPage(onUploadSuccess: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var quantity by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("General") }
    var uploading by remember { mutableStateOf(false) }

    val categories = listOf("General", "Groceries", "Leftover", "Cooked Food")
    var expanded by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { imageUri = it }

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

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value = category,
                onValueChange = {},
                readOnly = true,
                label = { Text("Category") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                categories.forEach {
                    DropdownMenuItem(text = { Text(it) }, onClick = {
                        category = it
                        expanded = false
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (imageUri == null || quantity.isBlank() || duration.isBlank() || mobile.isBlank()) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@Button
            }

            uploading = true
            scope.launch {
                val success = SupabaseManager.uploadDonation(
                    imageUri!!,
                    quantity,
                    duration,
                    mobile,
                    location,
                    context,
                    category
                )

                withContext(Dispatchers.Main) {
                    uploading = false
                    if (success) {
                        Toast.makeText(context, "Uploaded successfully", Toast.LENGTH_SHORT).show()
                        imageUri = null
                        quantity = ""
                        duration = ""
                        mobile = ""
                        location = ""
                        category = "General"
                        onUploadSuccess()
                    } else {
                        Toast.makeText(context, "Upload failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }) {
            if (uploading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
            }
            Text(if (uploading) "Uploading..." else "Submit")
        }
    }
}
