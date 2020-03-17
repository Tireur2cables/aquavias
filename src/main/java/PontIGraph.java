import java.awt.image.BufferedImage;

class PontIGraph extends PontGraph {

	static BufferedImage[] pontI = PontGraph.chargeImages("InoO.png");
    static BufferedImage[] pontIEau = PontGraph.chargeImages("IwO.png");
	static BufferedImage[] entreeI = PontGraph.chargeImages("EntreeIwO.png");
    static BufferedImage[] sortieI = PontGraph.chargeImages("SortieIwO.png");
    static BufferedImage[] sortieIEau = PontGraph.chargeImages("SortieInoO.png");

	PontIGraph(char orientation, boolean eau, boolean entree, boolean sortie) {
		super(orientation, eau, entree, sortie);
	}

	BufferedImage getImage() {
		if(super.entree) return entreeI[super.orientation];
		else if(super.sortie) return (super.eau)? sortieIEau[super.orientation] : sortieI[super.orientation];
		return (super.eau)? pontIEau[super.orientation] : pontI[super.orientation];
	}

}
