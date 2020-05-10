# Document Explicatif du Projet

## Objectif du Projet
  
L'objectif de ce projet est de recréer un jeu sur le model du jeu mobile Aquavias.  
D'où le nom Aquavias pour le projet.  

## Architecture du programme

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

Lorsque l'on charge un niveau, le programme va chercher le fichier JSON correspondant à ce niveau.  
Grâce aux informations contenues dans ce fichier, le programme est capable de recréer une matrice de Ponts correspondant au niveau.
  
### Pont
Un pont est défini par 5 attributs :  
  * sa `forme` caractérisée par un char
  * son `orientation` caractérisée par un char E pour Est, N pour Nord, O pour Ouest et S pour Sud  
  * Un tableau de booléens de taille 4, `sorties`, qui défini dans quelles directions le pont est ouvert, le tableau sorties suit l'organisation [Nord, Est, Sud, Ouest]  
  * un booléen `eau` qui informe sur la présence d'eau ou non sur le pont
  * une string, `spe`, valant null pour les ponts normaux et "entree" ou "sortie" pour les ponts correspondants  

L'affichage se contentera d'afficher chaque pont au bon endroit dans la fenre d'affichage.  

### Formatage du code
Notre code suit l'architecture modèle - vue - controleur afin de structurer le code.  
Le schéma UML suivant décrit de façon globale les interactions de notre programme :  
![img](../resources/imgreadme/nomnoml.png)
  
## Pistes d'amélioration
  
Nous avons créé des issues contenant le tag [BONUS] dans leur titre pour chaque pistes d'améliorations que nous aurions aimé pouvoir implémenter.
Voici la liste exhaustive des ces issues :  
*  Décoration arrière du decor :  Cette issue à pour but de mettre une image en fond pour les niveaux.  
Nous avons commencé cette issue mais suite à quelques problèmes, dont nous parlerons plus dans la partie "Difficultés rencontrées", nous avons mis en pause la résolution de cette issue.  
*  Proposer de générer un nouveau niveau après la fin d'un niveau générer : Le but serait ici de proposer directement de générer un nouveau niveau lorsqu'on finit le dernier niveau du jeu (même lorsue ce dernier niveau vient d'être générer).  
Cela éviteraià l'utilisateur de devoir retourner au menu puis cliquer sur le bouton `mode infini`.
*  Menu Principal : Nous pensions faire ici un menu principal plus élaboré qui permettrait d'exploiter la taille de la fenêtre pour charger les niveaux ou faire d'autres actions que d'afficher une image.  
*  Meilleur affichage compteur : Nous aurions aimé faire un meilleur affichage pour le compteur de coups et de la réserve d'eau à l'image du jeu mobile Aquavias.  
*  ajout de sons : Afin de rendre le jeu plus agréable à jouer nous pensions ajouter des bruitages pour l'eau qui coule, les ponts qui tourne. Nous avons aussi pensé à une musique de fond ou encore une musique de victoire.  
*  Changement graphisme : Nous pouvons facilement changé les images de chaque ponts grâce au découpage des classes. Nous pensions donc passer sur des images plus détaillée en perspectives pour un rendu en 3 dimensions.  
*  Ajout d'animations : nous avons pensé à faire une animation plutôt que de faire un simple changement d'image lorsqu'on tourne un pont. L'eau aussi pourrait être faite en animation.  
*  Générique : Lorsqu'on finit les niveaux de la version originale de notre jeu nous aurions aimé rajouter un petit générique pour bien montrer que c'est la fin du jeu (même si nous pouvons encore jouer grâce au mode infini).  
*  FrameWork d'internationnalisation : Notre chargé de TP nous a soufflé l'idée d'intégrer un framework d'internationnalisation pour avoir le jeu en plusieurs langue et également de laissé la liberté du genre utilisé au joueur.


## Difficultés rencontrées  
  
Background  
eventqueue invoke later ralentit et produit des excepetion  
sous effectif  