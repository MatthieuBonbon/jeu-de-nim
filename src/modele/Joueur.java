package modele;

public class Joueur {

    private final String nom;
    private int nb_parties_gagnees;

    public Joueur(String nom) {
        this.nom = nom;
        this.nb_parties_gagnees = 0; // Car le Joueur commence toujours avec 0 victoire
    }

    public String getNom() {
        return nom;
    }

    public int getNb_parties_gagnees() {
        return nb_parties_gagnees;
    }

    public void partieGagnee(){
        nb_parties_gagnees++;
    }
}
