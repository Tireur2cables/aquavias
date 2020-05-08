package aquavias.jeu;

import org.json.JSONArray;

public class PontT extends Pont {

    PontT(JSONArray json) {
        super(json);
        this.sorties = this.calculSorties();
    }

    PontT(char Orientation, String spe) {
        super('T', Orientation, spe);
        this.sorties = this.calculSorties();
    }

    boolean[] calculSorties() {
        boolean[] tab = {true, true, true, true};
        switch (this.orientation) {
            case 'N' : tab[3] = false;
                break;
            case 'E' : tab[0] = false;
                break;
            case 'S' : tab[1] = false;
                break;
            case 'O' : tab[2] = false;
                break;
        }
        if (this.spe != null && this.spe.equals("sortie"))
            tab[1] = false;
        return tab;
    }

}
