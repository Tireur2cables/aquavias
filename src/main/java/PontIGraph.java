import java.awt.image.BufferedImage;

/* Imports with maven dependecies */
import org.json.JSONArray;

public class PontIGraph extends PontI implements PontGraph {

	static BufferedImage[] pontI = PontGraph.chargeImages("InoO.png");
    static BufferedImage[] pontIEau = PontGraph.chargeImages("IwO.png");

	PontIGraph(JSONArray json){
		super(json);
	}

}
