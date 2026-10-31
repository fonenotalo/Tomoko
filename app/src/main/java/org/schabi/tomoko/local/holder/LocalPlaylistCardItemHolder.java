package org.coffe.tomoko.local.holder;

import android.view.ViewGroup;

import org.coffe.tomoko.R;
import org.coffe.tomoko.local.LocalItemBuilder;

/**
 * Playlist card layout.
 */
public class LocalPlaylistCardItemHolder extends LocalPlaylistItemHolder {

    public LocalPlaylistCardItemHolder(final LocalItemBuilder infoItemBuilder,
                                       final ViewGroup parent) {
        super(infoItemBuilder, R.layout.list_playlist_card_item, parent);
    }
}
