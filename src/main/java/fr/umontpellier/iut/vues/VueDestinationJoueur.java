package fr.umontpellier.iut.vues;

import fr.umontpellier.iut.IDestination;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * Cette classe représente la vue d'une carte Destination.
 *
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueDestinationJoueur extends Button {

    private IDestination destination;

  public VueDestinationJoueur(IDestination destination) {
        this.destination = destination;
        ImageView imageCarte = new ImageView();
        String dest = destination.getNom();
        String[] d = dest.split(" ");

        setId(destination.getNom());

        imageCarte.setImage(new Image(String.format("images/missions/eu-%s-%s.png",d[0].toLowerCase(), d[2].toLowerCase())));
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
        setDisabled(true);
        setOpacity(1);
    }

    public IDestination getDestination() {
        return destination;
    }


    public void creerBindings() {
        bindDestination();
    }

    private void bindDestination() {
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
