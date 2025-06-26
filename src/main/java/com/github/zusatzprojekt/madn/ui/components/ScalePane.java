package com.github.zusatzprojekt.madn.ui.components;

import javafx.beans.DefaultProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.List;

@DefaultProperty("children")
public class ScalePane extends Pane {
    private final ObjectProperty<Pos> alignmentProp = new SimpleObjectProperty<>(Pos.CENTER);


    // == Constructor ==================================================================================================

    public ScalePane() {
        createListeners();
    }

    public ScalePane(Node... children) {
        this();
        getChildren().addAll(children);
    }


    // == Initialization ===============================================================================================

    private void createListeners() {
        alignmentProp.addListener((observableValue, oldPos, pos) -> requestLayout());
    }


    // == Helper methods ===============================================================================================

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
