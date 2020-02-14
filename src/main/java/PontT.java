import org.json.JSONArray;
import java.awt.image.BufferedImage;

public class PontT extends Pont {
    static BufferedImage pontT;
    static BufferedImage pontTEau;
    public PontT(JSONArray json) {
        super(json);
        this.setSorties(this.calculSorties());
        /* A compléter
        this.pontT =
        this.pontTEau =
        */
    }

    public boolean[] calculSorties(){
        boolean[] tab = {true,true,true,true};
        switch (getOrientation()){
            case 'N' :
                tab[2] = false;
                break;
            case 'E' :
                tab[3] = false;
                break;
            case 'S' :
                tab[0] = false;
                break;
            case 'O' :
                tab[1] = false;
                break;
        }
        return tab;
    }
}
