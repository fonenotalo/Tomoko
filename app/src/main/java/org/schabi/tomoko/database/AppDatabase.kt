/*
 * SPDX-FileCopyrightText: 2017-2024 NewPipe contributors <https://newpipe.net>
 * SPDX-FileCopyrightText: 2025 NewPipe e.V. <https://newpipe-ev.de>
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package org.coffe.tomoko.database

import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.RoomDatabase
import org.coffe.tomoko.database.feed.dao.FeedDAO
import org.coffe.tomoko.database.feed.dao.FeedGroupDAO
import org.coffe.tomoko.database.feed.model.FeedEntity
import org.coffe.tomoko.database.feed.model.FeedGroupEntity
import org.coffe.tomoko.database.feed.model.FeedGroupSubscriptionEntity
import org.coffe.tomoko.database.feed.model.FeedLastUpdatedEntity
import org.coffe.tomoko.database.history.dao.SearchHistoryDAO
import org.coffe.tomoko.database.history.dao.StreamHistoryDAO
import org.coffe.tomoko.database.history.model.SearchHistoryEntry
import org.coffe.tomoko.database.history.model.StreamHistoryEntity
import org.coffe.tomoko.database.playlist.dao.PlaylistDAO
import org.coffe.tomoko.database.playlist.dao.PlaylistRemoteDAO
import org.coffe.tomoko.database.playlist.dao.PlaylistStreamDAO
import org.coffe.tomoko.database.playlist.model.PlaylistEntity
import org.coffe.tomoko.database.playlist.model.PlaylistRemoteEntity
import org.coffe.tomoko.database.playlist.model.PlaylistStreamEntity
import org.coffe.tomoko.database.stream.dao.StreamDAO
import org.coffe.tomoko.database.stream.dao.StreamStateDAO
import org.coffe.tomoko.database.stream.model.StreamEntity
import org.coffe.tomoko.database.stream.model.StreamStateEntity
import org.coffe.tomoko.database.subscription.SubscriptionDAO
import org.coffe.tomoko.database.subscription.SubscriptionEntity

@ColumnTypeConverters(Converters::class)
@Database(
    version = Migrations.DB_VER_9,
    entities = [
        SubscriptionEntity::class,
        SearchHistoryEntry::class,
        StreamEntity::class,
        StreamHistoryEntity::class,
        StreamStateEntity::class,
        PlaylistEntity::class,
        PlaylistStreamEntity::class,
        PlaylistRemoteEntity::class,
        FeedEntity::class,
        FeedGroupEntity::class,
        FeedGroupSubscriptionEntity::class,
        FeedLastUpdatedEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun feedDAO(): FeedDAO
    abstract fun feedGroupDAO(): FeedGroupDAO
    abstract fun playlistDAO(): PlaylistDAO
    abstract fun playlistRemoteDAO(): PlaylistRemoteDAO
    abstract fun playlistStreamDAO(): PlaylistStreamDAO
    abstract fun searchHistoryDAO(): SearchHistoryDAO
    abstract fun streamDAO(): StreamDAO
    abstract fun streamHistoryDAO(): StreamHistoryDAO
    abstract fun streamStateDAO(): StreamStateDAO
    abstract fun subscriptionDAO(): SubscriptionDAO

    companion object {
        const val DATABASE_NAME: String = "newpipe.db"
    }
}
