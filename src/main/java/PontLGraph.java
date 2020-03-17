import java.awt.image.BufferedImage;

class PontLGraph extends PontGraph {

	static BufferedImage[] pontL = PontGraph.chargeImages("LnoO.png");
    static BufferedImage[] pontLEau = PontGraph.chargeImages("LwO.png");
	static BufferedImage[] entreeL = PontGraph.chargeImages("LwO.png");
	static BufferedImage[] sortieL = PontGraph.chargeImages("LnoO.png");
	static BufferedImage[] sortieLEau = PontGraph.chargeImages("LwO.png");

	PontLGraph(char orientation, boolean eau, boolean entree, boolean sortie) {
		super(orientation, eau, entree, sortie);
	}

	BufferedImage getImage() {
		if(super.entree) return entreeL[super.orientation];
		else if(super.sortie) return (super.eau)? sortieLEau[super.orientation] : sortieL[super.orientation];
		return (super.eau)? pontLEau[super.orientation] : pontL[super.orientation];
	}
}
