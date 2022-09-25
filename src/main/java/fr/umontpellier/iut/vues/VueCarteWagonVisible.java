package fr.umontpellier.iut.vues;

import fr.umontpellier.iut.ICouleurWagon;
import fr.umontpellier.iut.rails.CouleurWagon;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class VueCarteWagonVisible extends Button {

    private ICouleurWagon couleurWagon;

    public VueCarteWagonVisible(ICouleurWagon couleurWagon) {
        setId(((CouleurWagon) couleurWagon).name());
        this.couleurWagon = couleurWagon;
        ImageView imageCarte = new ImageView();
        imageCarte.setImage(new Image(String.format("images/cartesWagons/carte-wagon-%s.png", ((CouleurWagon) couleurWagon).name())));
        setGraphic(imageCarte);
        imageCarte.setPreserveRatio(true);
        imageCarte.setFitWidth(175);
        setPadding(Insets.EMPTY);
        addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> {
            setStyle("-fx-border-width: 3px; -fx-border-color: #315ed2;");
        });
        addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> {
            setStyle("-fx-border-width: 0px;");
        });
        setOnAction(actionEvent -> {
            ((VueDuJeu) getScene().getRoot()).getJeu().uneCarteWagonAEteChoisie(couleurWagon);
            setStyle("-fx-border-width: 0px;");
        });
    }

    public ICouleurWagon getCouleurWagon() {
        return couleurWagon;
    }

    public void creerBindings() {
        bindCarte();
    }

    private void bindCarte() {
        ((ImageView) getGraphic()).fitWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(((VBox) getParent()).widthProperty());
            }
            @Override
            protected double computeValue() {
                return getParent().getLayoutBounds().getWidth() * 0.9;
            }
        });
        ((ImageView) getGraphic()).fitHeightProperty().bind(new DoubleBinding() {
            {
                super.bind(((VBox) getParent()).heightProperty());
            }
            @Override
            protected double computeValue() {
                return getParent().getLayoutBounds().getHeight() / 5;
            }
        });
    }
}
