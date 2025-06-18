package com.example.foodmitradonar.Screens.Appwrite

import android.net.Uri
import android.widget.Toast
import androidx.core.net.toFile
import com.example.foodmitradonar.ui.theme.twilio.id
import com.example.foodmitradonar.ui.theme.twilio.url
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.models.InputFile
import io.appwrite.services.Account
import io.appwrite.services.Databases
import io.appwrite.services.Storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

suspend fun uploadToAppwrite(
    context: android.content.Context,
    imageUri: Uri,
    description: String,
    spoilTime: String,
    mobile: String,
    location: String
) {
    try {
        val client = Client(context)
            .setEndpoint(url)
            .setProject(id)

        val account = Account(client)
        account.createAnonymousSession()

        val storage = Storage(client)
        val databases = Databases(client)

        val fileId = ID.unique()
        val file = imageUri.toFile()

        val upload = storage.createFile(
            bucketId = "waste-images",
            fileId = fileId,
            file = InputFile.fromFile(file)
        )


        val imageUrl = "${url}/storage/buckets/waste-images/files/${fileId}/view?project=${id}"

        databases.createDocument(
            databaseId = "waste-db",
            collectionId = "waste-items",
            documentId = ID.unique(),
            data = mapOf(
                "imageUrl" to imageUrl,
                "description" to description,
                "spoilTime" to spoilTime,
                "mobile" to mobile,
                "location" to location
            )
        )

        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, "Uploaded Successfully!", Toast.LENGTH_SHORT).show()
        }

    } catch (e: Exception) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
