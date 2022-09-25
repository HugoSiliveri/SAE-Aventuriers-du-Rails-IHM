package fr.umontpellier.iut.vues;

import fr.umontpellier.iut.ICouleurWagon;
import fr.umontpellier.iut.IJoueur;
import fr.umontpellier.iut.rails.CouleurWagon;
import fr.umontpellier.iut.rails.Destination;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends GridPane {

    private IJoueur joueurCourant;
    private Label nomJoueur;
    private StackPane cartesWagon;
    private StackPane cartesDestination;
    private ImageView avatar;
    private GridPane inventaire;
    private ImageView iconeWagon;
    private Label nbWagon;
    private ImageView iconeGare;
    private Label nbGare;
    private Label score;

    public VueJoueurCourant(IJoueur joueur) {
        joueurCourant = joueur;

        //----- Image AVATAR JOUEUR -----//
        avatar = new ImageView();
        setConstraints(avatar, 0, 0);
        avatar.setPreserveRatio(true);
        getChildren().add(avatar);

        //----- Label NOM JOUEUR -----//
        nomJoueur = new Label(joueur.getNom());
        setConstraints(nomJoueur, 1, 0);
        GridPane.setHalignment(nomJoueur, HPos.CENTER);
        getChildren().add(nomJoueur);


        //----- StackPane CARTES WAGON -----//
        cartesWagon = new StackPane();
        setConstraints(cartesWagon, 1,1);
        GridPane.setHalignment(cartesWagon, HPos.CENTER);
        GridPane.setValignment(cartesWagon, VPos.TOP);
        cartesWagon.setAlignment(Pos.TOP_CENTER);
        getChildren().add(cartesWagon);

        //----- StackPane CARTES DESTINATION -----//
        cartesDestination = new StackPane();
        setConstraints(cartesDestination, 2,1);
        setHgrow(cartesDestination, Priority.ALWAYS);
        cartesDestination.setAlignment(Pos.TOP_CENTER);
        getChildren().add(cartesDestination);

        //----- GridPane INVENTAIRE -----//
        inventaire = new GridPane();
        inventaire.setAlignment(Pos.BASELINE_CENTER);
        setConstraints(inventaire,0, 1);
        setHgrow(inventaire, Priority.ALWAYS);
        getChildren().add(inventaire);


        iconeWagon = new ImageView();
        nbWagon = new Label();
        nbWagon.setFont(Font.font("Courrier", FontWeight.BOLD, 15));
        GridPane.setConstraints(iconeWagon, 0, 0);
        inventaire.getChildren().add(iconeWagon);
        GridPane.setConstraints(nbWagon, 1,0);
        inventaire.getChildren().add(nbWagon);
        setHgrow(nbWagon, Priority.ALWAYS);

        iconeGare = new ImageView();
        nbGare = new Label();
        nbGare.setFont(Font.font("Courrier", FontWeight.BOLD, 15));
        GridPane.setConstraints(iconeGare, 0, 1);
        inventaire.getChildren().add(iconeGare);
        GridPane.setConstraints(nbGare, 1, 1);
        inventaire.getChildren().add(nbGare);
        setHgrow(nbGare, Priority.ALWAYS);

        score = new Label();
        score.setFont(Font.font("Courrier", FontWeight.BOLD, 15));
        GridPane.setConstraints(score, 0, 2, 2, 1);
        inventaire.getChildren().add(score);
        setHgrow(score, Priority.ALWAYS);
    }

    public void creerBindings(){
        ChangeListener<IJoueur> listenerJoueurCourant = (observableValue, iJoueur, t1) -> Platform.runLater(() -> {
            // Nom Joueur
            nomJoueur.setText(t1.getNom());
            //nomJoueur.setTextAlignment(TextAlignment.CENTER);
            //nomJoueur.setFont(Font.font("Courrier", FontWeight.BOLD, 15));

            // Couleur Joueur
            String couleur = "black";
            switch (t1.getCouleur().name()) {
                case "BLEU" -> couleur = "#5185ea";
                case "ROUGE" -> couleur = "#f15a42";
                case "ROSE" -> couleur = "#f794f8";
                case "JAUNE" -> couleur = "#f9f46f";
                case "VERT" -> couleur = "#6efa6f";
            }
            setStyle(String.format("-fx-background-color: %s; -fx-border-radius: 20px; -fx-padding: 10px; -fx-background-radius: 20px", couleur));

            // Avatar Joueur
            Image image = new Image(String.format("images/avatars/avatar-%s.png", t1.getCouleur().name()));
            avatar.setImage(image);
            avatar.setPreserveRatio(true);

            // Cartes Wagon Joueur
            cartesWagon.getChildren().clear();
            for(CouleurWagon c : t1.getCartesWagon()) {
                VueCarteWagon v = new VueCarteWagon(c);
                v.setTranslateY(cartesWagon.getChildren().size()*10);
                cartesWagon.getChildren().add(v);
                v.creerBindings();
            }

            // Cartes Destination Joueur
            cartesDestination.getChildren().clear();
            for(Destination c : t1.getDestinations()) {
                VueDestinationJoueur v = new VueDestinationJoueur(c);
                v.setTranslateY(cartesDestination.getChildren().size()*10);
                cartesDestination.getChildren().add(v);
                v.creerBindings();
            }


            // Inventaire Joueur
            iconeWagon.setImage(new Image(String.format("images/wagons/image-wagon-%s.png", t1.getCouleur().name())));
            iconeWagon.setPreserveRatio(true);
            nbWagon.setText(String.format("x%d", t1.getNbWagons()));

            iconeGare.setImage(new Image(String.format("images/gares/gare-%s.png", t1.getCouleur().name())));
            iconeGare.setPreserveRatio(true);
            nbGare.setText(String.format("x%d", t1.getNbGares()));
            score.setText(String.format("score = %d", t1.getScore()));
        });
        ((VueDuJeu) getScene().getRoot()).getJeu().joueurCourantProperty().addListener(listenerJoueurCourant);
        joueurCourant.cartesWagonProperty().addListener((ListChangeListener<CouleurWagon>) change -> Platform.runLater(() -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (CouleurWagon c : change.getAddedSubList()) {
                        VueCarteWagon v = new VueCarteWagon(c);
                        cartesWagon.getChildren().add(v);
                        v.creerBindings();
                    }
                }
                if (change.wasRemoved()) {
                    for (CouleurWagon c : change.getRemoved()) {
                        cartesWagon.getChildren().remove(trouverCartesWagon(c));
                    }
                }
            }
        }));
        bindJoueurCourant();
    }

    private VueCarteWagon trouverCartesWagon(ICouleurWagon c) {
        for(Node n : cartesWagon.getChildren()) {
            if (((VueCarteWagon) n).getCouleurWagon() == c) return (VueCarteWagon) n;
        }
        return null;
    }

    public IJoueur getJoueurCourant() { return joueurCourant; }


    private void bindBulle(){
        minWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(getScene().widthProperty());
            }
            @Override
            protected double computeValue() {
                return  getParent().getLayoutBounds().getWidth() * 0.2;
            }
        });
        minHeightProperty().bind(new DoubleBinding() {
            {
                super.bind(getScene().widthProperty());
            }
            @Override
            protected double computeValue() {
                return getParent().getLayoutBounds().getHeight() * 0.25;
            }
        });
        prefWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(getScene().widthProperty());
            }
            @Override
            protected double computeValue() {
                return getParent().getLayoutBounds().getWidth() * 0.2;
            }
        });
        prefHeightProperty().bind(new DoubleBinding() {
            {
                super.bind(getScene().heightProperty());
            }
            @Override
            protected double computeValue() {
                return getParent().getLayoutBounds().getHeight() * 0.25;
            }
        });
        maxWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(getScene().widthProperty());
            }
            @Override
            protected double computeValue() {
                return getParent().getLayoutBounds().getWidth() * 0.2;
            }
        });
        maxHeightProperty().bind(new DoubleBinding() {
            {
                super.bind(getScene().heightProperty());
            }
            @Override
            protected double computeValue() {
                return getParent().getLayoutBounds().getHeight() * 0.25;
            }
        });
    }

    private void bindInventaire(){
        inventaire.prefWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(((GridPane) inventaire.getParent()).prefWidthProperty());
            }
            @Override
            protected double computeValue() {
                return inventaire.getParent().getLayoutBounds().getWidth()/3;
            }
        });
        inventaire.prefHeightProperty().bind(new DoubleBinding() {
            {
                super.bind(((GridPane) inventaire.getParent()).prefHeightProperty());
            }
            @Override
            protected double computeValue() {
                return inventaire.getParent().getLayoutBounds().getHeight()/2;
            }
        });
    }

    private void bindPileCartes(StackPane pane){
        pane.prefWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(((GridPane) pane.getParent()).prefWidthProperty());
            }
            @Override
            protected double computeValue() {
                return pane.getParent().getLayoutBounds().getWidth()/3;
            }
        });
        pane.prefHeightProperty().bind(new DoubleBinding() {
            {
                super.bind(((GridPane) pane.getParent()).prefHeightProperty());
            }
            @Override
            protected double computeValue() {
                return pane.getParent().getLayoutBounds().getHeight()/2;
            }
        });
    }

    private void bindImagesInventaire(ImageView image){
        image.fitWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(((GridPane) image.getParent()).prefWidthProperty());
            }
            @Override
            protected double computeValue() {
                return image.getParent().getLayoutBounds().getWidth() * 0.75;
            }
        });
        image.fitHeightProperty().bind(new DoubleBinding() {
            {
                super.bind(((GridPane) image.getParent()).prefHeightProperty());
            }
            @Override
            protected double computeValue() {
                return image.getParent().getLayoutBounds().getHeight() * 0.75;
            }
        });
    }

    private void bindAvatar(){
        avatar.fitWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(((GridPane) avatar.getParent()).prefWidthProperty());
            }
            @Override
            protected double computeValue() {
                return avatar.getParent().getLayoutBounds().getWidth() * 0.10 ;
            }
        });
        avatar.fitHeightProperty().bind(new DoubleBinding() {
            {
                super.bind(((GridPane) avatar.getParent()).prefHeightProperty());
            }
            @Override
            protected double computeValue() {
                return avatar.getParent().getLayoutBounds().getWidth() * 0.10;
            }
        });
    }

    private void bindLabel(Label label) {
        label.prefWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(((GridPane) label.getParent()).widthProperty());
            }

            @Override
            protected double computeValue() {
                return label.getParent().getLayoutBounds().getWidth()/4;
            }
        });
        label.prefHeightProperty().bind(new DoubleBinding() {
            {
                super.bind(((GridPane)label.getParent()).heightProperty());
            }

            @Override
            protected double computeValue() {
                return label.getParent().getLayoutBounds().getHeight()/5;
            }
        });

        DoubleProperty size = new SimpleDoubleProperty(15);
        size.bind((((GridPane) getParent()).widthProperty().add(((GridPane) getParent()).heightProperty())).divide(200));
        label.styleProperty().bind(Bindings.concat("-fx-font-size: ",size.asString(), "; -fx-font-weight: bold", ";-fx-font-family: Courrier"));
    }




    private void bindJoueurCourant(){
        bindAvatar();
        bindImagesInventaire(iconeWagon);
        bindImagesInventaire(iconeGare);
        bindLabel(nbGare);
        bindLabel(nbWagon);
        bindLabel(score);
        bindLabel(nomJoueur);
        bindPileCartes(cartesWagon);
        bindPileCartes(cartesDestination);
        bindInventaire();
        bindBulle();
    }
}
