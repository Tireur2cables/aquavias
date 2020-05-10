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
  On créer un nouveau pont aléatoire, qui a plus de chance de diriger vers la droite (vers la sortie) que vers la gauche et de revenir sur ses pas.
- Sinon, la case du plateau vers lequelle la sortie dirige contient déja un pont :
  Il faut donc qu'on forme une connexion entre ces deux ponts, pour former un passage, ou une boucle.  
  On doit donc rajouter une sortie au pont sur lequel on arrive, en changeant sa forme. Ainsi un pont en I ou en L qui ne présente que 2 sorties (déja connectées car l'appelle récursif
est déja passé) doivent devenir des ponts en T, et les ponts en T, qui présentent 3 sorties, doivent devenir des ponts en +, et être connectés dans toutes les directions.


## Calcul de la limite et de la difficulté  

