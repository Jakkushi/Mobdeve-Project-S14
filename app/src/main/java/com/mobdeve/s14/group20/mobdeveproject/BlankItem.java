package com.mobdeve.s14.group20.mobdeveproject;

import java.io.Serializable;

public class BlankItem extends Item implements Serializable {

    private String text;

    public BlankItem(String text) {
        super("Blank");

        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
