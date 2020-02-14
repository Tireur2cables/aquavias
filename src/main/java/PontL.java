import org.json.JSONArray;
import java.awt.image.BufferedImage;

public class PontL extends Pont {
    static BufferedImage pontL;
    static BufferedImage pontLEau;
    public PontL(JSONArray json) {
        super(json);
        this.setSorties(this.calculSorties());
        /* A compléter
        this.pontL =
        this.pontLEau =
        */
    }

    private boolean[] calculSorties(){
        boolean[] tab = {true,true,true,true};
        switch (this.getOrientation()) {
            case 'N' :
                tab[2] = false;
                tab[3] = false;
                break;
            case 'E' :
                tab[0] = false;
                tab[3] = false;
                break;
            case 'S' :
                tab[0] = false;
                tab[1] = false;
                break;
            case 'O' :
                tab[1] = false;
                tab[2] = false;
                break;
        }
        return tab;
    }
}
