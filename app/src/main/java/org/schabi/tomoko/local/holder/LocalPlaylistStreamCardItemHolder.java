package org.coffe.tomoko.local.holder;

import android.view.ViewGroup;

import org.coffe.tomoko.R;
import org.coffe.tomoko.local.LocalItemBuilder;

/**
 * Local playlist stream UI. This also includes a handle to rearrange the videos.
 */
public class LocalPlaylistStreamCardItemHolder extends LocalPlaylistStreamItemHolder {

    public LocalPlaylistStreamCardItemHolder(final LocalItemBuilder infoItemBuilder,
                                             final ViewGroup parent) {
        super(infoItemBuilder, R.layout.list_stream_playlist_card_item, parent);
    }
}
