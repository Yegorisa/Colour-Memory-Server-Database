package com.egoregorov.colourmemory.model;

public class Card {
    private int mImageResourceId;
    private boolean mSelected;

    public Card(int imageResourceId) {
        mImageResourceId = imageResourceId;
        mSelected = false;
    }

    public int getImageResourceId() {
        return mImageResourceId;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
    }
}
