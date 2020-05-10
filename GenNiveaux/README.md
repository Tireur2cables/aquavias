# Algorithme de génération de niveau

## Les deux classes
Le générateur est séparé en deux classes. La classe GenNiveaux s'occupe du choix aléatoire du mode du jeu entre "compteur" et "fuite", et de la taille du niveau, entre 3 (la taille
minimale d'un niveau, en dessous les niveaux n'ont aucuns intérêts) et 11 (une taille déja assez élevée, qui peut entrainer la génération de niveau trés complexes).  
Le reste de la génération se passe dans la classe Plateau, qui créé le plateau d'un niveau en s'occupant de toutes les contraintes requises à la fabriquation d'un niveau.  

## Génération du plateau  
    
La classe Plateau s'occupe de la mise en place d'un plateau de jeu, et s'assure, qu'il est terminable, ainsi que des quelques fonctions annexes, telles que le calcul du nombre de coups.  
L'algorithme s'articule autour de la fonction genererChemin(). On créé dans un premier temps un tableau de la taille remplit de case nulle, puis on place l'entrée et la sortie 
du jeu à des hauteurs aléatoires. La position sur l'axe X étant définie (0 pour l'entrée et taille du plateau -1 pour la sortie) il n'y a qu'a générer un nombre pour l'axe Y.  
Les ponts d'entrées et de sorties peuvent être placés a n'importe quelle hauteur, et on vérifie bien qu'il ne présente pas de sortie en direction d'un mur et qu'il soit 
bien orienté (les images de ces ponts n'étant pas symétriques, leur orientation est donc importante).  
    
Ensuite on appel genererChemin() sur le pont d'entrée.
  
### La fonction genererChemin()  

La fonction genererChemin() est une fonction récursive qui s'appelle sur toutes les sorties du pont qu'elle vient de générer, si on a besoin de continuer le chemin (voir condition d'arret).  
Ainsi, pour chaque sortie du pont passer en paramètre de la fonction, on à deux possibilités :  
- Si la case du plateau vers laquelle dirige une sortie du pont est vide :  
  On créer un nouveau pont aléatoire, qui a plus de chance de diriger vers la droite (vers la sortie) que vers la gauche et de revenir sur ses pas. Les essais nous on montrés
qu'avoir deux chances sur trois de diriger plutôt vers la droite donnait les résultats les plus intéressants.
- Sinon, la case du plateau vers lequelle la sortie dirige contient déja un pont :
  Il faut donc qu'on forme une connexion entre ces deux ponts, pour former un passage, ou une boucle.  
  On doit donc rajouter une sortie au pont sur lequel on arrive, en changeant sa forme. Ainsi un pont en I ou en L qui ne présente que 2 sorties (déja connectées car l'appelle récursif
est déja passé) doivent devenir des ponts en T, et les ponts en T, qui présentent 3 sorties, doivent devenir des ponts en +, et être connectés dans toutes les directions.  
C'est pourquoi on à les fonctions shouldBeX(), shouldBeL() et shouldBeT(), qui calcul les conditions pour savoir en quel pont le pont sur lequel on forme une nouvelle connexion doit
etre transformé.  
  
![image info](../resources/imgreadme/PontTConnection.png)

Les fonctions satisfaitSortiesPont(), lierPontWith() et verifMur() permettent de vérifier les conditions de base de fonctionnement d'un niveau.  
- verifMur() permet de vérifier que les ponts ne dirigent pas en dehors des limites du plateau.   
- lierPontWith() permet de connecter de pont ensemble.
- satisfaitSortiesPont() permet d'assurer que le nouveau pont est bien connectés avec ses voisins. 


Condition d'arrêt :  
La récursion s'arrête quand on tombe sur une case non vide. On transfome le pont sur lequel on tombe, et on arrête cette partie de la récursion car le reste du chemin 
à été traité par l'appel récursif du pont sur lequel on est tombé.

L'algorithme génère le niveau dans la configuration de victoire, avec tous les ponts bien connectés et orientés. Il nous est donc très simple de vérifier 
si celui-ci est faisable. En effet, un simple appel a la fonction isVictoire() de Jeu nous permet de savoir si il est faisable.

Le fonctionnement de l'algortihme ne nous prouve pas que le niveau produit est faisable. 
Par exemple, le niveau suivant n'est pas faisable mais pourtant l'algorithme termine :
![image info](../resources/imgreadme/Niveau_infaisable.png)

C'est pourquoi notre algorithme vérifie la faisabilité du niveau avant d'effectuer la suite du programme. Si il détecte un niveau qui n'est pas faisable, il efface 
celui-ci et recommence une nouvelle génération. Ce fonctionnement n'est pas parfait, mais permet d'assurer l'exportation de niveau faisable. Etant donné qu'il existe un 
nombre (très) important de configuration faisable, et un nombre assez faible de configuration non-faisable, on est sur que cette boucle while termine et entraine l'exportation 
d'un niveau.  
  
A ce moment de l'algorithme, on est en capacité d'exporter le niveau, sous forme de solution avant de mélanger les pièces.

## Calcul de la limite et de la difficulté  

Afin de rendre le niveau générer intérressant on mélange les pièces et on en rajoute certaines inutiles. Chaque case vide à une chance sur trois d'accueillir un pont
inutile, ils sont donc placés de façon totalement aléatoire, parfois isolés et (vraiment) inutiles, parfois au plus proche d'un chemin, et donc ajoutant de la difficulté.  
Chaque case contenant un pont est tourné, un nombre de coup choisi aléatoirement.
Ajout screen niveau solution + Niveau mélangé

Ces deux fonctions (rotateAleaPont() & placerPontInutile()) permettent aussi de calculer le nombre de coup pour le niveau, et sa difficulté.  
La limite du nombre de coup est calculé de la façon suivante : On compte le nombre de fois que l'on doit cliquer sur le pont pour atteindre la position de victoire, et on rajoute 
un coup par pont placé aléatoirement. Après plusieurs essais, c'est cette façon de compter qui semble donner un nombre de coup le plus interressant pour le joueur.  
Pour le temps, on à considérer que chaque coup prenait 2 secondes, on multiplie donc par deux le nombre de coups, et transformons en seconde. Les niveaux avec fuite d'eau
sont donc un peu plus simple que les niveaux avec compteur de coups, pour contre-balancer la "pression" du chronomètre. 
  
Finalement, on exprime la difficulté du niveau en fonction de sa taille et du nombre de coup pour le résoudre, et le constructeur renvoit un nouveau plateau, 
qui peut être exporter dans le dossier niveau par la fonction createNiveau() du GenNiveaux.  


