package com.example.foodmitra.Screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.services.Account
import io.appwrite.services.Databases
import io.appwrite.services.Storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import androidx.core.net.toFile
import com.example.foodmitradonar.Screens.Appwrite.uploadToAppwrite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPage() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var description by remember { mutableStateOf("") }
    var spoilTime by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Donate Food", style = MaterialTheme.typography.titleLarge)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                AsyncImage(
                    model = ImageRequest.Builder(context).data(imageUri).build(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text("Click to select food image")
            }
        }

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Food Description") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = spoilTime,
            onValueChange = { spoilTime = it },
            label = { Text("Spoil Time (e.g. 2 hrs)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = mobile,
            onValueChange = { mobile = it },
            label = { Text("Mobile Number") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (imageUri != null && description.isNotBlank() && spoilTime.isNotBlank() && mobile.isNotBlank() && location.isNotBlank()) {
                    scope.launch {
                        uploadToAppwrite(
                            context = context,
                            imageUri = imageUri!!,
                            description = description,
                            spoilTime = spoilTime,
                            mobile = mobile,
                            location = location
                        )
                    }
                } else {
                    Toast.makeText(context, "Fill all fields & select image", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Submit")
        }
    }
}
