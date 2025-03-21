package com.github.zusatzprojekt.madn.ui.components;

import javafx.beans.DefaultProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.List;

@DefaultProperty("children")
public class ScalePane extends Pane {

    public ScalePane() {
        super();
    }

    public ScalePane(Node... children) {
        super();
        addChildren(children);
    }

    public void addChildren(Node... children) {
        getChildren().addAll(children);
        requestLayout();
    }

    @Override
    protected void layoutChildren() {
        List<Node> children = getManagedChildren();

        final double insetTop = getInsets().getTop();
        final double insetBottom = getInsets().getBottom();
        final double insetLeft = getInsets().getLeft();
        final double insetRight = getInsets().getRight();

        final double availWidth = (getWidth() - insetLeft - insetRight);
        final double availHeight = (getHeight() - insetTop - insetBottom);
        final double baselineOffset = getBaselineOffset();

        final HPos hPos = HPos.CENTER;
        final VPos vPos = VPos.CENTER;

        for (Node child : children) {
            layoutNode(child, insetTop, insetLeft, availWidth, availHeight, baselineOffset, hPos, vPos);
        }
    }

    private void layoutNode(Node node, double insetTop, double insetLeft, double availWidth, double availHeight, double baselineOffset, HPos hPos, VPos vPos) {
        final double originalWidth = node.getBoundsInLocal().getWidth();
        final double originalHeight = node.getBoundsInLocal().getWidth();

        final double scale = Math.max(Math.min(availWidth / originalWidth, availHeight / originalHeight), 0);

        node.setScaleX(scale);
        node.setScaleY(scale);

        layoutInArea(node, insetLeft, insetTop, availWidth, availHeight, baselineOffset, hPos, vPos);
    }
}
