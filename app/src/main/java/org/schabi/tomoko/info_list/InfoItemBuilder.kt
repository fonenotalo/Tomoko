/*
 * SPDX-FileCopyrightText: 2016-2026 NewPipe contributors <https://newpipe.net>
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package org.coffe.tomoko.info_list

import android.content.Context
import org.coffe.tomoko.extractor.channel.ChannelInfoItem
import org.coffe.tomoko.extractor.comments.CommentsInfoItem
import org.coffe.tomoko.extractor.playlist.PlaylistInfoItem
import org.coffe.tomoko.extractor.stream.StreamInfoItem
import org.coffe.tomoko.util.OnClickGesture

class InfoItemBuilder(val context: Context) {
    var onStreamSelectedListener: OnClickGesture<StreamInfoItem>? = null
    var onChannelSelectedListener: OnClickGesture<ChannelInfoItem>? = null
    var onPlaylistSelectedListener: OnClickGesture<PlaylistInfoItem>? = null
    var onCommentsSelectedListener: OnClickGesture<CommentsInfoItem>? = null
}
