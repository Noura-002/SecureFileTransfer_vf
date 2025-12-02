# Système de transfert de fichiers sécurisé

Ce projet est une application **Client-Serveur** en Java permettant le transfert sécurisé de fichiers via TCP, avec chiffrement AES et vérification d'intégrité SHA-256.

------------------------------------------------------------------------

## 📥 Installation de Java (JDK 17)

Avant d'exécuter les fichiers `.jar`, vous devez installer **Java JDK
17**, car il n'est pas inclus dans le projet.

### 🔹 Étape 1 : Télécharger JDK 17

Téléchargez la version JDK 17 depuis le site officiel de Java ou depuis
n'importe quelle distribution OpenJDK compatible.\
Choisissez la version correspondant à votre système (Windows, macOS ou
Linux).

### 🔹 Étape 2 : Installer

-   Lancez le fichier d'installation.\
-   Cliquez sur **Next** jusqu'à la fin.\
-   Java sera automatiquement configuré sur votre système (incluant
    l'ajout dans le PATH selon l'installateur).

---

## Contenu du dépôt

```
SecureFileTransfer_vf/
├── SecureFileServer.jar # Serveur compilé
├── SecureFileClient.jar # Client compilé
├── transfert_de_fichiers_securise_vf/
│ ├── src/
│ │ ├── SecureFileServer.java
│ │ ├── SecureFileClient.java
│ │ ├── ClientTransferHandler.java
│ │ ├── FileMetadata.java
│ │ └── CryptoUtils.java
├── README.md # Ce fichier
```

> Les fichiers `.jar` sont les exécutables prêts à l'emploi.
> Le dossier `transfert_de_fichiers_securise_vf/` contient tout le code Java pour consultation ou compilation.

---

## Instructions pour exécuter

### 1️⃣ Lancer le serveur

Ouvrir un terminal ou CMD et taper :

```bash
java -jar SecureFileServer.jar
```

Le serveur écoute par défaut sur le port **5000**.

---

### 2️⃣ Lancer le client

Ouvrir un autre terminal et taper :

```bash
java -jar SecureFileClient.jar
```

Le client demandera les informations suivantes :

```
Adresse IP du serveur (ex: localhost) : localhost
Port (ex: 5000) : 5000
Login : alice
Mot de passe : pass123
Chemin du fichier à envoyer : C:\chemin\vers\ton\fichier.txt
```

Si tout est correct, le message suivant s'affichera :

```
Authentification réussie.
Transfert réussi !
```

---

## Informations sur le protocole

Le transfert se fait en **3 phases** :

| Phase | Client → Serveur | Serveur → Client |
|-------|-----------------|-----------------|
| Authentification | `login`, `password` | `AUTH_OK` / `AUTH_FAIL` |
| Négociation     | `FileMetadata`     | `READY_FOR_TRANSFER` |
| Transfert       | `fichier chiffré` | `TRANSFER_SUCCESS` / `TRANSFER_FAIL` |

---

## Sécurité

- **Confidentialité** : AES/ECB/PKCS5Padding
- **Intégrité** : SHA-256
- **Authentification** : Login/mot de passe codés en dur

---

## Fichiers reçus côté serveur

Les fichiers transférés sont enregistrés dans le dossier **`server_uploads/`** côté serveur.

---

## Auteurs

- Noura EL FARISSI
- Hanane ERRADY  
- Projet pour le **Module Java Avancé**
