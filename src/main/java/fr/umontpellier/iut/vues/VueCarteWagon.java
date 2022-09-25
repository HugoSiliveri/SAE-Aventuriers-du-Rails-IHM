package fr.umontpellier.iut.vues;

import fr.umontpellier.iut.ICouleurWagon;
import fr.umontpellier.iut.rails.CouleurWagon;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * Cette classe représente la vue d'une carte Wagon.
 *
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueCarteWagon extends Button {

    private ICouleurWagon couleurWagon;

    public VueCarteWagon(ICouleurWagon couleurWagon) {
        this.couleurWagon = couleurWagon;
        ImageView imageCarte = new ImageView();
        imageCarte.setImage(new Image(String.format("images/cartesWagons/carte-wagon-%s.png", ((CouleurWagon) couleurWagon).name())));
        setGraphic(imageCarte);
        imageCarte.setPreserveRatio(true);
        imageCarte.setFitWidth(100);
        setPadding(Insets.EMPTY);
        addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> {
            setViewOrder(0);
        });
        addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> {

            setViewOrder(1);
        });
        setOnAction(actionEvent -> {
            ((VueDuJeu) getScene().getRoot()).getJeu().uneCarteWagonAEteChoisie(couleurWagon);
            setViewOrder(1);
        });
    }

    public ICouleurWagon getCouleurWagon() {
        return couleurWagon;
    }

    public void creerBindings(){
        bindCarte();
    }

    private void bindCarte() {
        ((ImageView) getGraphic()).fitWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(((StackPane) getParent()).widthProperty());
            }
            @Override
            protected double computeValue() {
                return getParent().getLayoutBounds().getWidth()*0.75;
            }
        });
        ((ImageView) getGraphic()).fitHeightProperty().bind(new DoubleBinding() {
            {
                super.bind(((StackPane) getParent()).heightProperty());
            }
            @Override
            protected double computeValue() {
                return getParent().getLayoutBounds().getHeight()*0.5;
            }
        });
    }
}
