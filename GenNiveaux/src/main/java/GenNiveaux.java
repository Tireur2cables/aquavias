import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

/* Import with maven dependencies */
import org.json.JSONArray;
import org.json.JSONObject;

class GenNiveaux {

    private static String exportDir = "../resources/niveaux/niveau";

    public static void main(String[] args) {
        System.out.println("je suis gen niveau");
    }

    /*

    private static void exportNiveau() {
        String chemin = exportDir + numNiveau + ".json";
        JSONObject fic = this.createJSON();
        writeFile(fic, chemin);
    }

    private static JSONObject createJSON() {
        JSONObject fic = new JSONObject();
        fic.put("num", this.numNiveau);
        fic.put("hauteur", this.getHauteur());
        fic.put("largeur", this.getLargeur());
        fic.put("mode", this.mode);
        fic.put("limite", this.limite);
        fic.put("compteur", this.compteur);
        JSONArray niveau = this.saveNiveau();
        fic.put("niveau", niveau);
        return fic;
    }

    private static JSONArray saveNiveau() {
        JSONArray niveau = new JSONArray();
        for (int i = 0; i < this.getLargeur(); i++) {
            JSONArray ligne = new JSONArray();
            for (int j = 0; j < this.getHauteur(); j++) {
                Pont modPont = this.getPont(i,j);
                if (modPont != null) {
                    JSONArray pont = new JSONArray();
                    pont.put((modPont.forme + ""));
                    pont.put(modPont.orientation + "");
                    pont.put(modPont.spe);
                    ligne.put(pont);
                }else
                    ligne.put((Collection<?>) null);
            }
            niveau.put(ligne);
        }
        return niveau;
    }

    */

    private static void writeFile(JSONObject file, String chemin) {
        try {
            FileWriter fichier = new FileWriter(chemin);
            fichier.write(file.toString());
            fichier.close();
        }catch (IOException e) {
            throw new RuntimeException("Erreur d'écriture du fichier exporté");
        }
    }


}
