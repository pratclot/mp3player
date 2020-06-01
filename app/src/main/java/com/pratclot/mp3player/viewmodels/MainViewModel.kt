package com.pratclot.mp3player.viewmodels

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import javax.inject.Inject

class MainViewModel @Inject constructor(val context: Context) : ViewModel() {

    lateinit var exoPlayer: SimpleExoPlayer

    private var _navigateToPlaylist = MutableLiveData<Boolean>()
    val navigateToPlayList: LiveData<Boolean>
        get() = _navigateToPlaylist

    fun navigateToPlaylist() {
        _navigateToPlaylist.value = true
    }

    private var _audioList = MutableLiveData<List<Audio>>()
    val audioList: LiveData<List<Audio>>
        get() = _audioList

    private var _concatenatedSource = MutableLiveData<ConcatenatingMediaSource>()
    val concatenatedSource: LiveData<ConcatenatingMediaSource>
        get() = _concatenatedSource
    lateinit var nonLiveConcatenatedSource: ConcatenatingMediaSource

    val projection = arrayOf(
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.DISPLAY_NAME,
        MediaStore.Audio.Media.DATA
    )

    val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} DESC"

    init {
        _navigateToPlaylist.value = false
        initializePlayer()
        updateAudioList()
        updateExoPlayerPlayListWithNonLiveSource()
    }

    fun reinit() {
        updateAudioList()
        updateExoPlayerPlayListWithNonLiveSource()
    }

    private fun initializePlayer() {
        exoPlayer = ExoPlayerFactory.newSimpleInstance(context)
    }

    fun updateExoPlayerPlayList() {
        exoPlayer.prepare(concatenatedSource.value)
    }

    fun updateExoPlayerPlayListWithNonLiveSource() {
        exoPlayer.prepare(nonLiveConcatenatedSource)
    }

    fun updateExoPlayerPlayList(contentUri: String) {
        exoPlayer.prepare(buildMediaSource(Uri.parse(contentUri)))
    }

    private fun updateAudioList() {
        var tempList = mutableListOf<Audio>()
        var tempSource = ConcatenatingMediaSource()
        context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val nameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val contentUriColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            while (cursor.moveToNext()) {
                val name = cursor.getString(nameColumn)
                val artist = cursor.getString(artistColumn)
                val contentUri = cursor.getString(contentUriColumn)
                val mediaSource = buildMediaSource(Uri.parse(contentUri))
                tempList.add(Audio(name, artist, contentUri))
                tempSource.addMediaSource(mediaSource)
            }
        }
        _audioList.postValue(tempList)
        _concatenatedSource.postValue(tempSource)
        nonLiveConcatenatedSource = tempSource
    }

    fun buildMediaSource(uri: Uri): MediaSource {
        val dataSourceFactory = DefaultDataSourceFactory(context, "exoplayer")
        val mediaSourceFactory = ProgressiveMediaSource.Factory(dataSourceFactory)

        val mediaSource = mediaSourceFactory
            .setTag(uri)
            .createMediaSource(uri)
        return mediaSource
    }
}

data class Audio(
    val name: String,
    val artist: String,
    val contentUri: String
)
