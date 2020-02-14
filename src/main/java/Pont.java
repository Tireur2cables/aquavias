/* Imports with maven dependecies */
import org.json.*;

public class Pont {

    private char forme; /* I, T, L */
    private char orientation; /* N, E, S, O */
    private boolean[] sorties;
    private boolean eau;

    public Pont(JSONArray json){
        this.forme = json.getString(0).toUpperCase().charAt(0);
        this.orientation = json.getString(1).toUpperCase().charAt(0);
        this.sorties = this.calculSorties();
        this.eau = false;
    }

    public Pont(char forme, char orientation) {
        this.forme = forme;
        this.orientation = orientation;
        this.sorties = calculSorties();
        this.eau = false;
    }

    public boolean[] calculSorties(){
        boolean[] tab = new boolean[4];
        return tab;
    }

    public char getForme(){
        return this.forme;
    }

    public char getOrientation(){
        return this.orientation;
    }

}
