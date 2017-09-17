package com.frozen_foo.shuffle_my_music_app.main;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;

/**
 * Created by Frank on 16.09.2017.
 */

public class IndexEntryRowModelConverter {

	private RowModel toRowModel(IndexEntry indexEntry) {
		return new RowModel(indexEntry.getFileName(), indexEntry.getPath(), false);
	}

	public RowModel[] toRowModels(IndexEntry[] indexEntries) {
		RowModel[] rowModels = new RowModel[indexEntries.length];
		for (int i = 0; i < rowModels.length; i++) {
			rowModels[i] = toRowModel(indexEntries[i]);
		}
		return rowModels;
	}

	private IndexEntry toIndexEntry(RowModel rowModel) {
		return new IndexEntry(rowModel.getLabel(), rowModel.getPath());
	}

	public IndexEntry[] toIndexEntries(RowModel[] rowModels) {
		IndexEntry[] indexEntries = new IndexEntry[rowModels.length];
		for (int i = 0; i < indexEntries.length; i++) {
			indexEntries[i] = toIndexEntry(rowModels[i]);
		}
		return indexEntries;
	}

}
