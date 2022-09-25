package fr.umontpellier.iut.vues;

import fr.umontpellier.iut.ICouleurWagon;
import fr.umontpellier.iut.rails.CouleurWagon;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class VueCarteDefaussee extends Button {

    private ICouleurWagon couleurWagon;

    public VueCarteDefaussee(ICouleurWagon couleurWagon) {
        setId(((CouleurWagon) couleurWagon).name());
        this.couleurWagon = couleurWagon;
        ImageView imageCarte = new ImageView();
        imageCarte.setImage(new Image(String.format("images/cartesWagons/carte-wagon-%s.png", ((CouleurWagon) couleurWagon).name())));
        setGraphic(imageCarte);
        imageCarte.setPreserveRatio(true);
        imageCarte.setFitWidth(175);
        setPadding(Insets.EMPTY);
        setDisable(true);
        setOpacity(0.7);
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
                super.bind(((VBox) getParent().getParent()).widthProperty());
            }

            @Override
            protected double computeValue() {
                return getParent().getParent().getLayoutBounds().getWidth() * 0.9;
            }
        });
        ((ImageView) getGraphic()).fitHeightProperty().bind(new DoubleBinding() {
            {
                super.bind(((VBox) getParent().getParent()).heightProperty());
            }
            @Override
            protected double computeValue() {
                return getParent().getParent().getLayoutBounds().getHeight() * 0.3;
            }
        });
    }
}
