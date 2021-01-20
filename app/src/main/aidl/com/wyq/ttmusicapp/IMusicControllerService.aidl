// IMusicControllerService.aidl
package com.wyq.ttmusicapp;
//手动添加包名，注意和项目报名保持一致
import com.wyq.ttmusicapp.entity.SongInfo;

// Declare any non-default types here with import statements

interface IMusicControllerService {

        int getPid();
        /**
         * Demonstrates some basic types that you can use as parameters
         * and return values in AIDL.
         */
        void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
                double aDouble, String aString);

        void play();

        void pause();

        void stop();

        void seekTo(int mesc);

        void preparePlayingList(int musicIndex,in List<SongInfo>  list,boolean isPlaying);

        boolean isPlaying();

        int getPlayingSongIndex();

        SongInfo getNowPlayingSong();

        boolean isForeground();

        void nextSong();

        void preSong();

        void randomSong();
}