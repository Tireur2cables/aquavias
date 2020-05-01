package aquavias.jeu;/* Imports with maven dependecies */
import org.json.*;

import java.util.concurrent.ThreadLocalRandom;

public abstract class Pont {

    char forme; /* I, T, L */
    char orientation; /* N, E, S, O */
    boolean[] sorties; /* [N, E, S, O] */
    boolean eau;
    String spe; /* entree, sortie, immobile */

    /**
     * INIT PART
     * */

    Pont(JSONArray json) {
        this.forme = json.getString(0).toUpperCase().charAt(0);
        this.orientation = json.getString(1).toUpperCase().charAt(0);
        this.spe = (!json.isNull(2))? json.getString(2).toLowerCase() : null;
        this.eau = this.isEntree();
    }

    Pont(char forme, char orientation, String spe){
        this.forme = forme;
        this.orientation = orientation;
        this.spe = spe;
        this.eau = this.isEntree();
    }

    public abstract boolean[] calculSorties();

    static Pont castPont(JSONArray tab) {
        switch(tab.getString(0).toUpperCase().charAt(0)) {
            case 'I' : return new PontI(tab);
            case 'L' : return new PontL(tab);
            case 'T' : return new PontT(tab);
            case 'X' : return new PontX(tab);
        }
        throw new RuntimeException("char du pont inconnu");
    }

    /**
     * GETTER PART
     * */

    public char getForme(){
        return this.forme;
    }

    boolean getEau() {
        return this.eau;
    }

    public char getOrientation(){
        return this.orientation;
    }

    public String getSpe() {
        return this.spe;
    }

    boolean isMovable() {
        return this.spe == null;
    }

    public boolean isEntree() {
        return this.spe != null && this.spe.equals("entree");
    }

    public boolean isSortie() {
        return this.spe != null && this.spe.equals("sortie");
    }

    boolean[] getSorties() {
        return this.sorties;
    }

    public static char getNextOrientation(char c) {
        switch (c) {
            case 'N' : return 'E';
            case 'E' : return 'S';
            case 'S' : return 'O';
            case 'O' : return 'N';
        }
        throw new RuntimeException("Calcul nouvelle orientation incorrect, Orientation = " + c);
    }

    public boolean isAccessibleFrom(char c) {
        switch (c) {
            case 'N' : return this.sorties[2]; /* accessible depuis le nord de l'autre pont donc le sud de ce pont etc... */
            case 'E' : return this.sorties[3];
            case 'S' : return this.sorties[0];
            case 'O' : return this.sorties[1];
        }
        throw new RuntimeException("char Sortie incorrect : " + c);
    }

    /* Should only be used on entree and sortie */
    public boolean isOrientationCorrecteEntreeSortie() {
        switch (this.forme) {
            case 'I' :
                return this.orientation == 'E';
            case 'T' :
                return (this.spe.equals("entree"))? this.orientation == 'N' : this.orientation != 'S' ;
            case 'L' :
                return (this.spe.equals("entree"))? (this.orientation == 'N' || this.orientation == 'O') : (this.orientation != 'E' && this.orientation != 'S');
        }
        throw new RuntimeException("Forme Pont Incorrecte : " + this.forme);
    }

    /**
     * SETTER PART
     * */

    public void setOrientation(char c) {
        this.orientation = c;
        this.sorties = this.calculSorties();
    }

    void setEau(boolean eau) {
        this.eau = eau;
    }

    /***
     * API PART
     * */

    public static Pont createPont(char forme, String spe) {
        if(forme == 'O') {
            forme = getRandomForme();
        }
        char orientation = getRandomOrientation();
        switch(forme) {
            case 'I' :
                return new PontI(orientation, spe);
            case 'L' :
                return new PontL(orientation, spe);
            case 'T' :
                return new PontT(orientation, spe);
            case 'X' :
                return new PontX(orientation, spe);
        }
        throw new RuntimeException("char du pont inconnu");
    }

    public static char getRandomOrientation(){
        int random = ThreadLocalRandom.current().nextInt(0, 4);
        switch (random){
            case 0 : return 'N';
            case 1 : return 'E';
            case 2 : return 'S';
            case 3 : return 'O';
        }
        throw new RuntimeException("Valeur aléatoire d'orientation inconnue");
    }

    public static char getRandomForme() {
        int random = ThreadLocalRandom.current().nextInt(0, 5);
        switch (random) {
            case 0: case 1:
                return 'I';
            case 2: case 3:
                return 'L';
            case 4 : //moins de probabilité d'avoir un T que d'avoir les autres
                return 'T';
        } //les ponts en X nes sont pas pris en compte pour en avoir seulement lorsqu'on en a besoin
        throw new RuntimeException("Random int out of bounds");
    }
}
