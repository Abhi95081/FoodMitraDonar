package com.example.foodmitradonar.Screens.Supabase

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.foodmitradonar.ui.theme.twilio.key
import com.example.foodmitradonar.ui.theme.twilio.url
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.UploadData
import io.github.jan.supabase.storage.storage
import io.ktor.utils.io.ByteReadChannel
import java.io.InputStream
import java.util.UUID

data class Donation(
    val image_url: String,
    val quantity: String,
    val duration: String,
    val mobile: String,
    val location: String,
    val category: String
)

object SupabaseManager {

    private val client = createSupabaseClient(
        supabaseUrl = url,
        supabaseKey = key
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
        context: Context,
        category: String
    ): Boolean {
        return try {
            val fileName = "${UUID.randomUUID()}.jpg"
            val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)

            inputStream?.use { stream ->
                val bytes = stream.readBytes()
                val byteReadChannel = ByteReadChannel(bytes)
                val size = bytes.size.toLong()

                client.storage.from("donations").upload(
                    path = fileName,
                    data = UploadData(byteReadChannel, size),
                    upsert = false
                )
            } ?: throw IllegalArgumentException("Input stream is null")

            val imageUrl =
                "https://vqrmpufgwnbjafcbctwj.supabase.co/storage/v1/object/public/donations/$fileName"

            client.postgrest["donations"].insert(
                mapOf(
                    "image_url" to imageUrl,
                    "quantity" to quantity,
                    "duration" to duration,
                    "mobile" to mobile,
                    "location" to location,
                    "category" to category
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
