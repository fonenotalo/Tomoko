package org.coffe.tomoko.local.holder;

import android.view.ViewGroup;

import org.coffe.tomoko.R;
import org.coffe.tomoko.local.LocalItemBuilder;

public class RemotePlaylistGridItemHolder extends RemotePlaylistItemHolder {
    public RemotePlaylistGridItemHolder(final LocalItemBuilder infoItemBuilder,
                                        final ViewGroup parent) {
        super(infoItemBuilder, R.layout.list_playlist_grid_item, parent);
    }
}
