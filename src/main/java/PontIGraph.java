import java.awt.image.BufferedImage;

public class PontIGraph extends PontGraph {

	static BufferedImage[] pontI = PontGraph.chargeImages("InoO.png");
    static BufferedImage[] pontIEau = PontGraph.chargeImages("IwO.png");

	PontIGraph(char orientation, boolean eau) {
		super(orientation, eau);
	}

	BufferedImage getImage() {
		return (super.eau)? pontIEau[super.orientation] : pontI[super.orientation];
	}

}
