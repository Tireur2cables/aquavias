package aquavias.jeu;

import org.json.JSONArray;

class PontI extends Pont {

    PontI(JSONArray json) {
        super(json);
        this.sorties = this.calculSorties();
    }

    protected boolean[] calculSorties() {
        boolean[] tab = new boolean[4];
        for(int i = 0 ; i < tab.length ; i++) {
            tab[i] = ((this.orientation == 'N' || this.orientation == 'S') && i%2 == 0)
                    || ((this.orientation == 'E' || this.orientation == 'O') && i%2 == 1);
        }
        return tab;
    }

}
