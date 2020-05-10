# Document Explicatif du Projet

## Objectif du Projet
  
L'objectif de ce projet est de recréer un jeu sur le model du jeu mobile Aquavias.  
D'où le nom Aquavias pour le projet.  

## Architecture du programme
  
Un niveau de jeu est une matrice de Ponts.  
  
### Pont
Un pont est défini par 5 attributs :  
  * sa `forme` caractérisée par un char
  * son `orientation` caractérisée par un char E pour Est, N pour Nord, O pour Ouest et S pour Sud  
  * Un tableau de booléens de taille 4, `sorties`, qui défini dans quelles directions le pont est ouvert, le tableau sorties suit l'organisation [Nord, Est, Sud, Ouest]  
  * un booléen `eau` qui informe sur la présence d'eau ou non sur le pont
  * une string, `spe`, valant null pour les ponts normaux et "entree" ou "sortie" pour les ponts correspondants  

### Format et sauvegarde des niveaux
Les niveaux sont sauvegardés dans des fichiers json.  
Le json d'un niveau comprend 3 champs :  
  * `hauteur` et `largeur` qui définissent la taille du plateau  
  * `mode` qui définit le mode de jeu  
  * `limite` et `compteur` qui définissent respectivement la limite de coups et le compteur initial lors qu lancement du niveau  
  * `difficulte` qui définit le niveau de difficulté estimé pour le niveau  
  * `niveau` est un tableau de tableau de tableau représentant respectivement les lignes, les colonnes et le contenu d'une case (qui contient un pont ou non).  
    * S'il y a un pont, une case est un tableau de 3 champs : le premier est le type du pont, le second est son orientation et le troisieme précise s'il s'agit d'un pont spécial (entrée, sortie, immobile).  
    * S'il n'y a pas de pont, il y a un tableau vide.  

### Formatage du code
Notre code suit l'architecture modèle - vue - controleur en suivant le schéma UML suivant :  
![img](../resources/imgreadme/nomnoml.png)
  
## Pistes d'amélioration
  
Voir issues Bonus.
  
## Difficultés rencontrées  
  
Background  
eventqueue invoke later ralentit et produit des excepetion  
sous effectif  