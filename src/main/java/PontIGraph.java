import java.awt.image.BufferedImage;
import org.json.JSONArray;

public class PontIGraph extends PontI implements PontGraph{
	static BufferedImage pontI = chargeImage("InoO.png");
    static BufferedImage pontIEau = chargeImage("IwO.png");

	PontIGraph(JSONArray json){
		super(json);
	}

}
