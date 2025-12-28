# 🛡️ Log-Sentry

> Un outil DevSecOps d'analyse de logs utilisant une détection hybride : Regex (pour les menaces connues) et Entropie de Shannon (pour les secrets inconnus).

![Java](https://img.shields.io/badge/Language-Java_17+-orange?style=flat&logo=java)
![Security](https://img.shields.io/badge/Security-Defense_in_Depth-blue)
![License](https://img.shields.io/badge/License-MIT-green)

**Log-Sentry** est un scanner de sécurité léger et performant conçu pour détecter les fuites de données sensibles dans les fichiers de logs. Contrairement aux outils classiques qui se basent uniquement sur des motifs connus (Regex), Log-Sentry utilise une approche mathématique pour repérer les secrets aléatoires (mots de passe, clés API obfusquées) qui n'ont pas de format prédéfini.

---

## 🚀 Pourquoi cet outil ?

Les fuites de données modernes ne ressemblent pas toujours à des emails ou des numéros de carte bleue.
* **Regex Standard :** ✅ Trouve `admin@company.com`
* **Regex Standard :** ❌ Rate `xJ8@!9Lp$2` (Aucun motif connu)
* **Log-Sentry :** ✅ Détecte les deux grâce à son moteur hybride.

---

## ⚙️ Architecture & Fonctionnement

L'outil analyse chaque ligne de log à travers un pipeline de filtrage intelligent :



### 1️⃣ Filtrage du Bruit (Noise Filtering)
Avant toute analyse, l'outil ignore automatiquement :
* Les mots trop courts (< 8 caractères).
* Les fichiers inoffensifs (`.pdf`, `.jpg`, `.png`) pour éviter les faux positifs.

### 2️⃣ Layer 1 : Pattern Matching (Regex)
Détection haute précision des menaces structurées.
* **Cibles :** Adresses Emails, IPs, Clés AWS (`AKIA...`).
* **Avantage :** Identification immédiate et catégorisée du type de fuite.

### 3️⃣ Layer 2 : Entropie de Shannon (Mathématiques)
Calcul de la "complexité" (aléatoire) des chaînes de caractères restantes.
* **Formule :** Utilise l'Entropie de Shannon pour scorer chaque mot.
* **Seuil Dynamique :** L'outil ajuste intelligemment ses exigences selon la longueur du mot pour éviter les faux positifs sur les mots courts tout en restant strict sur les longues chaînes.
    * *Mot de 8 chars :* Score requis > 3.2
    * *Mot de 20+ chars :* Score requis > 4.4

---

## 📦 Installation & Utilisation

### Prérequis
* Java Development Kit (JDK) 11 ou supérieur.

### Installation
1.  **Cloner le dépôt**
    ```bash
    git clone [https://github.com/VOTRE_USERNAME/Log-Sentry.git](https://github.com/VOTRE_USERNAME/Log-Sentry.git)
    cd Log-Sentry
    ```

2.  **Compiler le projet**
    Placez-vous à la racine du projet :
    ```bash
    javac src/*.java -d out
    ```
    *(Ou ouvrez simplement le dossier avec IntelliJ IDEA / VS Code)*

3.  **Lancer le Scan**
    Assurez-vous d'avoir un fichier `test.log` à la racine, puis lancez :
    ```bash
    java -cp out Main
    ```

---

## 🧪 Exemple de Sortie

Log-Sentry produit des alertes claires avec le contexte de la fuite :

```text
🚨 [REGEX DETECTED] Type: AWS_KEY
   > Value: AKIAIOSFODNN7EXAMPLE
   > Line: 2023-10-27 [CRITICAL] AWS Credential Leak: AKIAIOSFODNN7EXAMPLE
------------------------------------------------
🚨 [REGEX DETECTED] Type: EMAIL
   > Value: admin@corp.com
   > Line: User admin@corp.com login failed.
------------------------------------------------
⚠️ [ENTROPY DETECTED] High Randomness (Score: 4.94)
   > Value: jdbc:mysql://db:3306/?secret=X7#mK9!vL2p@Zq1
   > Line: 2023-10-27 [CRITICAL] Database connection string leaked...
------------------------------------------------