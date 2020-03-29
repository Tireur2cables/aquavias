package aquavias.jeu;

class CantFindFolderException extends RuntimeException{
    /**
     * Il faudrait que cette exception soit Checked, car elle dépend de l'utilisateur (si il a supprimé les fichiers etc) et pas
     * de la programmation en elle même !
     */
    public CantFindFolderException(String message) {
        super(message);
    }
}

class CantFindNiveauException extends RuntimeException{
    /**
     * Il faudrait que cette exception soit Checked, car elle dépend de l'utilisateur (si il a supprimé les fichiers etc) et pas
     * de la programmation en elle même !
     */
    public CantFindNiveauException(String message) {
        super(message);
    }
}