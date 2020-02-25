import java.awt.image.BufferedImage;

/* Imports with maven dependecies */
import org.json.JSONArray;


public class PontL extends Pont {

    static BufferedImage pontL = chargeImage("LnoO.png");
    static BufferedImage pontLEau = chargeImage("LwO.png");

    public PontL(JSONArray json) {
        super(json);
        this.sorties = this.calculSorties();
    }

    public boolean[] calculSorties() {
        boolean[] tab = {true,true,true,true};
        switch (this.orientation) {
            case 'N' : {
                tab[2] = false;
                tab[3] = false;
                break;
            }
            case 'E' : {
                tab[0] = false;
                tab[3] = false;
                break;
            }
            case 'S' : {
                tab[0] = false;
                tab[1] = false;
                break;
            }
            case 'O' : {
                tab[1] = false;
                tab[2] = false;
                break;
            }
        }
        return tab;
    }

}
