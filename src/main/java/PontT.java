import org.json.JSONArray;
import java.awt.image.BufferedImage;

public class PontT extends Pont {

    protected static BufferedImage pontT;
    protected static BufferedImage pontTEau;

    public PontT(JSONArray json) {
        super(json);
        this.sorties = this.calculSorties();
        /* A compléter
        this.pontT =
        this.pontTEau =
        */
    }

    public boolean[] calculSorties(){
        boolean[] tab = {true,true,true,true};
        switch (this.orientation){
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
