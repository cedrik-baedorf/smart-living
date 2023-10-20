package smart.housing.controllers;

import javafx.collections.ObservableList;

public class Item {

    private String name;
    private double anzahl;
    private String einheit;

    private static ObservableList<Item> itemObservableList;

    public Item (String name, double anzahl, String einheit) {

        this.name = name;
        this.anzahl = anzahl;
        this.einheit = einheit;

        itemObservableList.add(this);
    }

    public static ObservableList<Item> getList() {
        return itemObservableList;
    }
}
