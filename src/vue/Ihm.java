package vue;

import java.util.Scanner;

public class Ihm {

    private final Scanner scanner = new Scanner(System.in);

    public boolean interactionChoixIA(){
        // On demande au joueur s'il veut jouer contre l'IA
        // ou contre une autre personne
        boolean jouerContreIA;
        System.out.println("Choisissez si vous voulez jouer " +
                "contre l'IA ou contre un autre joueur." +
                "\n Ecrivez <<oui>> our jouer contre l'IA " +
                "et <<non>> dans le cas contraire");
        String veutJouerContreIA;
        veutJouerContreIA = scanner.next();
        // Si la réponse donnée n'est pas valide
        if (!veutJouerContreIA.equals("oui") && !veutJouerContreIA.equals("non")) {
            System.out.println("Votre réponse n'est pas valide.");
            jouerContreIA = interactionChoixIA();
        }
        // Si veutContinuer.equals("oui") ou veutContinuer.equals("non")
        else jouerContreIA = veutJouerContreIA.equals("oui");
        return jouerContreIA;
    }

    public int interactionCreationTas(){
        // Choix du nombre de tas
        System.out.println("Choisissez le nombre de tas avec lequel vous allez jouer : ");
        int nbTas;
        if(scanner.hasNextInt()){
            nbTas = scanner.nextInt();
            if (nbTas<1){ // Si le nombre de tas est inférieur à 1
                System.out.println("Vous avez choisi moins d'un tas.");
                System.out.println("Choisissez au minimum un tas pour jouer.");
                nbTas = interactionCreationTas();
            }
        }
        else {
            if(!scanner.next().equals("")){ // Permet de ne pas avoir le message d'erreur en double à cause du scanner.nextLine()
                System.out.println("Seul un chiffre est autorisé.");
                scanner.nextLine();
            }
            nbTas = interactionCreationTas();
        }
        return nbTas;
    }

    public int interactionChoixLimiteAllumettes(){
        /* Demande à l'utilisateur s'il souhaite un nombre
           limite d'allumettes par coup à retirer */
        System.out.println("Choisissez un nombre limite d'allumettes à retirer par coup.");
        System.out.println("Si vous n'en souhaitez pas, choisissez 0");
        int nbLimiteParCoup;
        if(scanner.hasNextInt()) {
            nbLimiteParCoup = scanner.nextInt();
            if (nbLimiteParCoup < 0) {
                System.out.println("Le nombre limite d'allumettes choisi n'est pas valide.");
                nbLimiteParCoup = interactionChoixLimiteAllumettes();
            }
        }
        else {
            if(!scanner.next().equals("")){ // Permet de ne pas avoir le message d'erreur en double à cause du scanner.nextLine()
                System.out.println("Seul un chiffre est autorisé.");
                scanner.nextLine();
            }
            nbLimiteParCoup = interactionChoixLimiteAllumettes();
        }
        return nbLimiteParCoup;
    }

    public String interactionCreationJoueurs(String numJoueur){
        // Création des noms des joueurs

        System.out.println( numJoueur + ", choisissez un nom :");
        String nomJoueur = scanner.next();

        if (nomJoueur.isEmpty()){ // Si le joueur rentre un nom vide
            System.out.println("Attention " + numJoueur +" votre nom est vide.");
            nomJoueur = interactionCreationJoueurs(numJoueur);
        }
        return nomJoueur;
    }

    public void affichageAllumettes(int nbAllumettes){
        //Affiche les allumettes
        String allumettes;
        allumettes = "";
        for(int i = 0; i < nbAllumettes; i++){
            allumettes+=("| ");
        }
        System.out.println(allumettes);
    }

    public String interactionEnleverAllumettes(String nom){
        /* Demande au joueur le nombre d'allumettes il souhaite enlever
           dans un tas */
        System.out.println(nom + ", Saisissez un coup sous la forme suivante : 'm n' avec m le tas et n le nombre d'allumettes à enlever");
        String strAllumettes;
        strAllumettes = scanner.nextLine(); // Permet de sauter une ligne avec le scanner si besoin dans le but d'éviter des erreurs
        if (strAllumettes.isEmpty()){
            strAllumettes = scanner.nextLine();
        }
        for (int i = 0; i <strAllumettes.length(); i++){
            if(strAllumettes.substring(i, (i + 1)).equals(" ")){

                try {
                    int part1 = Integer.parseInt(strAllumettes.substring(0,i));
                    int part2 = Integer.parseInt(strAllumettes.substring(i+1));
                }

                catch (NumberFormatException e){ // Sinon on le prévient et on lui redemande de faire un choix
                    System.out.println("Seule une combinaison de deux chiffres est autorisée.");
                    strAllumettes = interactionEnleverAllumettes(nom);
                }

                break;
            }
        }

        return strAllumettes;
    }

    public void errSupNbAllumettes(){
        /* Message d'erreur si le joueur demande à retirer
           un nombre d'allumettes supérieur au nombre d'allumettes
           présent dans ce tas */
        System.out.println("On ne peut pas retirer les allumettes choisies.");
        System.out.println("Rejouez votre coup.");
    }

    public void affichageCoupIA(int i, int j){
        System.out.println("L' IA joue : " + i + " " + j);
    }

    public boolean interactionFinirJouer(String strNom){
        // On demande si ils veulent continuer à jouer
        boolean boolVeutContinuer;
        System.out.println(strNom + " a gagné ! \n " +
                "Voulez vous commencer une nouvelle partie ? " +
                "\n Ecrivez <<oui>> ou <<non>>");
        String veutContinuer;
        veutContinuer = scanner.next();
        // Si la réponse donnée n'est pas valide
        if(!veutContinuer.equals("oui") && !veutContinuer.equals("non")){
            System.out.println("Votre réponse n'est pas valide.");
            boolVeutContinuer = interactionFinirJouer(strNom);
        }
        // Si veutContinuer.equals("oui") ou veutContinuer.equals("non")
        else boolVeutContinuer = veutContinuer.equals("oui");
        return boolVeutContinuer;
    }

    public void affichageFinal(String nom1, String nom2, int gain1, int gain2){
        // Affiche le score final des deux joueurs
        System.out.println("SCORE FINAL \n");
        System.out.println(nom1 + " a gagné " + gain1 + " partie(s)");
        System.out.println(nom2 + " a gagné " + gain2 + " partie(s) \n");
        if(gain1 > gain2){
            System.out.println(nom1 + " gagne !");
        }
        else if(gain1 == gain2){
            System.out.println(nom1 + " et " + nom2 + " sont ex aequo !");
        }
        else {
            System.out.println(nom2 + " gagne !");
        }
    }
}
