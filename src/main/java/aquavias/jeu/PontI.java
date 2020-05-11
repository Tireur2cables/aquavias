package aquavias.jeu;

import org.json.JSONArray;

public class PontI extends Pont {

    PontI(JSONArray json) {
        super(json);
        this.sorties = this.calculSorties();
    }

    PontI(char Orientation, String spe) {
        super('I', Orientation, spe);
        this.sorties = this.calculSorties();
    }

    boolean[] calculSorties() {
        boolean[] tab = new boolean[4];
        for(int i = 0 ; i < tab.length ; i++) {
            tab[i] = ((this.orientation == 'N' || this.orientation == 'S') && i%2 == 0)
                    || ((this.orientation == 'E' || this.orientation == 'O') && i%2 == 1);
        }
        if (this.spe != null)
            this.modifSortiesParSpe(tab);
        return tab;
    }

    private void modifSortiesParSpe(boolean[] tab) {
        switch (this.orientation) {
            case 'N' :
                if (this.spe.equals("entree"))
                    tab[2] = false;
                else if (this.spe.equals("sortie"))
                    tab[0] = false;
                break;
            case 'E' :
                if (this.spe.equals("entree"))
                    tab[3] = false;
                else if (this.spe.equals("sortie"))
                    tab[1] = false;
                break;
            case 'S' :
                if (this.spe.equals("entree"))
                    tab[0] = false;
                else if (this.spe.equals("sortie"))
                    tab[2] = false;
                break;
            case 'O' :
                if (this.spe.equals("entree"))
                    tab[1] = false;
                else if (this.spe.equals("sortie"))
                    tab[3] = false;
                break;
        }
    }

}
