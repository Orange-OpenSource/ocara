# Fichier JSON des régles
* app : Nom de l'application ex : "ocara"
* version : Version de l'application ex : "1.0.0"
* type : Nom du fichier de régles ex "Régles Open Source"

## objects
* id : Identifiant de l'élément de parcours ex "Obj4b"
* icon : Nom du fichier image correspondant à l'icône de cet élément de parcours ex "Porte coulissante-01.png"
* name : Nom de l'élément de parcours ex "porte coulissante"
* définition : Définition de l'élément de parcours ex "Arrêté du 8 décembre 2014, article 10 : Les portes doivent répondre aux usages attendus.
Toutes les portes situées sur les cheminements permettent le passage des personnes handicapées et peuvent être manœuvrées par des personnes ayant des capacités physiques réduites, y compris en cas de système d’ouverture complexe. 
Les portes comportant une partie vitrée importante peuvent être repérées par les personnes malvoyantes de toutes tailles et ne créent pas de gêne visuelle.
Les portes battantes et les portes automatiques peuvent être utilisées sans danger par les personnes handicapées.
Les sas permettent le passage et la manœuvre des portes pour les personnes handicapées ...
Pour plus d'infos : http://www.accessibilite-batiment.fr/erp-situes-dans-un-cadre-bati-existant/portes-portiques-et-sas/arrete.html"
* illustrations : Identifiant de l'illustration
* objects : Identifiant des caractéristiques de l'élément de parcours ex ["SObj04", qui correspond à l’élément « déverrouillage » "SObj05", qui correspond à l’élément « ouverture à distance » "SObj06" qui correspond à l’élément « système de détection » ]
* questions : Identifiant des questions ex ["O-4", qui correspond à « Es ce que la porte a les dimensions requises ? » "O-4a" qui correspond à « Est-ce qu'il y a des repères visuels (bandes, contrastes) sur la porte ? » ]

## handicaps
* idHandicap : Identifiant du type de handicap ex "H1"
* label : Libellé du type de handicap ex "Fauteuil roulant éclectrique"
* picto : Nom du fichier image correspondant à l'icône du type de handicap ex "Fauteuil roulant électrique-01.png"

## questions
* "Identifiant de la question" ex "O-4"
    * ref : Identifiant de la question ex "O-4"
    * subject : Groupe de la question ex Caractériqtiques et équipements
    * origin : Origine de la question
    * handicaps : Types de handicap concernés par cette question
    * text : Libellé de la question ex "Est-ce que la porte a les dimensions requises ?"
    * doubts : Règles à vérifier 
        *  id : Identifiant de la règle ex "P01"
        *  text : Libellé de la règle ex  "On mesure le passage utile porte ouverte, en enlevant l'épaisseur de la porte car elle limite le passage.<br/>Une porte (hors porte d’évacuation) doit faire au moins 0,75 m de passage utile <br/>Un  portique de sécurité doit faire au moins 0,77 m de passage utile.<br/>Une porte servant à l'évacuation :<br/>   > doit faire au moins 1,40 m de largeur pour les locaux recevant plus de 100 personnes, et comporter un vantail d'au moins 0,90 m<br/>   > doit faire au moins 0,90 m pour les locaux recevant moins de 100 personnes"
        *  handicaps : Types de handicap concernés par cette règle
            * "Identifiant du handicap " : "B" pour bloquant ou "G" pour génant ex {"H1" : "B", "H2" : "B", "H3" : "G" }
        * illustration : Identifiant de l'illustration de la règle ex "P01"

## rules 
* "Identifiant de la question" ex O-4
    * V : "Identifiant de la question suivante" ex "O-4-a"
    * NV : "Identifiant de la question suivante" ex "O-4-a"

## sites
* type : Type de l'audit ex "Audit de parcours"
* areas : Catégories présentes dans ce type d'audit
    * desc : Catégorie ex "Accès zone"
    * objects : Elements de parcours qui appartiennent à cette catégorie ex ["Obj1", "Obj4a", "Obj4b", "Obj5a","Obj5b","Obj6", "Obj9", "Obj10", "Obj15", "SObj09", "SObj11" ]

##illustrations
* "Identifiant de l'illustration ex "P01" "
    * title : Titre de l'illustration "Passage utile"
    * image : Nom du fichier image correspondant à l'illustration ex "porte01.jpg"
    * comment : Commentaire texte lié à l'illustration ex "Origine de la recommandation : Arrêtés du 1er août 2006 et du 30 novembre 2007, Article 10 - Dispositions relatives aux portes, portiques et sas"
