import java.awt.image.BufferedImage;

class PontIGraph extends PontGraph {

	static BufferedImage[] pontI = PontGraph.chargeImages("InoO.png");
    static BufferedImage[] pontIEau = PontGraph.chargeImages("IwO.png");
	static BufferedImage[] entreeI = PontGraph.chargeImages("EntreeI.png");
    static BufferedImage[] sortieI = PontGraph.chargeImages("SortieInoO.png");
    static BufferedImage[] sortieIEau = PontGraph.chargeImages("SortieIwO.png");

	PontIGraph(Pont p) {
		super(p);
	}

	BufferedImage getImage() {
		if(super.pont.isEntree()) return entreeI[this.getOrientation()];
		else if(super.pont.isSortie()) return (super.pont.eau)? sortieIEau[this.getOrientation()] : sortieI[this.getOrientation()];
		return (super.pont.eau)? pontIEau[this.getOrientation()] : pontI[this.getOrientation()];
	}

}
