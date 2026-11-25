# NEXUS AI Android — v0.1.0

Prototype natif Kotlin / Jetpack Compose.

## Inclus
- Interface Android NEXUS.
- Console de conversation.
- Appels à une API HTTPS compatible OpenAI Chat Completions.
- Vérification indicative de la présence du service Shizuku.

## Pas encore inclus
- Demande de permission Shizuku et service utilisateur complet.
- Capture d'écran / MediaProjection.
- Analyse visuelle OCR ou vision.
- Exécution de gestes Android.
- Envoi de messages dans Roblox.
- Pilotage autonome du personnage.

Ce prototype ne joue donc pas encore à la place du joueur.

## Compilation sans PC (recommandé)

Ce projet est préparé pour être compilé **directement sur GitHub** (gratuit) :

1. Crée un compte GitHub si besoin.
2. Crée un **nouveau dépôt public**.
3. Upload **tout le contenu de ce dossier** (y compris le dossier `.github`).
4. Va dans l’onglet **Actions** → sélectionne le workflow **Build APK (armeabi-v7a)** → **Run workflow**.
5. Attends 5-12 minutes.
6. Dans le résumé du job, télécharge l’artifact **NEXUS-AI-v0.1-armeabi-v7a**.
7. Installe l’APK sur ton téléphone (autorise les sources inconnues).

L’APK est forcé en **armeabi-v7a** uniquement (compatible 32 bits).

## Compilation classique (avec PC)
1. Installer Android Studio récent avec Android SDK 35 et JDK 17.
2. Ouvrir ce dossier dans Android Studio.
3. Laisser Gradle synchroniser les dépendances.
4. Utiliser Build > Build APK(s).
5. Le fichier debug sera normalement dans `app/build/outputs/apk/debug/app-debug.apk`.

## Configuration
Saisir une URL HTTPS d’API compatible OpenAI, le modèle et la clé API.
La clé reste uniquement en mémoire pendant cette session dans cette version.
Ne partage pas une clé API et surveille les coûts associés à son utilisation.

## À respecter
Utiliser les interactions Android autorisées et respecter les règles Roblox et celles du jeu concerné. Ne pas utiliser l’application pour spammer le chat, contourner les protections ou modifier le client Roblox.
