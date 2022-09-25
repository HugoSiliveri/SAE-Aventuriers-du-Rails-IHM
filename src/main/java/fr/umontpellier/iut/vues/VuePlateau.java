package fr.umontpellier.iut.vues;

import fr.umontpellier.iut.IJoueur;
import fr.umontpellier.iut.rails.Route;
import fr.umontpellier.iut.rails.Ville;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

/**
 * Cette classe présente les routes et les villes sur le plateau.
 *
 * On y définit le listener à exécuter lorsque qu'un élément du plateau a été choisi par l'utilisateur
 * ainsi que les bindings qui mettront ?à jour le plateau après la prise d'une route ou d'une ville par un joueur
 */
public class VuePlateau extends Pane {

    @FXML
    Group routes;

    @FXML
    Group villes;

    @FXML
    ImageView image;

    private Circle villeChoisie;
    private Group routeChoisie;

    public VuePlateau() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/plateau.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void choixRouteOuVille(MouseEvent mouseEvent) {
        String id = "";
        if (mouseEvent.getSource() instanceof Circle) {
            id = ((Circle) mouseEvent.getSource()).getId();
            villeChoisie = (Circle) mouseEvent.getSource();
        } else {
            id = ((Group) mouseEvent.getSource()).getId();
            routeChoisie = (Group) mouseEvent.getSource();
        }
        ((VueDuJeu) getScene().getRoot()).getJeu().uneVilleOuUneRouteAEteChoisie(id);

    }

    public void creerBindings() {
        ChangeListener<IJoueur> ecouteurVille = (observableValue, iJoueur, t1) -> Platform.runLater(() -> {
            constructionGare(villeChoisie, t1);
        });
        ChangeListener<IJoueur> ecouteurRoute = (observableValue, iJoueur, t1) -> Platform.runLater(() -> {
            captureRoute(routeChoisie, t1);
        });
        for (Object v : ((VueDuJeu) getScene().getRoot()).getJeu().getVilles()) {
            ((Ville) v).proprietaireProperty().addListener(ecouteurVille);
        }
        for (Object r : ((VueDuJeu) getScene().getRoot()).getJeu().getRoutes()) {
            ((Route) r).proprietaireProperty().addListener(ecouteurRoute);
        }
        bindRedimensionPlateau();
    }

