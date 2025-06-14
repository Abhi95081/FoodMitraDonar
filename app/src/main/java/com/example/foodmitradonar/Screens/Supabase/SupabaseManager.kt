package com.example.foodmitradonar.Screens.Supabase

import android.content.Context
import android.net.Uri
import io.ktor.utils.io.ByteReadChannel
import android.util.Log
import com.example.foodmitradonar.ui.theme.twilio.key
import com.example.foodmitradonar.ui.theme.twilio.url
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.UploadData
import io.github.jan.supabase.storage.storage
import java.io.InputStream
import java.util.UUID

data class Donation(
    val image_url: String,
    val quantity: String,
    val duration: String,
    val mobile: String,
    val location: String
)

object SupabaseManager {

    private val client = createSupabaseClient(
        supabaseUrl = url,
        supabaseKey = key // Replace with your actual key
    ) {
        install(Postgrest)
        install(Storage)
    }

    suspend fun uploadDonation(
        imageUri: Uri,
        quantity: String,
        duration: String,
        mobile: String,
        location: String,
        context: Context
    ): Boolean {
        return try {
            val fileName = "${UUID.randomUUID()}.jpg" // Ensure this is a valid file name
            val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)

            inputStream?.use { stream ->
                val bytes = stream.readBytes()
                val byteReadChannel = ByteReadChannel(bytes) // Convert ByteArray to ByteReadChannel
                val size = bytes.size.toLong() // Get the size of the byte array

                // Upload the image to the storage bucket
                client.storage.from("donations").upload(
                    path = fileName,
                    data = UploadData(
                        byteReadChannel,
                        size = size // Pass the size here
                    ),
                    upsert = false
                )
            } ?: throw IllegalArgumentException("Input stream is null")

            // Construct the URL for accessing the uploaded image
            val imageUrl = "https://vqrmpufgwnbjafcbctwj.supabase.co/storage/v1/object/public/donations/$fileName"

            // Insert donation details into the database
            client.postgrest["donations"].insert(
                mapOf(
                    "image_url" to imageUrl,
                    "quantity" to quantity,
                    "duration" to duration,
                    "mobile" to mobile,
                    "location" to location
                )
            )

            true
        } catch (e: Exception) {
            Log.e("SupabaseUpload", "Upload failed: ${e.localizedMessage}", e)
            false
        }
    }


    suspend fun getDonations(): List<Donation> {
        return try {
            client.postgrest["donations"].select().decodeList<Donation>()
        } catch (e: Exception) {
            Log.e("SupabaseGet", "Fetch failed: ${e.localizedMessage}", e)
            emptyList()
        }
    }
}
