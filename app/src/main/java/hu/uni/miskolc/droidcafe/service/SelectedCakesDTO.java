package hu.uni.miskolc.droidcafe.service;

import java.io.Serializable;

public class SelectedCakesDTO implements Serializable {

    private String name;
    private int image;
    private String descData;
    private boolean isSelected;

    public SelectedCakesDTO() {
    }

    public SelectedCakesDTO(String name, int image, String descData, boolean isSelected) {
        this.name = name;
        this.image = image;
        this.descData = descData;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDescData() {
        return descData;
    }

    public void setDescData(String descData) {
        this.descData = descData;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "SelectedCakesDTO{" +
                "name='" + name + '\'' +
                ", image=" + image +
                ", descData='" + descData + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}
