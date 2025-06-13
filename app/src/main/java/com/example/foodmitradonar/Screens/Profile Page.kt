package com.example.foodmitra.Screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.foodmitradonar.SharedPref.SharedPrefHelper
import androidx.core.net.toUri

@Composable
fun ProfilePage(navController: NavController) {
    val context = LocalContext.current
    val sharedPref = remember { SharedPrefHelper(context) }

    var name by remember { mutableStateOf(sharedPref.getDonorName()) }
    var mobile by remember { mutableStateOf(sharedPref.getDonorMobile()) }
    var imageUri by remember { mutableStateOf(sharedPref.getDonorImageUri().toUri()) }

    var showEditDialog by remember { mutableStateOf(false) }
    var isDarkMode by remember { mutableStateOf(false) }

    val backgroundColor = if (isDarkMode) Color.Black else Color.White
    val textColor = if (isDarkMode) Color.White else Color.Black

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            sharedPref.saveDonor(name, mobile, it.toString())
            Toast.makeText(context, "Profile photo updated", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "My Profile",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Image(
            painter = rememberAsyncImagePainter(model = imageUri),
            contentDescription = "Profile Photo",
            modifier = Modifier
                .size(140.dp)
                .background(Color.Gray, shape = CircleShape)
                .clickable { imagePickerLauncher.launch("image/*") },
            contentScale = ContentScale.Crop
        )

        TextButton(onClick = { imagePickerLauncher.launch("image/*") }) {
            Text("Change Photo", color = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = Modifier.height(24.dp))

        ProfileField("Name", name, textColor)
        ProfileField("Mobile Number", mobile, textColor)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showEditDialog = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Edit, contentDescription = "Edit")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Edit Profile")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Dark Mode", fontSize = 16.sp, color = textColor, modifier = Modifier.weight(1f))
            Switch(
                checked = isDarkMode,
                onCheckedChange = { isDarkMode = it }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                sharedPref.clearDonorData()
                Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                navController.navigate("login") {
                    popUpTo("bottom_nav") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Logout", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))
    }

    if (showEditDialog) {
        EditProfileDialog(
            initialName = name,
            initialMobile = mobile,
            onDismiss = { showEditDialog = false },
            onSave = { newName, newMobile ->
                sharedPref.saveDonor(newName, newMobile, imageUri.toString())
                name = newName
                mobile = newMobile
                showEditDialog = false
                Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
fun ProfileField(label: String, value: String, textColor: Color) {
    Text(label, fontSize = 14.sp, color = textColor.copy(alpha = 0.6f))
    Text(value, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = textColor)
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun EditProfileDialog(
    initialName: String,
    initialMobile: String,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var nameState by remember { mutableStateOf(TextFieldValue(initialName)) }
    var mobileState by remember { mutableStateOf(TextFieldValue(initialMobile)) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onSave(nameState.text, mobileState.text)
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Edit Profile") },
        text = {
            Column {
                OutlinedTextField(
                    value = nameState,
                    onValueChange = { nameState = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = mobileState,
                    onValueChange = { mobileState = it },
                    label = { Text("Mobile Number") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}
