/* Imports with maven dependecies */
import org.json.JSONArray;

import java.awt.image.BufferedImage;


public class PontTGraph extends PontT implements PontGraph {

	static BufferedImage pontI = chargeImage("InoO.png");
    static BufferedImage pontIEau = chargeImage("IwO.png");

	PontTGraph(JSONArray json){
		super(json);
	}

}
