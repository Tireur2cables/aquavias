import java.awt.image.BufferedImage;

/* Imports with maven dependecies */
import org.json.JSONArray;

public class PontLGraph extends PontL implements PontGraph {

	static BufferedImage pontI = chargeImage("InoO.png");
    static BufferedImage pontIEau = chargeImage("IwO.png");

	PontLGraph(JSONArray json){
		super(json);
	}

}
