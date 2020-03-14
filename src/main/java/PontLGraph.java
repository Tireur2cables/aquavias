import java.awt.image.BufferedImage;

/* Imports with maven dependecies */
import org.json.JSONArray;

public class PontLGraph extends PontGraph {

	static BufferedImage[] pontL = PontGraph.chargeImages("LnoO.png");
    static BufferedImage[] pontLEau = PontGraph.chargeImages("LwO.png");

	PontLGraph(JSONArray json){
		super(json);
	}

}
