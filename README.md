# Aquavias

Version d'Aquavias qui n'a pas encore de nom pour le projet d'informatique

[GROUPE]  
Sang David - 21804098 - @david  
Canto Justin - 21965056 - @Justin   
Battaglini Nicolas - 21801244 - @Tireur2cables  
Barrault Victor - 21803922 - @Brrlt  


Format et sauvegarde des niveaux :   

Les niveaux sont sauvegardés dans des fichiers json.  
Le json d'un niveau comprend 3 champs :  
    * hauteur et longueur qui définissent la taille du plateau  
      Pour chaque niveau on augmente la hauteur et la longueur de 1 pour pouvoir placer les ponts d'entrée et de sortie.  
    * niveau est un tableau de tableau de tableau représentant respectivement les lignes, les colonnes et le contenu d'une case.  
    Le contenu d'une case est un tableau de 3 champs : le premier est le type du pont, le second est son orientation et le troisieme précise s'il s'agit d'un pont spécial (entrée, sortie, immobile).
