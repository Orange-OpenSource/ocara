[![Build Status](https://travis-ci.org/Orange-OpenSource/ocara.svg?branch=master)](https://travis-ci.org/Orange-OpenSource/ocara)

# OCARA

OCARA,acronyme de "Outil de Contrôle et d’Analyse du Réel Accessible", est une application Android concue pour tablette 10".
OCARA permet d’effectuer un autodiagnostic d’accessibilité des locaux accueillant les salariés. 
Il fonctionne selon une logique de parcours utilisateur ; grâce à un jeu de règles élaboré, il permet d’auditer tout type d’objet ou de zone de service, 
selon une approche multi-handicaps.



**Pour qui ?**
  * Indirectement pour les salariés en situation de handicap eux-mêmes 
  * Pour l’ensemble des acteurs de l’entreprise qui interviennent dans le domaine du handicap : correspondants handicap, responsables de sites, responsables sécurité …


**Bénéfices salariés**

Pour les acteurs du handicap
  * Pouvoir évaluer de manière autonome le niveau d’accessibilité des parcours des collaborateurs, sans expertise particulière
  * Produire rapidement et simplement un rapport d’audit
  * Intégrer rapidement de nouvelles règles 	

Pour les salariés en situation de handicap
  * Pouvoir faire des audits réguliers, qui entrent dans le cadre de l'amélioration de la qualité de vie au travail
  * Pouvoir mettre en évidence / exprimer des besoins spécifiques lors des audits, voire demander la création de nouvelles règles


**Bénéfices pour l’entreprise**
  * Mieux intégrer dans les pratiques courantes la démarche accessibilité : l’évaluation de l’accessibilité des lieux de travail se fait simplement 
  * Faciliter l’opportunité d’accueil d’une personne handicapée et aider à résoudre les situations de maintien dans l’emploi d’une personne handicapée
  * Au-delà du handicap, permettre d’améliorer la circulation et la sécurité pour tous les salariés du site 

**Orange** a décidé de mettre OCARA en open-source afin que toutes les entreprises, associations ou personnes préoccupés par l’accessibilité, puissent utiliser cet outil et contribuer à son développement.

 L'accessibilité est une préoccupation commune. Par la mutualisation et le partage de nos ressources en matière d'accessibilité, nous pourrons aller plus loin ensemble. [voir la vidéo] (http://vod.com.ftgroup/content/encoded/t81HxrJyewTuNLcLgfph.asx)



# Construire et utiliser

Un apk prédéfini est fourni pour votre commodité (cliquez sur "Raw" pour enregistrer le fichier) [ici] (https://github.com/Orange-OpenSource/ocara/blob/master/demo/ocara-opensource-debug.apk)

Pour construire l'application, vous devez avoir une installation correcte de la [SDK Android] (https://developer.android.com/sdk/index.html).
Ensuite, l'application peut être construite avec Android Studio, ou directement sur avec la commande :
```shell
gradlew clean assembleDebug
```

Lors du lancement de l'application, vous arrivez sur la page * Créer un audit *

#License

Copyright (C) 2015 Orange

[Mozilla Public License Version 2.0](https://www.mozilla.org/MPL/2.0)


#Authors
Denis Boisset

Alexis Chemin

Ludovic Lelion

Manuel Sanchez

#Support
ocara.support@orange.com
