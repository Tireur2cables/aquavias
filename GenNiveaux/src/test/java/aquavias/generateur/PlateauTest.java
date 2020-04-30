package aquavias.generateur;

import aquavias.jeu.Jeu;
import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.*;
import org.junit.*;

import java.io.FileWriter;
import java.io.IOException;

public class PlateauTest{
    @Test
    public void catchExceptionFromGenerating(){
        try{
            for(int i = 0; i < 50; i++){
                Plateau p = new Plateau(6, 6, false);
                String mode = "compteur";
                int limite = 100;
                Jeu jeu = new Jeu(p.getPlateau(), 16, mode, limite);
            }
        }catch(RuntimeException e){
            e.printStackTrace();
            System.exit(0);
        }



    }
}
