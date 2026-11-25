package com.nexus.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { NexusApp() }
    }
}

data class ChatLine(val author: String, val message: String)

@Composable
fun NexusApp() {
    val scope = rememberCoroutineScope()
    var apiKey by remember { mutableStateOf("") }
    var endpoint by remember { mutableStateOf("https://api.openai.com/v1/chat/completions") }
    var model by remember { mutableStateOf("gpt-4o-mini") }
    var input by remember { mutableStateOf("") }
    var busy by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf("Non configurée") }
    var shizukuStatus by remember { mutableStateOf(ShizukuBridge.status()) }
    val messages = remember {
        mutableStateListOf(
            ChatLine("NEXUS", "Système initialisé. Configure une API compatible pour commencer."),
            ChatLine("INFO", "Version 0.1 : chat IA et diagnostic Shizuku. Le contrôle Roblox n'est pas encore activé.")
        )
    }

    MaterialTheme(colorScheme = darkColorScheme()) {
        Scaffold(
            topBar = {
                Column(Modifier.fillMaxWidth().padding(16.dp)) {
                    Text("NEXUS AI", style = MaterialTheme.typography.headlineMedium)
                    Text("AUTONOMOUS GAME ASSISTANT · v0.1.0")
                    Text("IA : $status  •  Shizuku : $shizukuStatus",
                        style = MaterialTheme.typography.labelSmall)
                }
            }
        ) { padding ->
            Column(
                Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                Text("CONFIGURATION IA", style = MaterialTheme.typography.titleSmall)
                OutlinedTextField(
                    value = endpoint, onValueChange = { endpoint = it },
                    label = { Text("URL API compatible OpenAI") },
                    modifier = Modifier.fillMaxWidth(), singleLine = true
                )
                OutlinedTextField(
                    value = model, onValueChange = { model = it },
                    label = { Text("Nom du modèle") },
                    modifier = Modifier.fillMaxWidth(), singleLine = true
                )
                OutlinedTextField(
                    value = apiKey, onValueChange = { apiKey = it },
                    label = { Text("Clé API (session uniquement)") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(), singleLine = true
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { shizukuStatus = ShizukuBridge.status() }) {
                        Text("Vérifier Shizuku")
                    }
                    Text("Permission Shizuku : prochaine étape",
                        modifier = Modifier.padding(top = 12.dp),
                        style = MaterialTheme.typography.labelSmall)
                }
                Divider()
                Text("CONSOLE NEXUS", style = MaterialTheme.typography.titleSmall)
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(messages) { line ->
                        Surface(
                            color = when (line.author) {
                                "NEXUS" -> MaterialTheme.colorScheme.secondaryContainer
                                "ERREUR" -> MaterialTheme.colorScheme.errorContainer
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(Modifier.fillMaxWidth().padding(10.dp)) {
                                Text(line.author, style = MaterialTheme.typography.labelSmall)
                                Text(line.message, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
                OutlinedTextField(
                    value = input, onValueChange = { input = it },
                    label = { Text("Écris une commande à NEXUS…") },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !busy && input.isNotBlank() && apiKey.isNotBlank(),
                    onClick = {
                        val prompt = input.trim()
                        input = ""
                        messages.add(ChatLine("TOI", prompt))
                        busy = true
                        status = "Réflexion…"
                        scope.launch {
                            try {
                                val answer = NexusApi.ask(endpoint, apiKey, model, prompt)
                                messages.add(ChatLine("NEXUS", answer))
                                status = "Connectée"
                            } catch (e: Exception) {
                                messages.add(ChatLine("ERREUR", e.message ?: "Échec de la requête API"))
                                status = "Erreur API"
                            } finally { busy = false }
                        }
                    }
                ) { Text(if (busy) "NEXUS réfléchit…" else "Envoyer") }
                Text(
                    "Les réponses apparaissent ici, pas encore dans le chat Roblox.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

private object ShizukuBridge {
    fun status(): String = try {
        if (rikka.shizuku.Shizuku.pingBinder()) "Service détecté" else "Service indisponible"
    } catch (_: Throwable) { "Shizuku non installé ou non démarré" }
}
