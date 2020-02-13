/* Imports with maven dependecies */
import org.json.*;

public class Pont {

    private char forme; /* I, T, L */
    private char orientation; /* N, E, S, O */
    private boolean[] sorties;
    private boolean water;
    private String spe; /* entree, sortie, immobile */

    public Pont(JSONArray json){
        this.forme = json.getString(0).toUpperCase().charAt(0);
        this.orientation = json.getString(1).toUpperCase().charAt(0);
        this.spe = json.getString(2).toLowerCase();
        this.sorties = this.calculateSorties();
        this.water = false;
    }

    //constructeur voué à disparaitre
    public Pont(char forme, char orientation) {
        this.forme = forme;
        this.orientation = orientation;
        this.sorties = calculateSorties();
        this.water = false;
    }

    public boolean[] calculateSorties(){
        switch (forme) {
          case "I" :
            return this.calculateSortiesI();
          case "L" :
            return this.calculateSortiesL();
          case "T" :
            return this.calculateSortiesT();
        }
    }

    private boolean[] calculateSortiesI(){
      boolean[] tab = boolean[4];
      for(int = 0 ; i < tab.length ; i++){
        tab[i] = ((this.orientation == 'N' || this.orientation == 'S') && i%2 == 0)
              || ((this.orientation == 'E' || this.orientation == 'O') && i%2 == 1);
      }
    }

    private boolean[] calculateSortiesL(){
      boolean[] tab = boolean[4];
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

    }

    public char getForme(){
        return this.forme;
    }

    public char getOrientation(){
        return this.orientation;
    }

}
