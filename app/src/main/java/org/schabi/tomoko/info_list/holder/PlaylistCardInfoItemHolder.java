package org.coffe.tomoko.info_list.holder;

import android.view.ViewGroup;

import org.coffe.tomoko.R;
import org.coffe.tomoko.info_list.InfoItemBuilder;

/**
 * Playlist card layout.
 */
public class PlaylistCardInfoItemHolder extends PlaylistMiniInfoItemHolder {

    public PlaylistCardInfoItemHolder(final InfoItemBuilder infoItemBuilder,
                                      final ViewGroup parent) {
        super(infoItemBuilder, R.layout.list_playlist_card_item, parent);
    }
}
