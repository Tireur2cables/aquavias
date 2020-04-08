package aquavias.jeu;

import org.json.JSONArray;

public class PontL extends Pont {

    PontL(JSONArray json) {
        super(json);
        this.sorties = this.calculSorties();
    }

    public PontL(char Orientation, String spe){
        super('L', Orientation, spe);
        this.sorties = this.calculSorties();
    }

    public boolean[] calculSorties() {
        boolean[] tab = {true,true,true,true};
        switch (this.orientation) {
            case 'N' : {
                tab[0] = false;
                tab[3] = false;
                break;
            }
            case 'E' : {
                tab[0] = false;
                tab[1] = false;
                break;
            }
            case 'S' : {
                tab[1] = false;
                tab[2] = false;
                break;
            }
            case 'O' : {
                tab[2] = false;
                tab[3] = false;
                break;
            }
        }
        return tab;
    }

}
