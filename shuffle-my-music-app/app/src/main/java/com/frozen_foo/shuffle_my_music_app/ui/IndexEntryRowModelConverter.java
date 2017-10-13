package com.frozen_foo.shuffle_my_music_app.ui;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import java.util.List;

/**
 * Created by Frank on 16.09.2017.
 */

public class IndexEntryRowModelConverter {

	private RowModel toRowModel(IndexEntry indexEntry) {
		return new RowModel(indexEntry.getFileName(), indexEntry.getPath(), false);
	}

	public RowModel[] toRowModels(List<IndexEntry> indexEntries) {
		RowModel[] rowModels = new RowModel[indexEntries.size()];
		for (int i = 0; i < rowModels.length; i++) {
			rowModels[i] = toRowModel(indexEntries.get(i));
		}
		return rowModels;
	}

	private IndexEntry toIndexEntry(RowModel rowModel) {
		return new IndexEntry(rowModel.getLabel(), rowModel.getPath());
	}

	public List<IndexEntry> toIndexEntries(List<RowModel> rowModels) {
		return (List<IndexEntry>) CollectionUtils.collect(rowModels, new Transformer() {
			@Override
			public Object transform(final Object input) {
				return toIndexEntry((RowModel) input);
			}
		});
	}
}
