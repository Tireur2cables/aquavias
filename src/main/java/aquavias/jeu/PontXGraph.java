package aquavias.jeu;

import java.awt.image.BufferedImage;

public class PontXGraph extends PontGraph {

    static BufferedImage pontX = PontGraph.chargeImage("XnoO.png");
    static BufferedImage pontXEau = PontGraph.chargeImage("XwO.png");
    static BufferedImage sortieX = PontGraph.chargeImage("SortieXnoO.png");
    static BufferedImage sortieXEau = PontGraph.chargeImage("SortieXwO.png");

    PontXGraph(Pont p) {
        super(p);
    }

    BufferedImage getImage() {
        if (super.pont.getSpe() == null) return (super.pont.eau)? pontXEau : pontX;
        else return (super.pont.eau)? sortieXEau : sortieX;
    }

}
