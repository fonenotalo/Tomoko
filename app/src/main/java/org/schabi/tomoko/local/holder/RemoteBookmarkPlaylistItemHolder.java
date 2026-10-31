package org.coffe.tomoko.local.holder;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import org.coffe.tomoko.R;
import org.coffe.tomoko.database.LocalItem;
import org.coffe.tomoko.database.playlist.model.PlaylistRemoteEntity;
import org.coffe.tomoko.local.LocalItemBuilder;
import org.coffe.tomoko.local.history.HistoryRecordManager;

import java.time.format.DateTimeFormatter;

public class RemoteBookmarkPlaylistItemHolder extends RemotePlaylistItemHolder {
    private final View itemHandleView;

    public RemoteBookmarkPlaylistItemHolder(final LocalItemBuilder infoItemBuilder,
                                            final ViewGroup parent) {
        this(infoItemBuilder, R.layout.list_playlist_bookmark_item, parent);
    }

    RemoteBookmarkPlaylistItemHolder(final LocalItemBuilder infoItemBuilder, final int layoutId,
                                     final ViewGroup parent) {
        super(infoItemBuilder, layoutId, parent);
        itemHandleView = itemView.findViewById(R.id.itemHandle);
    }

    @Override
    public void updateFromItem(final LocalItem localItem,
                               final HistoryRecordManager historyRecordManager,
                               final DateTimeFormatter dateTimeFormatter) {
        if (!(localItem instanceof PlaylistRemoteEntity)) {
            return;
        }
        final PlaylistRemoteEntity item = (PlaylistRemoteEntity) localItem;

        itemHandleView.setOnTouchListener(getOnTouchListener(item));

        super.updateFromItem(localItem, historyRecordManager, dateTimeFormatter);
    }

    private View.OnTouchListener getOnTouchListener(final PlaylistRemoteEntity item) {
        return (view, motionEvent) -> {
            view.performClick();
            if (itemBuilder != null && itemBuilder.getOnItemSelectedListener() != null
                    && motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                itemBuilder.getOnItemSelectedListener().drag(item,
                        RemoteBookmarkPlaylistItemHolder.this);
            }
            return false;
        };
    }
}
