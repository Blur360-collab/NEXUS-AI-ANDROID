package com.nexus.ai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object NexusApi {
    private val client = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(45, TimeUnit.SECONDS)
        .build()

    suspend fun ask(endpoint: String, apiKey: String, model: String, prompt: String): String =
        withContext(Dispatchers.IO) {
            require(endpoint.startsWith("https://")) { "L'URL API doit utiliser HTTPS." }
            val systemPrompt = "Tu es NEXUS, un assistant de jeu Android. Réponds en français, clairement et brièvement. Tu peux conseiller le joueur et expliquer des commandes. Ne prétends jamais avoir contrôlé Roblox, lu son chat ou analysé son écran si ces fonctions ne sont pas réellement disponibles. Respecte les règles du jeu."

            val body = JSONObject()
                .put("model", model)
                .put("messages", JSONArray()
                    .put(JSONObject().put("role", "system").put("content", systemPrompt))
                    .put(JSONObject().put("role", "user").put("content", prompt)))
                .put("temperature", 0.6)
                .toString()

            val request = Request.Builder()
                .url(endpoint)
                .header("Authorization", "Bearer $apiKey")
                .header("Content-Type", "application/json")
                .post(body.toRequestBody("application/json".toMediaType()))
                .build()

            client.newCall(request).execute().use { response ->
                val raw = response.body?.string().orEmpty()
                if (!response.isSuccessful) {
                    throw IllegalStateException("Erreur HTTP ${response.code}: $raw")
                }
                JSONObject(raw).getJSONArray("choices")
                    .getJSONObject(0).getJSONObject("message")
                    .optString("content", "Réponse vide de l'API.")
            }
        }
}
