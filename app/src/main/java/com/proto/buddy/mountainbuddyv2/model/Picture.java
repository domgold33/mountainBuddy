package com.proto.buddy.mountainbuddyv2.model;

import java.io.Serializable;
import java.util.*;

/**
 * 
 */
public final class Picture extends Notice implements Serializable{

    /**
     * Pfad des Files, in dem das zugehoerige Foto abgespeichert wurde.
     */
    private String imagePath;

    /**
     * Erzeugt ein Bild, welches mit Geodaten getaggt wird.
     * @param place Ort, an dem das Bild aufgenommen wurde.
     * @param title Titel des Bildes.
     * @param text Beschreibung des Bildes.
     * @param path Pfas des Files, in dem das Foto abgespeichert wurde.
     */
    public Picture(Point place, String title, String text, String path) {
        super(place, title, text);
        this.imagePath = path;
    }

    public Picture(){
        super();
    }

    /**
     * @return
     */
    public String getImagePath() {
        return this.imagePath;
    }

    /**
     * @param path
     */
    public void setImagePath(String path) {
        this.imagePath = path;
    }

}