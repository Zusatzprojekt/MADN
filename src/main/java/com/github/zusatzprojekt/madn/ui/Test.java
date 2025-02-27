package com.github.zusatzprojekt.madn.ui;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;

public class Test {
    IntegerProperty myInt = new SimpleIntegerProperty(0);

    public Test() {
        myInt.addListener((observableValue, oldNumber, number) -> {
            System.out.println(oldNumber + " | " + number);
        });
    }

    public void btnPlus(ActionEvent actionEvent) {
        myInt.set(myInt.get() + 1);
    }

    public void btnMinus(ActionEvent actionEvent) {
        myInt.set(myInt.get() - 1);
    }
}
