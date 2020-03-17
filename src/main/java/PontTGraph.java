import java.awt.image.BufferedImage;

class PontTGraph extends PontGraph {

	static BufferedImage[] pontT = PontGraph.chargeImages("TnoO.png");
    static BufferedImage[] pontTEau = PontGraph.chargeImages("TwO.png");
    static BufferedImage[] entreeT = PontGraph.chargeImages("TwO.png");
    static BufferedImage[] sortieT = chargeImages(false);
    static BufferedImage[] sortieTEau = chargeImages(true);

    static BufferedImage[] chargeImages(boolean eau) {
        BufferedImage imageN = PontGraph.chargeImage((eau)?"SortieTwO3.png":"SortieTnoO3.png");
        BufferedImage imageE = rotate(PontGraph.chargeImage((eau)?"SortieTwO.png":"SortieTnoO.png"), 90);
        BufferedImage imageS = PontGraph.chargeImage((eau)?"SortieTwO2.png":"SortieTnoO2.png");
        BufferedImage imageO = rotate(PontGraph.chargeImage((eau)?"SortieTwO2.png":"SortieTnoO2.png"), 270);
        BufferedImage[] images = new BufferedImage[4];
        images[0] = imageN;
        images[1] = imageE;
        images[2] = imageS;
        images[3] = imageO;
        return images;
    }

    PontTGraph(char orientation, boolean eau, boolean entree, boolean sortie) {
        super(orientation, eau, entree, sortie);
    }

    BufferedImage getImage() {
        if(super.entree) return entreeT[super.orientation];
        else if(super.sortie) return (super.eau)? sortieTEau[super.orientation] : sortieT[super.orientation];
        return (super.eau)? pontTEau[super.orientation] : pontT[super.orientation];
    }
}
