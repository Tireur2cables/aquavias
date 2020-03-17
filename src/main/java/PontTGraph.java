import java.awt.image.BufferedImage;

class PontTGraph extends PontGraph {

	static BufferedImage[] pontT = PontGraph.chargeImages("TnoO.png");
    static BufferedImage[] pontTEau = PontGraph.chargeImages("TwO.png");

    PontTGraph(char orientation, boolean eau) {
        super(orientation, eau);
    }

    BufferedImage getImage() {
        return (super.eau)? pontTEau[super.orientation] : pontT[super.orientation];
    }

}
