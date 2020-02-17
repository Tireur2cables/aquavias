import org.json.JSONArray;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PontT extends Pont {

    protected static BufferedImage pontT = chargeImage("TnoO.png");
    protected static BufferedImage pontTEau = chargeImage("TwO.png");

    public PontT(JSONArray json) {
        super(json);
        this.sorties = this.calculSorties();
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
