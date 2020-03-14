import java.awt.image.BufferedImage;

public class PontLGraph extends PontGraph {

	static BufferedImage[] pontL = PontGraph.chargeImages("LnoO.png");
    static BufferedImage[] pontLEau = PontGraph.chargeImages("LwO.png");
	private int orientation;

	PontLGraph(int orientation) {
		this.orientation = orientation;
	}

}
