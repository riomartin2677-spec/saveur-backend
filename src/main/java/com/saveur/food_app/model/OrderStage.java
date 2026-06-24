package com.saveur.food_app.model;

public enum OrderStage {
    PLACED, CONFIRMED, PREPARING, READY, DELIVERED;

    public OrderStage next() {
        OrderStage[] vals = values();
        return this.ordinal() < vals.length - 1
            ? vals[this.ordinal() + 1]
            : this;
    }
}