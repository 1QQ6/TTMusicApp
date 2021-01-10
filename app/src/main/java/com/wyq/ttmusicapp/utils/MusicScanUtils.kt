package com.wyq.ttmusicapp.utils

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.wyq.ttmusicapp.Entity.Song


/**
 * @author Admin
 * @des
 * @version $
 * @updateAuthor $
 * @updateDes
 */
/**
 *
 * MediaStore.Audio.Media._ID,                 //歌曲ID
 * MediaStore.Audio.Media.TITLE,               //歌曲名称
 * MediaStore.Audio.Media.ARTIST,              //歌曲歌手
 * MediaStore.Audio.Media.ALBUM,               //歌曲的专辑名
 * MediaStore.Audio.Media.DURATION,            //歌曲时长
 * MediaStore.Audio.Media.DISPLAY_NAME,        //歌曲文件的名称
 * MediaStore.Audio.Media.DATA};               //歌曲文件的全路径
 */
class MusicScanUtils {

    companion object{
        /**
         * 媒体库查询语句（写一个工具类MusicUtils）
         */
        fun startScanLocalMusic(context:Context):List<Song>{
            val songDataList= mutableListOf<Song>()
            //开启子线程进行查询本地音乐
            Thread {
                var cursor: Cursor? = null
                try{
                    cursor = context.contentResolver.query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                        null, MediaStore.Audio.AudioColumns.IS_MUSIC)
                    if (cursor!=null && cursor.count!=0){
                        while (cursor.moveToNext()){
                            var songName =
                                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                            var singer =
                                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                            var path =
                                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                            val duration =
                                cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                            val size =
                                cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))
                            songName = realiseUnKnown(songName)
                            singer = realiseUnKnown(singer)
                            path = realiseUnKnown(path)

                            if (size > 1000*800){
                                if (songName!!.contains("-")){
                                    val split = songName.split("-")
                                    singer = split[0]
                                    songName = split[1]
                                }
                                songDataList.add(Song(songName,singer,path,duration,size))
                            }
                        }
                    }
                }catch (e:Exception){

                }finally {
                    // 释放资源
                    cursor!!.close();
                }
            }.start()
            return songDataList
        }

        /**
         * 格式化获取到的时间
         */
        fun formatTime(time: Int): String? {
            return if (time / 1000 % 60 < 10) {
                (time / 1000 / 60).toString() + ":0" + time / 1000 % 60
            } else {
                (time / 1000 / 60).toString() + ":" + time / 1000 % 60
            }
        }

        private fun realiseUnKnown(oldStr: String?): String? {
            var oldStr = oldStr
            try {
                if (oldStr != null) {
                    if (oldStr == "<unknown>") {
                        oldStr = oldStr.replace("<unknown>".toRegex(), "未知")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return oldStr
        }

    }
}