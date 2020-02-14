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
        this.spe = json.getString(2).toLowerCase();
        this.water = false;
    }

    public boolean[] getSorties() {
        return sorties;
    }

    //constructeur voué à disparaitre
    public Pont(char forme, char orientation) {
        this.forme = forme;
        this.orientation = orientation;
        this.sorties = calculateSorties();
        this.water = false;
    }

    private boolean[] calculateSortiesL(){
      boolean[] tab = new  boolean[4];
      switch (this.orientation) {
        case 'N' :
          tab[0] = true;
          tab[1] = true;
          tab[2] = false;
          tab[3] = false;
          return tab;
        case 'E' :
          tab[0] = false;
          tab[1] = true;
          tab[2] = true;
          tab[3] = false;
          return tab;
        case 'S' :
          tab[0] = false;
          tab[1] = false;
          tab[2] = true;
          tab[3] = true;
          return tab;
        case 'O' :
          tab[0] = true;
          tab[1] = false;
          tab[2] = false;
          tab[3] = true;
            return tab;
      }
        return;
    }

    public char getForme(){
        return this.forme;
    }

    public char getOrientation(){
        return this.orientation;
    }

}
