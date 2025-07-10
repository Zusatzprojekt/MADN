package com.github.zusatzprojekt.madn.ui.components;

import javafx.beans.DefaultProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.List;

/**
 * Eine benutzerdefinierte JavaFX-Pane, die ihre Kinder skaliert, um in den verfügbaren Platz zu passen,
 * und sie gemäß der angegebenen Ausrichtung positioniert.
 *
 * <p>
 * Die Skalierung erfolgt proportional, sodass das Kind niemals verzerrt wird.
 * Das größte mögliche gleichmäßige Scaling wird verwendet, sodass das Kind vollständig in den Container passt.
 * </p>
 *
 * <p>
 * Zusätzlich wird das Kind entsprechend der Ausrichtung (CENTER, TOP_LEFT, etc.) innerhalb des verfügbaren Raumes platziert.
 * </p>
 */
@DefaultProperty("children")
public class ScalePane extends Pane {
    private final ObjectProperty<Pos> alignmentProp = new SimpleObjectProperty<>(Pos.CENTER);


    // == Constructor ==================================================================================================

    /**
     * Erstellt eine neue ScalePane mit Standardausrichtung (CENTER).
     */
    public ScalePane() {
        createListeners();
    }

    /**
     * Erstellt eine neue ScalePane und fügt die übergebenen Kinder hinzu.
     *
     * @param children Kinderknoten, die in die ScalePane aufgenommen werden sollen.
     */
    public ScalePane(Node... children) {
        this();
        getChildren().addAll(children);
    }


    // == Initialization ===============================================================================================

    /**
     * Fügt einen Listener hinzu, um bei Änderungen der Ausrichtung ein neues Layout auszulösen.
     */
    private void createListeners() {
        alignmentProp.addListener((observableValue, oldPos, pos) -> requestLayout());
    }


    // == Helper methods ===============================================================================================

    /**
     * Skaliert und positioniert einen einzelnen Kind-Knoten innerhalb des verfügbaren Bereichs.
     *
     * @param node            Der Knoten, bei dem das Layout angewendet werden soll.
     * @param insetTop        Obere Einrückung.
     * @param insetLeft       Linke Einrückung.
     * @param availWidth      Verfügbare Breite.
     * @param availHeight     Verfügbare Höhe.
     * @param baselineOffset  Baseline-Versatz (für Textunterstützung).
     * @param hPos            Horizontale Ausrichtung.
     * @param vPos            Vertikale Ausrichtung.
     */
    private void layoutNode(Node node, double insetTop, double insetLeft, double availWidth, double availHeight, double baselineOffset, HPos hPos, VPos vPos) {
        final Bounds bounds = node.getBoundsInLocal();
        final Bounds scaledBounds = node.getBoundsInParent();

        if (bounds.getHeight() != 0.0 && bounds.getWidth() != 0.0) {
            final double scaleHeight = availHeight / bounds.getHeight();
            final double scaleWidth = availWidth / bounds.getWidth();
            final double scale = Math.max(Math.min(scaleHeight, scaleWidth), 0);

            node.setScaleX(scale);
            node.setScaleY(scale);
        }

        if (scaledBounds.getHeight() != 0.0 && scaledBounds.getWidth() != 0.0) {

            switch (hPos) {
                case LEFT:
                    node.setTranslateX((scaledBounds.getWidth() / 2.0) - (bounds.getWidth() / 2.0));
                    break;

                case RIGHT:
                    node.setTranslateX(-(scaledBounds.getWidth() / 2.0) + (bounds.getWidth() / 2.0));
                    break;
            }

            switch (vPos) {
                case TOP:
                    node.setTranslateY((scaledBounds.getHeight() / 2.0) - (bounds.getHeight() / 2.0));
                    break;

                case BOTTOM:
                    node.setTranslateY(-(scaledBounds.getHeight() / 2.0) + (bounds.getHeight() / 2.0));
                    break;

                case BASELINE:
                    vPos = VPos.CENTER;
                    break;
            }
        }

        layoutInArea(node, insetLeft, insetTop, availWidth, availHeight, baselineOffset, hPos, vPos);
    }


    // == Overridden methods ===========================================================================================

    /**
     * Layoutet alle Kind-Knoten abhängig von der gewählten Ausrichtung und skaliert sie entsprechend.
     */
    @Override
    protected void layoutChildren() {
        List<Node> children = getManagedChildren();

        final double insetTop = getInsets().getTop();
        final double insetBottom = getInsets().getBottom();
        final double insetLeft = getInsets().getLeft();
        final double insetRight = getInsets().getRight();

        final double availableHeight = (getHeight() - insetTop - insetBottom);
        final double availableWidth = (getWidth() - insetLeft - insetRight);
        final double baselineOffset = getBaselineOffset();

        final HPos hPos = alignmentProp.getValue().getHpos();
        final VPos vPos = alignmentProp.getValue().getVpos();

        for (Node child: children) {
            layoutNode(child, insetTop, insetLeft, availableWidth, availableHeight, baselineOffset, hPos, vPos);
        }
    }


    // == Getter / Setter ==============================================================================================

    public Pos getAlignment() {
        return alignmentProp.getValue();
    }

    public void setAlignment(Pos value) {
        alignmentProp.setValue(value);
    }

}
