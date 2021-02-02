package com.wyq.ttmusicapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Audio.AlbumColumns.ALBUM_ART
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.wyq.ttmusicapp.GlideApp
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.entity.SongInfo


/**
 * 专辑封面图片加载器
 * Glide加载异常处理*/



object CoverLoader {
    private const val TAG = "CoverLoader"
    private const val coverUriByRandom: Int = R.drawable.default_cover_place

    /**
     * 根据专辑id返回专辑封面的uri
     */
    fun getCoverUri(context: Context, albumId: String): String? {
        if (albumId == "-1") {
            return null
        }
        var uri: String? = null
        try {
            val cursor = context.contentResolver.query(
                    Uri.parse("content://media/external/audio/albums/$albumId"),
                    arrayOf(ALBUM_ART), null, null, null)
            if (cursor != null) {
                cursor.moveToNext()
                uri = cursor.getString(0)
                cursor.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return uri
    }

/**
     * 获取专辑图url，
     *
     * @param music 音乐
     * @param isBig 是否是大图
     * @return*/



    private fun getCoverUriByMusic(music: SongInfo, isBig: Boolean): String? {
        return music.coverUrl
    }


/**
     * 显示小图
     *
     * @param mContext
     * @param music
     * @param callBack*/



    fun loadImageViewByMusic(mContext: Context, music: SongInfo?, callBack: ((Bitmap) -> Unit)?) {
        if (music == null) return
        val url = getCoverUriByMusic(music, false)
        loadBitmap(mContext, url, callBack)
    }

/*
*
     * 显示播放页大图
     *
     * @param mContext
*/



/*    fun loadBigImageView(mContext: Context?, music: SongInfo?, callBack: ((Bitmap) -> Unit)?) {
        if (music == null) return
        if (mContext == null) return
        val url = MusicUtils.getAlbumPic(music.coverUri, music.type, MusicUtils.PIC_SIZE_BIG)
        GlideApp.with(mContext)
                .asBitmap()
                .load(url ?: R.drawable.default_cover_place)
                .error(coverUriByRandom)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into<CustomTarget<Bitmap>>(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        if (callBack != null && resource != null) {
                            callBack.invoke(resource)
                        }
                    }
                })
    }

    fun loadBigImageView(mContext: Context, music: SongInfo?, imageView: ImageView?) {
        if (music == null || imageView == null) return
        val url = getCoverUriByMusic(music, true)
        GlideApp.with(mContext)
                .asBitmap()
                .load(url ?: R.drawable.default_cover_place)
                .error(coverUriByRandom)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
    }

    fun loadBigImageView(mContext: Context, url: String?, vendor: String?, imageView: ImageView?) {
        if (imageView == null) return
        val newUrl = MediaUtils.getAlbumPic(url, vendor, MusicUtils.PIC_SIZE_BIG)
        GlideApp.with(mContext)
                .asBitmap()
                .load(newUrl)
                .error(coverUriByRandom)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
    }*/

/**
     * 显示图片
     *
     * @param mContext
     * @param url
     * @param imageView*/


    fun loadImageView(mContext: Context?, url: String?, imageView: ImageView?) {
        if (mContext == null) return
        if (imageView == null) return
        GlideApp.with(mContext)
                .load(url)
                .placeholder(R.drawable.default_cover_place)
                .error(R.drawable.default_cover_place)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
    }

    fun loadImageView(mContext: Context?, url: String?, defaultUrl: Int, imageView: ImageView) {
        if (mContext == null) return
        GlideApp.with(mContext)
                .load(url)
                .placeholder(defaultUrl)
                .error(defaultUrl)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
    }
/*
*
     * 根据id显示
     *
     * @param mContext
     * @param albumId
     * @param callBack*/


    fun loadBitmapById(mContext: Context, albumId: String, callBack: ((Bitmap) -> Unit)?) {
        loadBitmap(mContext, getCoverUri(mContext, albumId), callBack)
    }

/*
*
     * 返回bitmap
     *
     * @param mContext
     * @param url
     * @param callBack
*/


    fun loadBitmap(mContext: Context?, url: String?, callBack: ((Bitmap) -> Unit)?) {
        if (mContext == null) return
        GlideApp.with(mContext)
                .asBitmap()
                .load(url ?: coverUriByRandom)
                .error(coverUriByRandom)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into<CustomTarget<Bitmap>>(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        callBack?.invoke(resource)
                    }
                })
    }

    fun loadBitmap(mContext: Context?, url: String?,withoutUrlCoverImage:Int, callBack: ((Bitmap) -> Unit)?) {
        if (mContext == null) return
        GlideApp.with(mContext)
            .asBitmap()
            .load(url ?: withoutUrlCoverImage)
            .error(withoutUrlCoverImage)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into<CustomTarget<Bitmap>>(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    callBack?.invoke(resource)
                }
            })
    }

/**
     * 返回Drawable
     *
     * @param mContext
     * @param url
     * @param callBack*/


    fun loadDrawable(mContext: Context?, url: String?, callBack: ((Drawable) -> Unit)?) {
        if (mContext == null) return
        GlideApp.with(mContext)
                .asBitmap()
                .load(url ?: coverUriByRandom)
                .error(coverUriByRandom)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into<CustomTarget<Bitmap>>(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        callBack?.invoke(BitmapDrawable(mContext.resources, resource))
                    }
                })
    }

    fun createBlurredImageFromBitmap(bitmap: Bitmap?): Drawable {
        return MediaUtils.createBlurredImageFromBitmap(bitmap!!, 4)
    }

}
