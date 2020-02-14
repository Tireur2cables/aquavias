/* Imports with maven dependecies */
import org.json.*;

public abstract class Pont {

    private char forme; /* I, T, L */
    private char orientation; /* N, E, S, O */
    private boolean[] sorties;
    private boolean water;
    private String spe; /* entree, sortie, immobile */

    public Pont(JSONArray json){
        this.forme = json.getString(0).toUpperCase().charAt(0);
        this.orientation = json.getString(1).toUpperCase().charAt(0);
        this.spe = (!json.isNull(2))?  json.getString(2).toLowerCase() : null;
        this.water = false;
    }

    public void setSorties(boolean[] sorties) {
        this.sorties = sorties;
    }

    public char getForme(){
        return this.forme;
    }

    public char getOrientation(){
        return this.orientation;
    }

}
