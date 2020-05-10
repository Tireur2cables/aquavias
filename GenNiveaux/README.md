# Algorithme de génération de niveau

Le générateur est séparé en deux classes. La classe GenNiveaux s'occupe du choix aléatoire du mode du jeu entre "compteur" et "fuite", et de la taille du niveau, entre 3 (la taille
minimale d'un niveau, en dessous les niveaux n'ont aucuns intérêts) et 11 (une taille déja assez élevée, qui peut entrainer la génération de niveau trés complexes).  
Le reste de la génération se passe dans la classe Plateau, qui créé le plateau d'un niveau en s'occupant de toutes les contraintes requises à la fabriquation d'un niveau.  