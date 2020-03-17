import java.awt.image.BufferedImage;

class PontTGraph extends PontGraph {

	static BufferedImage[] pontT = PontGraph.chargeImages("TnoO.png");
    static BufferedImage[] pontTEau = PontGraph.chargeImages("TwO.png");
    static BufferedImage[] entreeT = PontGraph.chargeImages("TwO.png");
    static BufferedImage[] sortieT = PontGraph.chargeImages("SortieTnoO2.png");
    static BufferedImage[] sortieTEau = PontGraph.chargeImages("SortieTwO2.png");

    PontTGraph(char orientation, boolean eau, boolean entree, boolean sortie) {
        super(orientation, eau, entree, sortie);
    }

    BufferedImage getImage() {
        if(super.entree) return entreeT[super.orientation];
        else if(super.sortie) return (super.eau)? sortieTEau[super.orientation] : sortieT[super.orientation];
        return (super.eau)? pontTEau[super.orientation] : pontT[super.orientation];
    }
}
