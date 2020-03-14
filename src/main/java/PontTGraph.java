import java.awt.image.BufferedImage;

/* Imports with maven dependecies */
import org.json.JSONArray;

public class PontTGraph extends PontT implements PontGraph {

	static BufferedImage[] pontT = PontGraph.chargeImages("TnoO.png");
    static BufferedImage[] pontTEau = PontGraph.chargeImages("TwO.png");

	PontTGraph(JSONArray json){
		super(json);
	}

}
