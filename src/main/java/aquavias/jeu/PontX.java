package aquavias.jeu;

import org.json.JSONArray;

public class PontX extends Pont{

    PontX(JSONArray json) {
        super(json);
        this.sorties = this.calculSorties();
    }

    public PontX(char Orientation, String spe){
        super('X', Orientation, spe);
        this.sorties = this.calculSorties();
    }

    @Override
    public boolean[] calculSorties() {
        boolean[] tab = new boolean[]{true, true, true, true};
        if (this.spe != null && this.spe.equals("sortie"))
            tab[1] = false;
        return tab;
    }

}
