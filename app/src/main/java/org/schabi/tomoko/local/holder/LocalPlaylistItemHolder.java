package org.coffe.tomoko.local.holder;

import android.view.View;
import android.view.ViewGroup;

import org.coffe.tomoko.database.LocalItem;
import org.coffe.tomoko.database.playlist.PlaylistDuplicatesEntry;
import org.coffe.tomoko.database.playlist.PlaylistMetadataEntry;
import org.coffe.tomoko.local.LocalItemBuilder;
import org.coffe.tomoko.local.history.HistoryRecordManager;
import org.coffe.tomoko.util.Localization;
import org.coffe.tomoko.util.image.CoilHelper;

import java.time.format.DateTimeFormatter;

public class LocalPlaylistItemHolder extends PlaylistItemHolder {

    private static final float GRAYED_OUT_ALPHA = 0.6f;

    public LocalPlaylistItemHolder(final LocalItemBuilder infoItemBuilder, final ViewGroup parent) {
        super(infoItemBuilder, parent);
    }

    LocalPlaylistItemHolder(final LocalItemBuilder infoItemBuilder, final int layoutId,
                            final ViewGroup parent) {
        super(infoItemBuilder, layoutId, parent);
    }

    @Override
    public void updateFromItem(final LocalItem localItem,
                               final HistoryRecordManager historyRecordManager,
                               final DateTimeFormatter dateTimeFormatter) {
        if (!(localItem instanceof PlaylistMetadataEntry item)) {
            return;
        }

        itemTitleView.setText(item.getOrderingName());
        itemStreamCountView.setText(Localization.localizeStreamCountMini(
                itemStreamCountView.getContext(), item.getStreamCount()));
        itemUploaderView.setVisibility(View.INVISIBLE);

        CoilHelper.INSTANCE.loadPlaylistThumbnail(itemThumbnailView, item.getThumbnailUrl());

        if (item instanceof PlaylistDuplicatesEntry
                && ((PlaylistDuplicatesEntry) item).getTimesStreamIsContained() > 0) {
            itemView.setAlpha(GRAYED_OUT_ALPHA);
        } else {
            itemView.setAlpha(1.0f);
        }

        super.updateFromItem(localItem, historyRecordManager, dateTimeFormatter);
    }
}
