package aquavias.jeu;

import org.json.JSONArray;

public class PontI extends Pont {

    PontI(JSONArray json) {
        super(json);
        this.sorties = this.calculSorties();
    }

    public PontI(char Orientation, String spe){
        super('I', Orientation, spe);
        this.sorties = this.calculSorties();
    }

    public boolean[] calculSorties() {
        boolean[] tab = new boolean[4];
        for(int i = 0 ; i < tab.length ; i++) {
            tab[i] = ((this.orientation == 'N' || this.orientation == 'S') && i%2 == 0)
                    || ((this.orientation == 'E' || this.orientation == 'O') && i%2 == 1);
        }
        return tab;
    }

}
