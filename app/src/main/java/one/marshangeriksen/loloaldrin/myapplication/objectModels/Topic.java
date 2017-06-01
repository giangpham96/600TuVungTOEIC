package one.marshangeriksen.loloaldrin.myapplication.objectModels;

import java.io.Serializable;


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
