package one.marshangeriksen.loloaldrin.myapplication.Models;

import java.io.Serializable;

/**
 * Created by conme on 30-May-17.
 */

public class Topic implements Serializable {
    private String name;
    private String image;

    public Topic(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
}
