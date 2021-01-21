package com.wyq.ttmusicapp.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.wyq.ttmusicapp.entity.SongInfo
import java.util.*

/**
 * Created by Roman on 2021/1/11
 */
class DatabaseManager(context: Context) {

    private val databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private val db: SQLiteDatabase

    companion object {
        private val TAG = DatabaseManager::class.java.name
        private var instance: DatabaseManager? = null

        @JvmStatic
        @Synchronized
        fun getInstance(context: Context?): DatabaseManager? {
            if (instance == null) {
                instance = DatabaseManager(context!!)
            }
            return instance
        }
    }

    init {
        db = databaseHelper.writableDatabase
    }

    /**
     * 更新所有音乐数据
     *
     * 使用SQLiteDatabase的beginTransaction()方法可以开启一个事务
     * 程序执行到endTransaction() 方法时会检查事务的标志是否为成功，
     * 如果程序执行到endTransaction()之前调用了setTransactionSuccessful()
     * 方法设置事务的标志为成功则提交事务，
     * 如果没有调用setTransactionSuccessful() 方法则回滚事务
     * 事务处理应用：很多时候我们需要批量的向Sqlite中插入大量数据时，单独的使用添加方法导致应用响应缓慢,
     * 因为sqlite插入数据的时候默认一条语句就是一个事务，
     * 有多少条数据就有多少次磁盘操作。
     * 如初始8000条记录也就是要8000次读写磁盘操作。
     * 同时也是为了保证数据的一致性，避免出现数据缺失等情况
     */
    fun updateAllMusic(musicsList: ArrayList<SongInfo>) {
        db.beginTransaction()
        try {
            deleteAllTable()
            insertMusicListToMusicTable(musicsList)
            db.setTransactionSuccessful()
        } catch (e: Exception) {

        } finally {
            db.endTransaction()
        }
    }

    /**
     * 遍历插入音乐到数据库中
     */
    private fun insertMusicListToMusicTable(musicInfoList: List<SongInfo>) {
        for (musicInfo in musicInfoList) {
            insertMusicInfoToMusicTable(musicInfo)
        }
    }

