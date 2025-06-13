package com.example.foodmitradonar.Screens

import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foodmitradonar.R
import com.example.foodmitradonar.SharedPref.SharedPrefHelper
import com.example.foodmitradonar.ui.theme.twilio.PHONE
import com.example.foodmitradonar.ui.theme.twilio.SID
import com.example.foodmitradonar.ui.theme.twilio.TOKEN
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonorRegistrationScreen(navController: NavController) {
    val context = LocalContext.current
    var donorName by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    var otpSent by remember { mutableStateOf(false) }
    var otpInput by remember { mutableStateOf("") }
    var generatedOtp by remember { mutableStateOf("") }


    val sharedPref = remember { SharedPrefHelper(context) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        uri?.let {
            val inputStream: InputStream? = context.contentResolver.openInputStream(it)
            bitmap = BitmapFactory.decodeStream(inputStream)
        }
    }

    //https://console.twilio.com/us1/develop/sms/try-it-out/send-an-sms

    fun sendOtp(mobile: String, otp: String) {
        val ACCOUNT_SID = SID
        val AUTH_TOKEN = TOKEN
        val FROM_PHONE = PHONE // Example: "+1415XXXXXXX"
        val TO_PHONE = "+91$mobile"
        val messageBody = "Your OTP for FoodMitra is: $otp"

        Thread {
            try {
                val url = URL("https://api.twilio.com/2010-04-01/Accounts/$ACCOUNT_SID/Messages.json")
                val auth = android.util.Base64.encodeToString(
                    "$ACCOUNT_SID:$AUTH_TOKEN".toByteArray(), android.util.Base64.NO_WRAP
                )

                val postData = "To=${URLEncoder.encode(TO_PHONE, "UTF-8")}" +
                        "&From=${URLEncoder.encode(FROM_PHONE, "UTF-8")}" +
                        "&Body=${URLEncoder.encode(messageBody, "UTF-8")}"

                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Authorization", "Basic $auth")
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                conn.doOutput = true
                conn.outputStream.write(postData.toByteArray(Charsets.UTF_8))

                val responseCode = conn.responseCode
                val response = conn.inputStream.bufferedReader().readText()

                (context as? android.app.Activity)?.runOnUiThread {
                    if (responseCode in 200..299) {
                        Toast.makeText(context, "OTP sent successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Twilio Error: $response", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                (context as? android.app.Activity)?.runOnUiThread {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFFFC))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .background(Color.White, RoundedCornerShape(20.dp))
                .padding(28.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(id = R.drawable.food), contentDescription = null, modifier = Modifier.size(72.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text("Register as a Food Donor", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF2E7D32))
            Text("Let's reduce food waste together.", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clickable { imagePickerLauncher.launch("image/*") }
                    .background(Color(0xFFF0F4F3), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                bitmap?.let {
                    Image(bitmap = it.asImageBitmap(), contentDescription = null, modifier = Modifier.fillMaxSize())
                } ?: Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painter = painterResource(id = R.drawable.upload_icon), contentDescription = null, modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Upload Image", color = Color.Gray, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(value = donorName, onValueChange = { donorName = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), singleLine = true)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = mobileNumber, onValueChange = { if (it.length <= 10 && it.all(Char::isDigit)) mobileNumber = it }, label = { Text("Mobile Number") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), singleLine = true)

            if (otpSent) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = otpInput, onValueChange = { otpInput = it }, label = { Text("Enter OTP") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), singleLine = true)
            }

            Spacer(modifier = Modifier.height(28.dp))
            Button(
                onClick = {
                    if (!otpSent) {
                        if (donorName.isNotBlank() && mobileNumber.length == 10 && bitmap != null) {
                            val otp = (100000..999999).random().toString()
                            generatedOtp = otp
                            sendOtp(mobileNumber, otp)
                            otpSent = true
                        } else {
                            Toast.makeText(context, "Please fill all fields and upload a photo.", Toast.LENGTH_SHORT).show()
                        }
                    } else {


                        if (otpInput == generatedOtp) {
                            sharedPref.saveDonor(donorName, mobileNumber, selectedImageUri.toString())
                            Toast.makeText(context, "Thank you for registering!", Toast.LENGTH_SHORT).show()
                            navController.navigate("bottom_nav") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                        else {
                            Toast.makeText(context, "Incorrect OTP", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C), contentColor = Color.White)
            ) {
                Text(if (otpSent) "Verify & Register" else "Send OTP", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
