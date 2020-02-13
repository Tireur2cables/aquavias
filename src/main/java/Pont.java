/* Imports with maven dependecies */
import org.json.*;

public class Pont {

    private char forme; /* I, T, L */
    private char orientation; /* N, E, S, O */
    private boolean[] sorties;
    private boolean water;

    public Pont(JSONArray json){
        this.forme = json.getString(0).toUpperCase().charAt(0);
        this.orientation = json.getString(1).toUpperCase().charAt(0);
        this.sorties = this.calculateSorties();
        this.water = false;
    }

    public Pont(char forme, char orientation) {
        this.forme = forme;
        this.orientation = orientation;
        this.sorties = calculateSorties();
        this.water = false;
    }

    public boolean[] calculateSorties(){
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
