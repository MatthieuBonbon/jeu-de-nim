package controleur;

import modele.Tas;
import vue.Ihm;

public class ConstructeurJeu {
    private final Ihm ihm;
    private Tas tas;

    public ConstructeurJeu(Ihm ihm) {
        this.ihm = ihm;
    }

    public void construireJeu(){
        //Création du nombre de tas
        int nbTas = ihm.interactionCreationTas();
        tas = new Tas(nbTas);
    }

    public Tas getLesTas() {
        return tas;
    }
}
