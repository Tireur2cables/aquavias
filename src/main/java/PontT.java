/* Imports with maven dependecies */
import org.json.JSONArray;

class PontT extends Pont {

    PontT(JSONArray json) {
        super(json);
        this.sorties = this.calculSorties();
    }

    protected boolean[] calculSorties() {
        boolean[] tab = {true,true,true,true};
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
        return tab;
    }

}
