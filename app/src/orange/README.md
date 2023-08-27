# Ocara Orange

[![CI](https://gitlab.forge.orange-labs.fr/OCARASoftwareSolution/Ocara/badges/develop/build.svg)](https://gitlab.forge.orange-labs.fr/OCARASoftwareSolution/Ocara/pipelines?scope=branches)
[![Mattermost](https://img.shields.io/badge/chat%20on-Mattermost-green.svg)](https://mattermost.forge.orange-labs.fr/ocara/channels/town-square)

> Ocara Orange est la version propriétaire du projet Ocara Open Source. Elle contient du code lié au portail SMTK et autres outils Orange qui ne sont pas Open Source.

## Installation

Pour builder la version Orange, nous proposons la structure suivante :

	.
	└── ocara
	    ├── ocara-orange (repertoire contenant le code source du Git "Ocara Orange")
		│   └── main
	    └── ocara-opensource (repertoire contenant le code source du Git "Ocara Open Source")
	        ├── app
		    ├── demo
	        ├── documentation
		    └── gradle


Créer un repertoire "ocara" puis créer deux sous-répertoires "ocara-orange" et "ocara-opensource"

- "ocara-orange" contient le code du dépôt [Ocara Orange](https://gitlab.forge.orange-labs.fr/OCARASoftwareSolution/ocara-orange)
- "ocara-opensource" contient le code du dépôt [Ocara](https://gitlab.forge.orange-labs.fr/OCARASoftwareSolution/Ocara)


## Usage

Le projet est optimisé pour Android Studio 3.0.

Il faut importer le dossier "ocara-opensource" ("File" > "New>" > "Import Projet..."), Gradle va détecter automatiquement le repertoire "ocara-orange".

Avant toute chose, le git du dépôt Ocara Orange doit être sur la branche develop.

Pour builder une version Orange d'Ocara, utiliser la commande suivante :

```
gradlew clean assembleOrangeRelease
```



## Forge

[![Jenkins](http://10.192.35.187/images/logo-jenkins.png)](http://10.192.35.187/jenkins/job/MP/)
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
[![Sonar](http://10.192.35.187/images/logo-sonar.png)](http://10.192.35.187/sonar/)

- Builds et tests automatiques à chaque commit
- Build des Merges Requests (pour Ocara uniquement)
- Intégration Jenkins <-> Gitlab, les résultats de builds sont directement intégrés à Gitlab, fermeture automatique de tickets, etc.
- Maintenabilité accrue des définitions des jobs Jenkins : conversion des jobs en [« declarative pipeline »](https://jenkins.io/doc/book/pipeline/syntax/)
- Build notifications sur le channel Mattermost 


## Mattermost

Le lien d'invitation pour le groupe Mattermost : [Rejoindre la team Ocara](https://mattermost.forge.orange-labs.fr/signup_user_complete/?id=49o8r8u4c3b9bp9xwhojwb1x9e)


## Signing

Pour signer l'apk il faut ajouter les mots de passe dans le `~\.gradle\gradle.properties` (sur Windows : `%USERPROFILE%/.gradle/gradle.properties`) :

```
OCARA_KSTORE_PWD=password
OCARA_KEY_PWD=password
```


## Code style

1. Importer le fichier settings `settings.jar` ("File" > "Import Settings...")
2. Sélectionner "Code Style" et "Code Style (schemes)"
3. Redémarrer Android Studio


## SonarLint

1. Ajouter le plugin `SonarLint` ("Preferences" > "Plugins" > "Browse repositories...")
2. Redémarrer Android Studio
3. Renseigner l'URL de SonarQube ("Preferences" > "Other Settings" > "SonarLint General Settings") : "http://10.192.35.187:3000/sonar/"
4. Utiliser l'authentification par token "960098215117e5cd7d4f5a595503ca9e9fed0dac"
5. Dans "SonarLint Project Settings" associer le projet à "ocara-opensource"

Il se peut qu'il faille renseigner le proxy pour que la connexion fonctionne correctement, auquel cas utiliser "proxypartner.itn.ftgroup:8080" ou passer par la configuration automatique "http://www-cache-nrs.si.fr.intraorange/proxy-vdr.pac"


## Firebase (Analytics & Crash reporting)

- [Console Firebase](https://console.firebase.google.com/project/ocara-4589c)
- Compte Google associé : itlabs.orange.lyon@gmail.com
- Mêmes identifiants pour la partie crash reporting ([Fabric](https://www.fabric.io/oab-lyons-projects/android/apps/com.orange.ocara/))


## Contributing

1. Créer une branche feature ou fix (par exemple feature/{ID ticket})
2. Commiter
3. Pusher
4. Créer un Merge Request sur Gitlab


## Divers

- Liste de diffusion projet : ocara-team@list.orange.com
- Compte projet OAB : PPP50252 / B2EYwx3KuR
- Compte projet Orange : ocara-team / fUNjCmrGG_8I


## License

	Copyright (C) 2015 Orange

	This software is the confidential and proprietary information of Orange.
	You shall not disclose such confidential information and shall use it only
	in accordance with the terms of the license agreement you entered into
	with Orange.
