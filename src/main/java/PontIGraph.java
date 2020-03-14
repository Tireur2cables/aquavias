import java.awt.image.BufferedImage;

public class PontIGraph extends PontGraph {

	static BufferedImage[] pontI = PontGraph.chargeImages("InoO.png");
    static BufferedImage[] pontIEau = PontGraph.chargeImages("IwO.png");
	private int orientation;

	PontIGraph(char orientation) {
		super(orientation);
	}

}
