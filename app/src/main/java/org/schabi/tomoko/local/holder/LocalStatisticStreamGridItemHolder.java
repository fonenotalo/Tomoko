package org.coffe.tomoko.local.holder;

import android.view.ViewGroup;

import org.coffe.tomoko.R;
import org.coffe.tomoko.local.LocalItemBuilder;

public class LocalStatisticStreamGridItemHolder extends LocalStatisticStreamItemHolder {
    public LocalStatisticStreamGridItemHolder(final LocalItemBuilder infoItemBuilder,
                                              final ViewGroup parent) {
        super(infoItemBuilder, R.layout.list_stream_grid_item, parent);
    }
}
