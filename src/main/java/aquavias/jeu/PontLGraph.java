package aquavias.jeu;

import java.awt.image.BufferedImage;

class PontLGraph extends PontGraph {

	static BufferedImage[] pontL = PontGraph.chargeImages("LnoO.png");
    static BufferedImage[] pontLEau = PontGraph.chargeImages("LwO.png");
	static BufferedImage[] entreeL = PontGraph.chargeImages("EntreeL.png");
	static BufferedImage[] sortieL = chargeImagesSorties(false);
	static BufferedImage[] sortieLEau = chargeImagesSorties(true);

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

	PontLGraph(Pont p) {
		super(p);
	}

	BufferedImage getImage() {
		if(super.pont.isEntree()) return entreeL[this.getOrientation()];
		else if(super.pont.isSortie()) return (super.pont.eau)? sortieLEau[this.getOrientation()] : sortieL[this.getOrientation()];
		return (super.pont.eau)? pontLEau[this.getOrientation()] : pontL[this.getOrientation()];
	}

}
