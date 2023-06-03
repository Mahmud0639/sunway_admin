package com.manuni.sunwayadmin;

public class SliderModel {
    private String id, slider, title;

    public SliderModel() {
    }

    public SliderModel(String id, String slider, String title) {
        this.id = id;
        this.slider = slider;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlider() {
        return slider;
    }

    public void setSlider(String slider) {
        this.slider = slider;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
