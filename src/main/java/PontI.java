import org.json.JSONArray;

import java.awt.image.BufferedImage;

public class PontI extends Pont {

    static BufferedImage pontI = chargeImage("InoO.png");
    static BufferedImage pontIEau = chargeImage("IwO.png");

    public PontI(JSONArray json) {
        super(json);
        this.sorties = this.calculSorties();
    }

    public PontI(){
        this.forme = 'I';
        this.orientation = 'N';
        this.eau = false;
        this.sorties = this.calculSorties();
        this.spe = null;
    }

    public boolean[] calculSorties(){
        boolean[] tab = new boolean[4];
        for(int i = 0 ; i < tab.length ; i++){
            tab[i] = ((this.orientation == 'N' || this.orientation == 'S') && i%2 == 0)
                    || ((this.orientation == 'E' || this.orientation == 'O') && i%2 == 1);
        }
        return tab;
    }

}
