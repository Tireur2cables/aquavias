# Aquavias #

Version d'Aquavias qui n'a pas encore de nom pour le projet d'informatique

## GROUPE ##
Sang David - 21804098 - @david  
Canto Justin - 21965056 - @Justin   
Battaglini Nicolas - 21801244 - @Tireur2cables  
Barrault Victor - 21803922 - @Brrlt  

## PONTS ##
Un pont est défini par 4 attributs :  
  * sa forme caractérisée par un char
  * son orientation caractérisée par un char E pour Est, N pour Nord, O pour Ouest et S pour Sud  
  * Un tableau de booléens de taille 4 sorties qui défini dans quelles directions le pont est ouvert, le tableau sortie suit l'organisation [Nord, Est, Sud, Ouest]  
  * un booléen water qui informe sur la présence d'eau ou non sur le pont  

## Format et sauvegarde des niveaux ##
Les niveaux sont sauvegardés dans des fichiers json.  
Le json d'un niveau comprend 3 champs :  
  * hauteur et longueur qui définissent la taille du plateau  
    Pour chaque niveau on augmente la hauteur et la longueur de 1 pour pouvoir placer les ponts d'entrée et de sortie.  
  * niveau est un tableau de tableau de tableau représentant respectivement les lignes, les colonnes et le contenu d'une case (qui contient un pont ou non).  
    * S'il y a un pont une case est un tableau de 3 champs : le premier est le type du pont, le second est son orientation et le troisieme précise s'il s'agit d'un pont spécial (entrée, sortie, immobile).  
    * S'il n'y a pas de pont, il y a un tableau vide.  

![img](https://gaufre.informatique.univ-paris-diderot.fr/Brrlt/aquavias/blob/planProjet/nomnoml.png)
```plantuml
Alice -> Bob: Authentication Request
Bob --> Alice: Authentication Response

Alice -> Bob: Another authentication Request
Alice <-- Bob: Another authentication Response
```
