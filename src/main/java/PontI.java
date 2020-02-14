import org.json.JSONArray;
import java.awt.image.BufferedImage;

public class PontI extends Pont {
    static BufferedImage pontI;
    static BufferedImage pontIEau;
    public PontI(JSONArray json) {
        super(json);
        this.setSorties(this.calculSorties());
        /* A compléter
        this.pontI =
        this.pontIEau =
        */
    }

    public boolean[] calculSorties(){
        boolean[] tab = new boolean[4];
        for(int i = 0 ; i < tab.length ; i++){
            tab[i] = ((this.getOrientation() == 'N' || this.getOrientation() == 'S') && i%2 == 0)
                    || ((this.getOrientation() == 'E' || this.getOrientation() == 'O') && i%2 == 1);
        }
        return tab;
    }
}
