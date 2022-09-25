package fr.umontpellier.iut.vues;


import fr.umontpellier.iut.IJoueur;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;


import java.util.List;

/**
 * Cette classe présente les éléments des joueurs autres que le joueur courant,
 * en cachant ceux que le joueur courant n'a pas à connaitre.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueAutresJoueurs extends HBox {


    private List<? extends IJoueur> joueurs;

    public VueAutresJoueurs(List<? extends IJoueur> joueurs) {
        this.joueurs = joueurs;
        setAlignment(Pos.BOTTOM_LEFT);
    }

    public void creerBindings() {
        ChangeListener<IJoueur> joueurCourantListener = new ChangeListener<IJoueur>() {
            @Override
            public void changed(ObservableValue<? extends IJoueur> observableValue, IJoueur iJoueur, IJoueur t1) {
                Platform.runLater(() -> {
                    getChildren().clear();
                    for (IJoueur j : joueurs) {
                        if (!j.equals(t1)) {
                            GridPane cadre = new GridPane();
                            cadre.setMaxHeight(60);
                            cadre.setMaxWidth(250);
                            cadre.setPrefSize(250,70);
                            ImageView avatar = new ImageView();
                            avatar.setFitHeight(45);
                            avatar.setFitWidth(30);
                            avatar.setPreserveRatio(true);
                            GridPane.setConstraints(avatar, 0, 0);
                            cadre.getChildren().add(avatar);


                            Label nomJoueur = new Label(j.getNom());
                            nomJoueur.setTextAlignment(TextAlignment.CENTER);
                            nomJoueur.setFont(Font.font("Courrier", FontWeight.BOLD, 20));
                            cadre.setHgap(60);
                            GridPane.setConstraints(nomJoueur, 1, 0);
                            cadre.getChildren().add(nomJoueur);
                            getChildren().add(cadre);
                            HBox.setHgrow(cadre, Priority.ALWAYS);

                            String couleur = "black";
                            switch (j.getCouleur().name()) {
                                case "BLEU" -> couleur = "#5185ea";
                                case "ROUGE" -> couleur = "#f15a42";
                                case "ROSE" -> couleur = "#f794f8";
                                case "JAUNE" -> couleur = "#f9f46f";
                                case "VERT" -> couleur = "#6efa6f";
                            }
                            cadre.setStyle(String.format("-fx-background-color: %s; -fx-border-radius: 20px; -fx-padding: 10px; -fx-background-radius: 20px", couleur));
                            Image image = new Image(String.format("images/avatars/avatar-%s.png", j.getCouleur().name()));

                            Label score = new Label(String.format("Score : %d", j.getScore()));
                            Label nbWagons = new Label(String.format("Wagons : %d", j.getNbWagons()));
                            Label nbCartes = new Label(String.format("Cartes wagon : %d", j.getCartesWagon().size()));
                            Label nbGares = new Label(String.format("Gares : %d", j.getNbGares()));
                            Label nbDestinations = new Label(String.format("Cartes destination : %d", j.getDestinations().size()));
                            cadre.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> {
                                GridPane.setConstraints(score, 0,1);
                                GridPane.setConstraints(nbWagons, 0,2, 2, 1);
                                GridPane.setConstraints(nbGares, 0, 3);
                                GridPane.setConstraints(nbCartes, 0, 4, 2, 1);
                                GridPane.setConstraints(nbDestinations, 0, 5, 2, 1);
                                cadre.getChildren().addAll(score, nbWagons, nbCartes, nbGares, nbDestinations);
                                bindLabel(score);
                                bindLabel(nbWagons);
                                bindLabel(nbCartes);
                                bindLabel(nbGares);
                                bindLabel(nbDestinations);
                            });
                            cadre.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> {
                                cadre.getChildren().removeAll(score, nbWagons, nbCartes, nbGares, nbDestinations);
                            });
                            avatar.setImage(image);
                            bindingAvatar(avatar);
                            bindNom(nomJoueur);
                            bindingBulle(cadre);
                        }
                    }
                });
            }
        };
        ((VueDuJeu) getScene().getRoot()).getJeu().joueurCourantProperty().addListener(joueurCourantListener);
        bindVue();

    }

    private void bindVue() {
        prefWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(getScene().widthProperty());
            }
            @Override
            protected double computeValue() {
                return getScene().getWidth();
            }
        });
        prefHeightProperty().bind(new DoubleBinding() {
            {
                super.bind(getScene().heightProperty());
            }
            @Override
            protected double computeValue() {
                return getScene().getHeight();
            }
        });
    }

    private void bindingAvatar(ImageView avatar){
        avatar.fitWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(((GridPane) avatar.getParent()).widthProperty());
            }
            @Override
            protected double computeValue() {
                return getLayoutBounds().getHeight() * 0.4;
            }
        });
        avatar.fitHeightProperty().bind(new DoubleBinding() {
            {
                super.bind(((GridPane) avatar.getParent()).heightProperty());
            }
            @Override
            protected double computeValue() {
                System.out.println();
                return getLayoutBounds().getHeight() * 0.4;
            }
        });
    }

    private void bindNom(Label label){
        label.prefWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(((GridPane) label.getParent()).prefWidthProperty());
            }

            @Override
            protected double computeValue() {
                return label.getParent().getLayoutBounds().getWidth();
            }
        });
        label.prefHeightProperty().bind(new DoubleBinding() {
            {
                super.bind(((GridPane)label.getParent()).prefHeightProperty());
            }

            @Override
            protected double computeValue() {
                return label.getParent().getLayoutBounds().getHeight();
            }
        });

        DoubleProperty size = new SimpleDoubleProperty(15);
        size.bind((((GridPane) getParent()).widthProperty().add(((GridPane) getParent()).heightProperty())).divide(150));
        label.styleProperty().bind(Bindings.concat("-fx-font-size: ",size.asString(), "; -fx-font-weight: bold", ";-fx-font-family: Courrier;"));
    }

    private void bindLabel(Label label) {
        label.prefWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(((GridPane) label.getParent()).prefWidthProperty());
            }

            @Override
            protected double computeValue() {
                return label.getParent().getLayoutBounds().getWidth();
            }
        });
        label.prefHeightProperty().bind(new DoubleBinding() {
            {
                super.bind(((GridPane)label.getParent()).prefHeightProperty());
            }

            @Override
            protected double computeValue() {
                return label.getParent().getLayoutBounds().getHeight();
            }
        });

        DoubleProperty size = new SimpleDoubleProperty(15);
        size.bind((((GridPane) getParent()).widthProperty().add(((GridPane) getParent()).heightProperty())).divide(200));
        label.styleProperty().bind(Bindings.concat("-fx-font-size: ",size.asString(), "; -fx-font-weight: bold", ";-fx-font-family: Courrier;"));
    }

    private void bindingBulle(GridPane cadre){
        cadre.prefWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(((HBox) cadre.getParent()).widthProperty());
            }
            @Override
            protected double computeValue() {
                return cadre.getParent().getLayoutBounds().getWidth();
            }
        });
        cadre.prefHeightProperty().bind(new DoubleBinding() {
            {
                super.bind(((HBox) cadre.getParent()).heightProperty());
            }
            @Override
            protected double computeValue() {
                return cadre.getParent().getLayoutBounds().getHeight();
            }
        });
    }
}
