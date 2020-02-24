import java.awt.image.BufferedImage;

/* Imports with maven dependecies */
import org.json.JSONArray;


public class PontI extends Pont {

    static BufferedImage pontI = chargeImage("InoO.png");
    static BufferedImage pontIEau = chargeImage("IwO.png");

    public PontI(JSONArray json) {
        super(json);
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
