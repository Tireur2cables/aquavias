import java.awt.image.BufferedImage;

class PontLGraph extends PontGraph {

	static BufferedImage[] pontL = PontGraph.chargeImages("LnoO.png");
    static BufferedImage[] pontLEau = PontGraph.chargeImages("LwO.png");
	static BufferedImage[] entreeL = PontGraph.chargeImages("EntreeL.png");
	static BufferedImage[] sortieL = chargeImagesSorties(false);
	static BufferedImage[] sortieLEau = chargeImagesSorties(true);

	PontLGraph(char orientation, boolean eau, boolean entree, boolean sortie) {
		super(orientation, eau, entree, sortie);
	}

	static BufferedImage[] chargeImagesSorties(boolean eau) {
		BufferedImage imageN = PontGraph.chargeImage((eau)?"SortieLwO.png":"SortieLnoO.png");
		BufferedImage imageE = null;
		BufferedImage imageS = null;
		BufferedImage imageO = rotate(PontGraph.chargeImage((eau)?"SortieLwO2.png":"SortieLnoO2.png"), 270);
		BufferedImage[] images = new BufferedImage[4];
		images[0] = imageN;
		images[1] = imageE;
		images[2] = imageS;
		images[3] = imageO;
		return images;
	}

	BufferedImage getImage() {
		if(super.entree) return entreeL[super.orientation];
		else if(super.sortie) return (super.eau)? sortieLEau[super.orientation] : sortieL[super.orientation];
		return (super.eau)? pontLEau[super.orientation] : pontL[super.orientation];
	}
}
