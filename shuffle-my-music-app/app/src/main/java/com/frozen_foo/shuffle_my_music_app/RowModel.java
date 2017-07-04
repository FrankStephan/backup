package com.frozen_foo.shuffle_my_music_app;

/**
 * Created by Frank on 04.07.2017.
 */

public class RowModel {

    private String label;
    private boolean checked;

    public RowModel(String label, boolean checked) {
        this.label = label;
        this.checked = checked;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
