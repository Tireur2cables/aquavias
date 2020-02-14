import org.json.JSONArray;

import java.awt.image.BufferedImage;

public class PontI extends Pont {
    static BufferedImage pontI;
    static BufferedImage pontIEau;
    public PontI(JSONArray json) {
        super(json);
        this.getSorties() = this.calculateSorties();
        /* A compléter
        this.pontI =
        this.pontIEau =
        */
    }

    public boolean[] calculateSorties(){
        boolean[] tab = new boolean[4];
        for(int i = 0 ; i < tab.length ; i++){
            tab[i] = ((this.orientation == 'N' || this.orientation == 'S') && i%2 == 0)
                    || ((this.orientation == 'E' || this.orientation == 'O') && i%2 == 1);
        }
    }
}
