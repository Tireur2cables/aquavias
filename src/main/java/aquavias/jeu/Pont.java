package aquavias.jeu;/* Imports with maven dependecies */
import org.json.*;

public abstract class Pont {

    protected char forme; /* I, T, L */
    protected char orientation; /* N, E, S, O */
    protected boolean[] sorties; /* [N, E, S, O] */
    protected boolean eau;
    protected String spe; /* entree, sortie, immobile */

    /**
     * INIT PART
     * */

    Pont(JSONArray json) {
        this.forme = json.getString(0).toUpperCase().charAt(0);
        this.orientation = json.getString(1).toUpperCase().charAt(0);
        this.spe = (!json.isNull(2))?  json.getString(2).toLowerCase() : null;
        this.eau = this.isEntree();
    }

    Pont(char forme, char orientation, String spe){
        this.forme = forme;
        this.orientation = orientation;
        this.spe = spe;
    }

    abstract boolean[] calculSorties();

    private void castAndCalculateSorties() {
        if (this instanceof PontI) this.sorties = ((PontI) this).calculSorties();
        else if (this instanceof PontL) this.sorties = ((PontL) this).calculSorties();
        else if (this instanceof PontT) this.sorties = ((PontT) this).calculSorties();
    }

    /**
     * GETTER PART
     * */

    char getForme(){
        return this.forme;
    }

    boolean getEau() {
        return this.eau;
    }

    char getOrientation(){
        return this.orientation;
    }

    boolean isMovable() {
        return this.spe == null;
    }

    boolean isEntree() {
        return this.spe != null && this.spe.equals("entree");
    }

    boolean isSortie() {
        return this.spe != null && this.spe.equals("sortie");
    }

    boolean[] getSorties() {
        return this.sorties;
    }

    static char getNextOrientation(char c) {
        switch (c) {
            case 'N' : return 'E';
            case 'E' : return 'S';
            case 'S' : return 'O';
            case 'O' : return 'N';
        }
        throw new RuntimeException("Calcul nouvelle orientation incorrect, Orientation = " + c);
    }

    boolean isAccessibleFrom(char c) {
        switch (c) {
            case 'N' : return this.sorties[2]; /* accessible depuis le nord de l'autre pont donc le sud de ce pont etc... */
            case 'E' : return this.sorties[3];
            case 'S' : return this.sorties[0];
            case 'O' : return this.sorties[1];
        }
        throw new RuntimeException("char Sortie incorrect : " + c);
    }

    /**
     * SETTER PART
     * */

    void setOrientation(char c) {
        this.orientation = c;
        this.castAndCalculateSorties();
    }

    void setEau(boolean eau) {
        this.eau = eau;
    }

}