    /**
     * 添加音乐到音乐表中
     */
    private fun insertMusicInfoToMusicTable(musicInfo: SongInfo) {
        val musicInfoToContentValues: ContentValues
        var cursor: Cursor? = null
        try {
            musicInfoToContentValues = musicInfoToContentValues(musicInfo)
            //将musicInfo转换后的数据插入到MUSIC_TABLE中
            db.insert(DatabaseHelper.MUSIC_TABLE, null, musicInfoToContentValues)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
    }

    /**
     * 音乐转化成ContentValues
     */
    private fun musicInfoToContentValues(musicInfo: SongInfo): ContentValues {
        val values = ContentValues()
        try {
            values.run {
                put(DatabaseHelper.ID_COLUMN,musicInfo.music_id)
                put(DatabaseHelper.MUSIC_NAME_COLUMN, musicInfo.musicName)
                put(DatabaseHelper.MUSIC_SINGER_COLUMN, musicInfo.musicSinger)
                put(DatabaseHelper.MUSIC_ALBUM_COLUMN, musicInfo.musicAlbum)
                put(DatabaseHelper.MUSIC_DURATION_COLUMN, musicInfo.musicDuration)
                put(DatabaseHelper.MUSIC_PATH_COLUMN, musicInfo.musicPath)
                put(DatabaseHelper.MUSIC_PARENT_PATH_COLUMN, musicInfo.musicParentPath)
                put(DatabaseHelper.MUSIC_LOVE_COLUMN, musicInfo.musicLove)
                put(DatabaseHelper.MUSIC_FIRST_LETTER_COLUMN, musicInfo.musicFirstLetter)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return values
    }

    private fun deleteAllTable() {
        //在SQLite中启用外键支持,必须在运行时打开, 因为默认是关闭的
        db.execSQL("PRAGMA foreign_keys = on")
        db.delete(DatabaseHelper.MUSIC_TABLE, null, null)
        db.delete(DatabaseHelper.RECENT_PLAY_TABLE, null, null)
        db.delete(DatabaseHelper.MUSIC_LIST_TABLE, null, null)
        db.delete(DatabaseHelper.MUSIC_MUSIC_LIST_TABLE, null, null)
    }

    fun getMusicPath(curMusicId: Long): String? {
        if (curMusicId == -1L) {
            return ""
        }
        var path: String? = null
        var cursor: Cursor? = null
        //每次播放一首新歌前都需要获取歌曲路径，所以可以在此设置最近播放
        setLastPlay(curMusicId)
        try {
            //TODO  测试rawQuery
            cursor = db.query(
                DatabaseHelper.MUSIC_TABLE,
                null,
                DatabaseHelper.ID_COLUMN + " = ?",
                arrayOf("" + curMusicId),
                null,
                null,
                null
            )
            Log.i(TAG, "getCount: " + cursor.count)
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MUSIC_PATH_COLUMN))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor!!.close()
        }

        return path
    }

    /**
     * 设置最近播放的20首歌曲
     */
    private fun setLastPlay(curMusicId: Long) {
        if (curMusicId == -1L || curMusicId == 0L) {
            return
        }
        val values = ContentValues()
        val lastList = ArrayList<Long>()
        var cursor: Cursor? = null
        lastList.add(curMusicId)
        db.beginTransaction()
        try {
            //首先查询历史播放表
            cursor = db.rawQuery("select id from " + DatabaseHelper.RECENT_PLAY_TABLE, null)
            //将历史播放表中所有的音乐id添加到临时的list中，不包括当前播放的id
            while (cursor.moveToNext()) {
                if (cursor.getLong(0) != curMusicId) {
                    lastList.add(cursor.getLong(0))
                }
            }
            //删除历史播放表数据
            db.delete(DatabaseHelper.RECENT_PLAY_TABLE, null, null)
            //将最新的历史播放列表插入数据库中
            for (i in 0..19) {
                values.put(DatabaseHelper.ID_COLUMN, lastList[i])
                db.insert(DatabaseHelper.RECENT_PLAY_TABLE, null, values)
            }
            db.setTransactionSuccessful()
            /*if (lastList.size<20){
                lastList.forEach {
                    values.put(DatabaseHelper.ID_COLUMN,it)
                    db.insert(DatabaseHelper.RECENT_PLAY_TABLE,null,values)
                }
            }else{

            }*/

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            cursor!!.close()
            db.endTransaction()
        }
    }

    /**
     * 获取音乐表中的第一首音乐的ID
     */
    fun getFirstId(listAllMusic: Any): String? {
        return null
    }

    /**
     * 获取所有的音乐数据，返回为集合
     */
    fun getAllMusicFromMusicTable(): ArrayList<SongInfo> {
        Log.d(TAG, "getAllMusicFromMusicTable: ")
        var musicInfoList = ArrayList<SongInfo>()
        var cursor: Cursor? = null
        //开启一个事务
        db.beginTransaction()
        try {
            cursor = db.query(DatabaseHelper.MUSIC_TABLE, null, null, null, null, null, null, null)
            musicInfoList = cursorToSongsList(cursor)

            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
            cursor?.close()
        }
        return musicInfoList
    }

    /**
     * 数据库数据存到集合中
     */
    private fun cursorToSongsList(cursor: Cursor?): ArrayList<SongInfo> {
        var musicsList: ArrayList<SongInfo>? = null
        try {
            if (cursor != null) {
                musicsList = ArrayList()
                while (cursor.moveToNext()) {
                    val musicId = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.ID_COLUMN))
                    val musicName =
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.MUSIC_NAME_COLUMN))
                    val musicSinger =
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.MUSIC_SINGER_COLUMN))
                    val musicAlbum =
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.MUSIC_ALBUM_COLUMN))
                    val musicPath =
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.MUSIC_PATH_COLUMN))
                    val musicParentPath =
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.MUSIC_PARENT_PATH_COLUMN))
                    val musicFirstLetter =
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.MUSIC_FIRST_LETTER_COLUMN))
                    val musicLove =
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.MUSIC_LOVE_COLUMN))
                    val musicDuration =
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.MUSIC_DURATION_COLUMN))

                    val songInfo = SongInfo(
                        musicId,
                        musicName,
                        musicSinger,
                        musicDuration,
                        musicAlbum,
                        musicPath,
                        musicParentPath,
                        musicFirstLetter,
                        musicLove
                    )
                    musicsList.add(songInfo)
                }
            }
        } catch (e: Exception) {

        }
        return musicsList!!
    }

    /**
     * 根据id返回数据库中的一条歌曲信息
     */
    fun getSongInfo(nextMusicId: Long): SongInfo? {
        if (nextMusicId == -1L) {
            return null
        }
        var songInfo: SongInfo? = null
        var cursor: Cursor? = null
        cursor = db.query(
            DatabaseHelper.MUSIC_TABLE, null, DatabaseHelper.ID_COLUMN + " = ?",
            arrayOf("" + nextMusicId), null, null, null)
        if (cursor.moveToFirst()) {
            val musicId = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.ID_COLUMN))
            val musicName =
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.MUSIC_NAME_COLUMN))
            val musicSinger =
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.MUSIC_SINGER_COLUMN))
            val musicAlbum =
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.MUSIC_ALBUM_COLUMN))
            val musicPath =
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.MUSIC_PATH_COLUMN))
            val musicParentPath =
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.MUSIC_PARENT_PATH_COLUMN))
            val musicFirstLetter =
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.MUSIC_FIRST_LETTER_COLUMN))
            val musicLove =
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.MUSIC_LOVE_COLUMN))
            val musicDuration =
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.MUSIC_DURATION_COLUMN))

                songInfo = SongInfo(
                musicId,
                musicName,
                musicSinger,
                musicDuration,
                musicAlbum,
                musicPath,
                musicParentPath,
                musicFirstLetter,
                musicLove
            )
        }
        return songInfo
    }

}



