    private void constructionGare(Circle ville, IJoueur j) {
        ImageView gare = new ImageView(String.format("images/gares/gare-%s.png", j.getCouleur().name()));
        gare.setPreserveRatio(true);
        gare.fitWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(ville.radiusProperty());
            }
            @Override
            protected double computeValue() {
                return ville.getRadius()*2.5;
            }
        });
        gare.layoutXProperty().bind(new DoubleBinding() {
            {
                super.bind(ville.layoutXProperty());
            }
            @Override
            protected double computeValue() {
                return ville.getLayoutX() - ville.getRadius()*1.5;
            }
        });
        gare.layoutYProperty().bind(new DoubleBinding() {
            {
                super.bind(ville.layoutYProperty());
            }
            @Override
            protected double computeValue() {
                return ville.getLayoutY() - ville.getRadius()*2;
            }
        });
        villes.getChildren().add(gare);
    }

    private void captureRoute(Group route, IJoueur j) {
        for(Node rect : route.getChildren()) {
            ImageView wagon = new ImageView(String.format("images/wagons/image-wagon-%s.png", j.getCouleur().name()));
            wagon.setPreserveRatio(true);
            wagon.fitWidthProperty().bind(((Rectangle) rect).widthProperty());
            wagon.rotateProperty().bind(rect.rotateProperty());
            wagon.layoutXProperty().bind(new DoubleBinding() {
                {
                    super.bind(rect.layoutXProperty());
                }
                @Override
                protected double computeValue() {
                    return rect.getLayoutX() - rect.getLayoutBounds().getWidth() * 0.50;
                }
            });
            wagon.layoutYProperty().bind(new DoubleBinding() {
                {
                    super.bind(rect.layoutYProperty());
                }
                @Override
                protected double computeValue() {
                    return rect.getLayoutY() - rect.getLayoutBounds().getHeight() * 1.70;
                }
            });
            routes.getChildren().add(wagon);
        }
    }

    private void bindRedimensionPlateau() {
        bindRoutes();
        bindVilles();
//        Les dimensions de l'image varient avec celle de la scène
        image.fitWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(((GridPane) getParent()).widthProperty());
            }
            @Override
            protected double computeValue() {
                return getParent().getLayoutBounds().getWidth() * 0.70;
            }
        });
        image.fitHeightProperty().bind(new DoubleBinding() {
            {
                super.bind(((GridPane) getParent()).heightProperty());
            }
            @Override
            protected double computeValue() {
                return getParent().getLayoutBounds().getHeight() * 0.70;
            }
        });
        prefWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(((GridPane) getParent()).widthProperty());
            }
            @Override
            protected double computeValue() {
                return getParent().getLayoutBounds().getWidth() * 0.70;
            }
        });
        prefHeightProperty().bind(new DoubleBinding() {
            {
                super.bind(((GridPane) getParent()).heightProperty());
            }
            @Override
            protected double computeValue() {
                return getParent().getLayoutBounds().getHeight() * 0.70;
            }
        });
    }

    private void bindRectangle(Rectangle rect, double layoutX, double layoutY) {
        rect.heightProperty().bind(new DoubleBinding() {
              {
                  super.bind(image.fitWidthProperty(), image.fitHeightProperty());
              }
              @Override
              protected double computeValue() {
                  return image.getLayoutBounds().getHeight() * DonneesPlateau.hauteurRectangle / DonneesPlateau.hauteurInitialePlateau;
              }
          });
        rect.widthProperty().bind(new DoubleBinding() {
             {
                 super.bind(image.fitWidthProperty(), image.fitHeightProperty());
             }
             @Override
             protected double computeValue() {
                 return image.getLayoutBounds().getWidth() * DonneesPlateau.largeurRectangle / DonneesPlateau.largeurInitialePlateau;
             }
          });
        rect.layoutXProperty().bind(new DoubleBinding() {
              {
                  super.bind(image.fitWidthProperty(), image.fitHeightProperty());
              }
              @Override
              protected double computeValue() {
                  return layoutX * image.getLayoutBounds().getWidth()/ DonneesPlateau.largeurInitialePlateau;
              }
          });
        rect.xProperty().bind(new DoubleBinding() {
            {
                super.bind(image.fitWidthProperty(), image.fitHeightProperty());
            }
            @Override
            protected double computeValue() {
                return DonneesPlateau.xInitial * image.getLayoutBounds().getWidth() / DonneesPlateau.largeurInitialePlateau;
            }
        });
        rect.layoutYProperty().bind(new DoubleBinding() {
              {
                  super.bind(image.fitWidthProperty(), image.fitHeightProperty());
              }
              @Override
              protected double computeValue() {
                 return layoutY * image.getLayoutBounds().getHeight()/ DonneesPlateau.hauteurInitialePlateau;
              }
          });
        rect.yProperty().bind(new DoubleBinding() {
            {
                super.bind(image.fitWidthProperty(), image.fitHeightProperty());
            }
            @Override
            protected double computeValue() {
                return DonneesPlateau.yInitial * image.getLayoutBounds().getHeight() / DonneesPlateau.hauteurInitialePlateau;
            }
        });
    }

    private void bindRoutes() {
        for (Node nRoute : routes.getChildren()) {
            Group gRoute = (Group) nRoute;
            int numRect = 0;
            for (Node nRect : gRoute.getChildren()) {
                Rectangle rect = (Rectangle) nRect;
                bindRectangle(rect, DonneesPlateau.getRoute(nRoute.getId()).get(numRect).getLayoutX(), DonneesPlateau.getRoute(nRoute.getId()).get(numRect).getLayoutY());
                numRect++;
            }
        }
    }

    private void bindVilles() {
        for (Node nVille : villes.getChildren()) {
            Circle ville = (Circle) nVille;
            bindVille(ville, DonneesPlateau.getVille(ville.getId()).getLayoutX(), DonneesPlateau.getVille(ville.getId()).getLayoutY());
        }
    }

    private void bindVille(Circle ville, double layoutX, double layoutY) {
        ville.layoutXProperty().bind(new DoubleBinding() {
            {
                super.bind(image.fitWidthProperty(), image.fitHeightProperty());
            }
            @Override
            protected double computeValue() {
                return layoutX * image.getLayoutBounds().getWidth()/ DonneesPlateau.largeurInitialePlateau;
            }
        });
        ville.layoutYProperty().bind(new DoubleBinding() {
            {
                super.bind(image.fitWidthProperty(), image.fitHeightProperty());
            }
            @Override
            protected double computeValue() {
                return layoutY * image.getLayoutBounds().getHeight()/ DonneesPlateau.hauteurInitialePlateau;
            }
        });
        ville.radiusProperty().bind(new DoubleBinding() {
            { super.bind(image.fitWidthProperty(), image.fitHeightProperty());}
            @Override
            protected double computeValue() {
                return DonneesPlateau.rayonInitial * image.getLayoutBounds().getWidth() / DonneesPlateau.largeurInitialePlateau;
            }
        });
    }

}
