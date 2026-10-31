package org.coffe.tomoko.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.coffe.tomoko.extractor.MediaFormat;
import org.coffe.tomoko.extractor.stream.AudioStream;
import org.coffe.tomoko.extractor.stream.Stream;
import org.coffe.tomoko.extractor.stream.VideoStream;
import org.coffe.tomoko.util.StreamItemAdapter.StreamInfoWrapper;

import java.util.List;

public class SecondaryStreamHelper<T extends Stream> {
    private final int position;
    private final StreamInfoWrapper<T> streams;

    public SecondaryStreamHelper(@NonNull final StreamInfoWrapper<T> streams,
                                 final T selectedStream) {
        this.streams = streams;
        this.position = streams.getStreamsList().indexOf(selectedStream);
        if (this.position < 0) {
            throw new RuntimeException("selected stream not found");
        }
    }

    /**
     * Finds an audio stream compatible with the provided video-only stream, so that the two streams
     * can be combined in a single file by the downloader. If there are multiple available audio
     * streams, chooses either the highest or the lowest quality one based on
     * {@link ListHelper#isLimitingDataUsage(Context)}.
     *
     * @param context      Android context
     * @param audioStreams list of audio streams
     * @param videoStream  desired video-ONLY stream
     * @return the selected audio stream or null if a candidate was not found
     */
    @Nullable
    public static AudioStream getAudioStreamFor(@NonNull final Context context,
                                                @NonNull final List<AudioStream> audioStreams,
                                                @NonNull final VideoStream videoStream) {
        final MediaFormat mediaFormat = videoStream.getFormat();

        if (mediaFormat == MediaFormat.WEBM) {
            return audioStreams
                    .stream()
                    .filter(audioStream -> audioStream.getFormat() == MediaFormat.WEBMA
                            || audioStream.getFormat() == MediaFormat.WEBMA_OPUS)
                    .max(ListHelper.getAudioFormatComparator(MediaFormat.WEBMA,
                            ListHelper.isLimitingDataUsage(context)))
                    .orElse(null);

        } else if (mediaFormat == MediaFormat.MPEG_4) {
            return audioStreams
                    .stream()
                    .filter(audioStream -> audioStream.getFormat() == MediaFormat.M4A)
                    .max(ListHelper.getAudioFormatComparator(MediaFormat.M4A,
                            ListHelper.isLimitingDataUsage(context)))
                    .orElse(null);

        } else {
            return null;
        }
    }

    public T getStream() {
        return streams.getStreamsList().get(position);
    }

    public long getSizeInBytes() {
        return streams.getSizeInBytes(position);
    }
}
