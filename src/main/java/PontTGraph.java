import java.awt.image.BufferedImage;

public class PontTGraph extends PontGraph {

	static BufferedImage[] pontT = PontGraph.chargeImages("TnoO.png");
    static BufferedImage[] pontTEau = PontGraph.chargeImages("TwO.png");

    PontTGraph(char orientation) {
        super(orientation);
    }

}
