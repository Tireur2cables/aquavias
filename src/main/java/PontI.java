import org.json.JSONArray;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PontI extends Pont {

    protected static BufferedImage pontI;
    protected static BufferedImage pontIEau;

    public PontI(JSONArray json) {
        super(json);
        this.sorties = this.calculSorties();
        /* A compléter
        this.pontI =
        this.pontIEau =
        */
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
