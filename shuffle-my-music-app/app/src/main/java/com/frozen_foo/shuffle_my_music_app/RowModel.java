package com.frozen_foo.shuffle_my_music_app;

/**
 * Created by Frank on 04.07.2017.
 */

public class RowModel {

	private String label;
	private String path;
	private boolean checked;

	public RowModel(String label, String path, boolean checked) {
		this.label = label;
		this.path = path;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
