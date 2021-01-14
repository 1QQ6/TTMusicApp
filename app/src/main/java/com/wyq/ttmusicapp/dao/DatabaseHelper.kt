
package com.wyq.ttmusicapp.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by Roman on 2021/1/11
 */
class DatabaseHelper(
    context: Context?
) : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {

    companion object{
        private val TAG = DatabaseHelper::class.java.name
        //数据库名称
        const val DATABASE_NAME ="MusicDatabase.db"
        //歌曲表
        const val MUSIC_TABLE = "music_table"
        //音乐相关信息
         const val ID_COLUMN = "id"
         const val MUSIC_ID_COLUMN = "music_id"
         const val MUSIC_NAME_COLUMN ="music_name"
         const val MUSIC_SINGER_COLUMN = "music_singer"
         const val MUSIC_DURATION_COLUMN = "music_duration"
         const val MUSIC_ALBUM_COLUMN = "music_album"
         const val MUSIC_PATH_COLUMN = "music_path"
         const val MUSIC_PARENT_PATH_COLUMN ="music_parent_path"
         const val MUSIC_FIRST_LETTER_COLUMN ="music_first_letter"
         const val MUSIC_LOVE_COLUMN = "music_love"
        //最近播放表
        const val RECENT_PLAY_TABLE = "recent_play_table"
        //歌单表
        const val MUSIC_LIST_TABLE = "music_list_table"
        //歌单-歌曲-表 (多对多)
        const val MUSIC_MUSIC_LIST_TABLE = "music_music_list_table"
        //数据库版本号
        const val VERSION = 2
    }


    /**
     * 创建音乐表语句，注意每个数据类型前面有一个空格
     */
    private val createMusicTable = ("create table if not exists "
            + MUSIC_TABLE + "("
            + ID_COLUMN + " integer PRIMARY KEY ,"
            + MUSIC_NAME_COLUMN + " text,"
            + MUSIC_SINGER_COLUMN + " text,"
            + MUSIC_ALBUM_COLUMN + " text,"
            + MUSIC_DURATION_COLUMN + " long,"
            + MUSIC_PATH_COLUMN + " text,"
            + MUSIC_PARENT_PATH_COLUMN + " text,"
            + MUSIC_LOVE_COLUMN + " integer,"
            + MUSIC_FIRST_LETTER_COLUMN + " text);")

    /**
     * 创建最近播放表
     */
    private val createRecentPlayTable = ("create table if not exists "
            + RECENT_PLAY_TABLE + "("
            + ID_COLUMN + " INTEGER,"
            +"FOREIGN KEY(id) REFERENCES " + MUSIC_TABLE +" (id) ON DELETE CASCADE);"
            )

    /**
     *创建歌单表
     */
    private val createMusicListTable = ("create table if not exists "
            + MUSIC_LIST_TABLE +"("
            + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + MUSIC_NAME_COLUMN + " TEXT);"
            )

    /**
     * 创建歌单-歌曲表（多对多）
     */
    private val createMusicMusicListTable = ("create table if not exists "
            + MUSIC_MUSIC_LIST_TABLE + "("
            + ID_COLUMN + " INTEGER,"
            + MUSIC_ID_COLUMN + " INTEGER,"
            + "FOREIGN KEY(music_id) REFERENCES " + MUSIC_TABLE +"(id) ON DELETE CASCADE,"
            + "FOREIGN KEY(id) REFERENCES " + MUSIC_LIST_TABLE +"(id) ON DELETE CASCADE);")


    override fun onCreate(db: SQLiteDatabase?) {
        db!!.run {
            execSQL(createMusicTable)
            execSQL(createRecentPlayTable)
            execSQL(createMusicListTable)
            execSQL(createMusicMusicListTable)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion< VERSION){
            db!!.run {
                execSQL("  drop table if exists $MUSIC_TABLE")
                execSQL("  drop table if exists $RECENT_PLAY_TABLE")
                execSQL("  drop table if exists $MUSIC_LIST_TABLE")
                execSQL("  drop table if exists $MUSIC_MUSIC_LIST_TABLE")
                onCreate(this)
            }
        }
    }
}