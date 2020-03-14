import java.awt.image.BufferedImage;

public class PontTGraph extends PontGraph {

	static BufferedImage[] pontT = PontGraph.chargeImages("TnoO.png");
    static BufferedImage[] pontTEau = PontGraph.chargeImages("TwO.png");
    private int orientation;

    PontTGraph(int orientation) {
        this.orientation = orientation;
    }

}
