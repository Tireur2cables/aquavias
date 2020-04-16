package aquavias.jeu;

/**
 * Il faudrait que ces exceptions soient Checked, car elles dépendent de l'utilisateur (si il a supprimé les fichiers etc) et pas
 * de la programmation en elle même !
 */

class CantFindFolderException extends RuntimeException {

    public CantFindFolderException(String message) {
        super(message);
    }

}

class CantFindNiveauException extends RuntimeException {

    public CantFindNiveauException(String message) {
        super(message);
    }

}