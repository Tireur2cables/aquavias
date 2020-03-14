import java.awt.image.BufferedImage;

public class PontLGraph extends PontGraph {

	static BufferedImage[] pontL = PontGraph.chargeImages("LnoO.png");
    static BufferedImage[] pontLEau = PontGraph.chargeImages("LwO.png");

	PontLGraph(char orientation) {
		super(orientation);
	}

}
