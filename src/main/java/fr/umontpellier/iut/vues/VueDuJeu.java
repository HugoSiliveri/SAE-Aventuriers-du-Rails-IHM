package fr.umontpellier.iut.vues;


import fr.umontpellier.iut.IDestination;
import fr.umontpellier.iut.IJeu;
import fr.umontpellier.iut.rails.CouleurWagon;
import fr.umontpellier.iut.rails.Destination;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * Cette classe correspond à la fenêtre principale de l'application.
 *
 * Elle est initialisée avec une référence sur la partie en cours (Jeu).
 *
 * On y définit les bindings sur les éléments internes qui peuvent changer
 * (le joueur courant, les 5 cartes Wagons visibles, les destinations lors de l'étape d'initialisation de la partie, ...)
 * ainsi que les listeners à exécuter lorsque ces éléments changent
 */
public class VueDuJeu extends GridPane {

    private IJeu jeu;
    private VuePlateau plateau;
    private HBox listeDest;
    private VueJoueurCourant joueurCourant;
    private VueAutresJoueurs joueurs;
    private Button passer;
    private Label instruction;
    private VBox cartesWagonsVisibles;
    private VBox pioches;
    private Button piocheWagon;
    private Button piocheDestination;
    private StackPane defausse;

    public VueDuJeu(IJeu jeu) {
        this.jeu = jeu;

        //----- Vue PLATEAU -----//
        plateau = new VuePlateau();
        setConstraints(plateau, 0,0, 2, 1);
        GridPane.setHalignment(plateau, HPos.LEFT);
        GridPane.setValignment(plateau, VPos.TOP);
        GridPane.setHgrow(plateau, Priority.ALWAYS);
        GridPane.setVgrow(plateau, Priority.ALWAYS);
        getChildren().add(plateau);

        //----- Bouton PASSER -----//
        passer = new Button("Passer");
        passer.setId("passer");
        setConstraints(passer, 2, 1);
        GridPane.setHgrow(passer, Priority.ALWAYS);
        getChildren().add(passer);

        //----- HBOX de Boutons Destinations -----//
        listeDest = new HBox();
        setConstraints(listeDest, 1,2);
        GridPane.setHgrow(listeDest, Priority.ALWAYS);
        getChildren().add(listeDest);

        //----- JOUEUR COURANT -----//
        joueurCourant = new VueJoueurCourant(getJeu().getJoueurs().get(0));
        setConstraints(joueurCourant, 0, 1, 1, 3);
        GridPane.setHalignment(joueurCourant, HPos.LEFT);
        GridPane.setValignment(joueurCourant, VPos.BOTTOM);
        GridPane.setHgrow(joueurCourant, Priority.ALWAYS);

        getChildren().add(joueurCourant);

        //----- Vue JOUEURS -----//
        joueurs = new VueAutresJoueurs(getJeu().getJoueurs());
        setConstraints(joueurs, 1, 3);
        GridPane.setValignment(joueurs, VPos.BOTTOM);
        GridPane.setHalignment(joueurs, HPos.LEFT);
        GridPane.setHgrow(joueurs, Priority.ALWAYS);
        getChildren().add(joueurs);

        //----- Label Instruction -----//
        instruction = new Label();
        instruction.setTextAlignment(TextAlignment.CENTER);
        instruction.setFont(Font.font("Impact", FontWeight.EXTRA_BOLD, 35));
        instruction.setTextFill(Color.valueOf("#b35b12"));
        setConstraints(instruction, 1,1);
        instruction.setAlignment(Pos.TOP_CENTER);
        //GridPane.setHgrow(instruction, Priority.ALWAYS);
        getChildren().add(instruction);

        //----- Vue Cartes Wagon Visible -----//
        cartesWagonsVisibles = new VBox();
        //cartesWagonsVisibles.setBackground(new Background(new BackgroundFill(Color.valueOf("#9ea3a2"), CornerRadii.EMPTY, Insets.EMPTY)));
        cartesWagonsVisibles.setSpacing(5);
        cartesWagonsVisibles.setPadding(new Insets(5));
        setConstraints(cartesWagonsVisibles, 3, 0);
        GridPane.setValignment(cartesWagonsVisibles, VPos.TOP);
        GridPane.setHgrow(cartesWagonsVisibles, Priority.ALWAYS);
        getChildren().add(cartesWagonsVisibles);

        //----- Pioche Cartes Wagon -----//
        piocheWagon = new Button();
        ImageView imagePiocheWagon = new ImageView(new Image("images/cartesWagons/eu_WagonCard_back.png"));
        imagePiocheWagon.setPreserveRatio(true);
        imagePiocheWagon.setFitWidth(175);
        piocheWagon.setGraphic(imagePiocheWagon);
        piocheWagon.setPadding(Insets.EMPTY);

        //----- Pioche Destinations -----//
        piocheDestination= new Button();
        ImageView imagePiocheDestination = new ImageView(new Image("images/missions/eu_TicketBack.png"));
        imagePiocheDestination.setPreserveRatio(true);
        imagePiocheDestination.setFitWidth(175);
        piocheDestination.setGraphic(imagePiocheDestination);
        piocheDestination.setPadding(Insets.EMPTY);

        //----- Défausse -----//
        defausse = new StackPane();

        //----- VBox Pioches et Défausse -----//
        pioches = new VBox();
        pioches.getChildren().addAll(piocheWagon, piocheDestination, defausse);
        pioches.setSpacing(5);
        pioches.setPadding(new Insets(5));
        setConstraints(pioches, 2,0);
        pioches.setAlignment(Pos.TOP_CENTER);
        GridPane.setValignment(pioches, VPos.TOP);
        GridPane.setHgrow(pioches, Priority.ALWAYS);
        getChildren().add(pioches);


        setBackground(new Background(new BackgroundImage(new Image("images/fond.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(getWidth(), getHeight(), false, false, true, true))));
    }

    public IJeu getJeu() {
        return jeu;
    }

    public void creerBindings() {
        jeu.destinationsInitialesProperty().addListener((ListChangeListener<Destination>) change -> Platform.runLater(() -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Destination d : change.getAddedSubList()) {
                        VueDestination v = new VueDestination(d);
                        listeDest.getChildren().add(v);
                    }
                }
                if (change.wasRemoved()){
                    for(IDestination d : change.getRemoved()){
                        listeDest.getChildren().remove(trouveBoutonDestination(d));
                    }
                }
            }
        }));
        jeu.cartesWagonVisiblesProperty().addListener((ListChangeListener<CouleurWagon>) change -> Platform.runLater(() -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (CouleurWagon c : change.getAddedSubList()) {
                        VueCarteWagonVisible carte = new VueCarteWagonVisible(c);
                        cartesWagonsVisibles.getChildren().add(carte);
                        carte.creerBindings();
                    }
                }
                if (change.wasRemoved()) {
                    for (CouleurWagon c : change.getRemoved()) {
                        cartesWagonsVisibles.getChildren().remove(trouverBoutonWagonVisible(c));
                    }
                }
            }
        }));
        jeu.defausseCartesWagonProperty().addListener((ListChangeListener<CouleurWagon>) change -> Platform.runLater(() -> {
            while(change.next()){
                if(change.wasAdded()){
                    for (CouleurWagon c : change.getAddedSubList()){
                        VueCarteDefaussee carte =  new VueCarteDefaussee(c);
                        defausse.getChildren().add(carte);
                        carte.creerBindings();
                    }
                }
            }
        }));
        defausse.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> {
            for (int i =0; i<defausse.getChildren().size(); i++){
                Node v = defausse.getChildren().get(i);
                v.setTranslateY(i*20);
                v.setViewOrder(0);
            }
        });
        defausse.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> {
            for (Node v : defausse.getChildren()){
                v.setTranslateY(0);
                v.setViewOrder(1);
            }
        });

        passer.setOnAction(actionEvent -> jeu.passerAEteChoisi());
        joueurCourant.creerBindings();
        joueurs.creerBindings();
        plateau.creerBindings();
        piocheWagon.setOnAction(actionEvent -> jeu.uneCarteWagonAEtePiochee());
        piocheDestination.setOnAction(actionEvent -> jeu.uneDestinationAEtePiochee());
        instruction.textProperty().bind(jeu.instructionProperty());
        bindScene();
    }

    private Button trouveBoutonDestination(IDestination d) {
        for (Node n : listeDest.getChildren()) {
            if (n.getId().equals(d.getNom())) return (Button) n;
        }
        return null;
    }

    private Button trouverBoutonWagonVisible(CouleurWagon c) {
        for (Node n : cartesWagonsVisibles.getChildren()) {
            if (n.getId().equals(c.name())) return (Button) n;
        }
        return null;
    }

    private void bindScene() {
        bindPiochesEtDefausse();
        bindVBox(cartesWagonsVisibles);
        prefWidthProperty().bind(getScene().getWindow().widthProperty());
        prefHeightProperty().bind(getScene().getWindow().heightProperty());
    }

    private void bindPiochesEtDefausse() {
        bindPiochesCarte(piocheWagon);
        bindPiochesCarte(piocheDestination);
        bindDefausse();
        bindVBox(pioches);
        bindInstruction();
        bindPasser();
    }

    private void bindPiochesCarte(Button pioche) {
        ((ImageView) pioche.getGraphic()).fitWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(((VBox) pioche.getParent()).widthProperty());
            }
            @Override
            protected double computeValue() {
                return pioche.getParent().getLayoutBounds().getWidth() * 0.9;
            }
        });
        ((ImageView) pioche.getGraphic()).fitHeightProperty().bind(new DoubleBinding() {
            {
                super.bind(((VBox) pioche.getParent()).heightProperty());
            }
            @Override
            protected double computeValue() {
                return pioche.getParent().getLayoutBounds().getHeight() * 0.3;
            }
        });
    }

    private void bindDefausse() {
        defausse.prefWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(((VBox) defausse.getParent()).widthProperty());
            }
            @Override
            protected double computeValue() {
                return defausse.getParent().getLayoutBounds().getWidth() * 0.9;
            }
        });
        defausse.prefHeightProperty().bind(new DoubleBinding() {
            {
                super.bind(((VBox) defausse.getParent()).heightProperty());
            }
            @Override
            protected double computeValue() {
                return defausse.getParent().getLayoutBounds().getHeight() * 0.3;
            }
        });
    }

    private void bindVBox(VBox box) {
        box.minWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(box.getScene().widthProperty());
            }
            @Override
            protected double computeValue() {
                return box.getScene().getWidth() * 0.15;
            }
        });
        box.minHeightProperty().bind(new DoubleBinding() {
            {
                super.bind(box.getScene().heightProperty());
            }
            @Override
            protected double computeValue() {
                return box.getScene().getHeight() * 0.7;
            }
        });
        box.prefWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(box.getScene().widthProperty());
            }
            @Override
            protected double computeValue() {
                return box.getScene().getWidth() * 0.15;
            }
        });
        box.prefHeightProperty().bind(new DoubleBinding() {
            {
                super.bind(box.getScene().heightProperty());
            }
            @Override
            protected double computeValue() {
                return box.getScene().getHeight() * 0.7;
            }
        });
        box.maxWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(box.getScene().widthProperty());
            }
            @Override
            protected double computeValue() {
                return box.getScene().getWidth() * 0.15;
            }
        });
        box.maxHeightProperty().bind(new DoubleBinding() {
            {
                super.bind(box.getScene().heightProperty());
            }
            @Override
            protected double computeValue() {
                return box.getScene().getHeight() * 0.7;
            }
        });
    }

    private void bindInstruction() {
        instruction.prefWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(((GridPane) instruction.getParent()).widthProperty());
            }
            @Override
            protected double computeValue() {
                return instruction.getParent().getLayoutBounds().getWidth();
            }
        });
        instruction.prefHeightProperty().bind(new DoubleBinding() {
            {
                super.bind(((GridPane) instruction.getParent()).heightProperty());
            }
            @Override
            protected double computeValue() {
                return instruction.getParent().getLayoutBounds().getHeight();
            }
        });
        DoubleProperty size = new SimpleDoubleProperty(35);
        size.bind(instruction.widthProperty().add(instruction.heightProperty()).divide(instruction.getText().length() * 1.5));
        instruction.styleProperty().bind(Bindings.concat("-fx-font-size: ", size.asString(), "; -fx-font-weight: bolder; -fx-font-family: Impact; -fx-text-alignment: center;"));
    }

    private void bindPasser() {
        passer.prefWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(((GridPane) passer.getParent()).widthProperty());
            }
            @Override
            protected double computeValue() {
                return passer.getParent().getLayoutBounds().getWidth() * 0.05;
            }
        });
        passer.prefHeightProperty().bind(new DoubleBinding() {
            {
                super.bind(((GridPane) passer.getParent()).heightProperty());
            }
            @Override
            protected double computeValue() {
                return passer.getParent().getLayoutBounds().getHeight() * 0.05;
            }
        });
    }

}

