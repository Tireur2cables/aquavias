package aquavias.jeu;

import java.awt.image.BufferedImage;

class PontTGraph extends PontGraph {

	static BufferedImage[] pontT = PontGraph.chargeImages("TnoO.png");
    static BufferedImage[] pontTEau = PontGraph.chargeImages("TwO.png");
    static BufferedImage[] entreeT = PontGraph.chargeImages("EntreeT.png");
    static BufferedImage[] sortieT = chargeImagesSorties(false);
    static BufferedImage[] sortieTEau = chargeImagesSorties(true);

    static BufferedImage[] chargeImagesSorties(boolean eau) {
        BufferedImage imageN = PontGraph.chargeImage((eau)?"SortieTwO3.png":"SortieTnoO3.png");
        BufferedImage imageE = rotate(PontGraph.chargeImage((eau)?"SortieTwO.png":"SortieTnoO.png"), 90);
        BufferedImage imageS = null;
        BufferedImage imageO = rotate(PontGraph.chargeImage((eau)?"SortieTwO2.png":"SortieTnoO2.png"), 270);
        BufferedImage[] images = new BufferedImage[4];
        images[0] = imageN;
        images[1] = imageE;
        images[2] = imageS;
        images[3] = imageO;
        return images;
    }

    PontTGraph(Pont p) {
        super(p);
    }

    BufferedImage getImage() {
        if(super.pont.isEntree()) return entreeT[this.getOrientation()];
        else if(super.pont.isSortie()) return (super.pont.eau)? sortieTEau[this.getOrientation()] : sortieT[this.getOrientation()];
        return (super.pont.eau)? pontTEau[this.getOrientation()] : pontT[this.getOrientation()];
    }

}
