package com.wyq.ttmusicapp.utils

import android.R
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import com.wyq.ttmusicapp.common.MusicApplication
import java.io.*


/**
 * Created by Roman on 2021/1/22
 */
object MediaUtils {
    // 获取专辑封面的Uri
    private val albumArtUri: Uri = Uri
        .parse("content://media/external/audio/albumart")

    /**
     * 获取默认专辑图片
     *
     * @param context
     * @return
     */
    private fun getDefaultArtwork(context: Context, small: Boolean): Bitmap? {
        val opts = BitmapFactory.Options()
        opts.inPreferredConfig = Bitmap.Config.RGB_565
        return if (small) { // 返回小图片,文件路径为默认的图片
            BitmapFactory.decodeStream(
                context.resources
                    .openRawResource(+ R.drawable.ic_dialog_info), null, opts
            )
        } else BitmapFactory.decodeStream(
            context.resources
                .openRawResource(+ R.drawable.ic_dialog_info), null, opts
        )
    }

    /**
     * 从文件当中获取专辑封面位图
     *
     * @param context
     * @param songId
     * @param albumId
     * @return
     */
    private fun getArtworkFromFile(
        context: Context, songId: Long,
        albumId: Long
    ): Bitmap? {
        var bm: Bitmap? = null
        require(!(albumId < 0 && songId < 0)) { "Must specify an album or a song id" }
        try {
            val options = BitmapFactory.Options()
            var fd: FileDescriptor? = null
            if (albumId < 0) {
                val uri: Uri = Uri.parse(
                    "content://media/external/audio/media/"
                            + songId + "/albumart"
                )
                val pfd: ParcelFileDescriptor = context.contentResolver
                    .openFileDescriptor(uri, "r")!!
                if (pfd != null) {
                    fd = pfd.fileDescriptor
                }
            } else {
                val uri: Uri = ContentUris.withAppendedId(albumArtUri, albumId)
                val pfd: ParcelFileDescriptor = context.contentResolver
                    .openFileDescriptor(uri, "r")!!
                if (pfd != null) {
                    fd = pfd.fileDescriptor
                }
            }
            options.inSampleSize = 1
            // 只进行大小判断
            options.inJustDecodeBounds = true
            // 调用此方法得到options得到图片大小
            BitmapFactory.decodeFileDescriptor(fd, null, options)
            // 我们的目标是在800pixel的画面上显示
            // 所以需要调用computeSampleSize得到图片缩放的比例
            options.inSampleSize = 100
            // 我们得到了缩放的比例，现在开始正式读入Bitmap数据
            options.inJustDecodeBounds = false
            options.inDither = false
            options.inPreferredConfig = Bitmap.Config.ARGB_8888

            // 根据options参数，减少所需要的内存
            bm = BitmapFactory.decodeFileDescriptor(fd, null, options)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return bm
    }

    /**
     * 获取专辑封面位图对象
     *
     * @param context
     * @param song_id
     * @param album_id
     * @param allowDefalut
     * @return
     */
    fun getArtwork(
        context: Context, song_id: Long,
        album_id: Long, allowDefalut: Boolean, small: Boolean
    ): Bitmap? {
        if (album_id < 0) {
            if (song_id < 0) {
                val bm = getArtworkFromFile(context, song_id, -1)
                if (bm != null) {
                    return bm
                }
            }
            return if (allowDefalut) {
                getDefaultArtwork(context, small)
            } else null
        }
        val res: ContentResolver = context.contentResolver
        val uri: Uri? = ContentUris.withAppendedId(albumArtUri, album_id)
        if (uri != null) {
            var `in`: InputStream? = null
            return try {
                `in` = res.openInputStream(uri)
                val options = BitmapFactory.Options()
                // 先制定原始大小
                options.inSampleSize = 1
                // 只进行大小判断
                options.inJustDecodeBounds = true
                // 调用此方法得到options得到图片的大小
                BitmapFactory.decodeStream(`in`, null, options)
                /** 我们的目标是在你N pixel的画面上显示。 所以需要调用computeSampleSize得到图片缩放的比例  */
                /** 这里的target为800是根据默认专辑图片大小决定的，800只是测试数字但是试验后发现完美的结合  */
                if (small) {
                    options.inSampleSize = computeSampleSize(options, 40)
                } else {
                    options.inSampleSize = computeSampleSize(options, 600)
                }
                // 我们得到了缩放比例，现在开始正式读入Bitmap数据
                options.inJustDecodeBounds = false
                options.inDither = false
                options.inPreferredConfig = Bitmap.Config.ARGB_8888
                `in` = res.openInputStream(uri)
                BitmapFactory.decodeStream(`in`, null, options)
            } catch (e: FileNotFoundException) {
                var bm = getArtworkFromFile(context, song_id, album_id)
                if (bm != null) {
                    if (bm.config == null) {
                        bm = bm.copy(Bitmap.Config.RGB_565, false)
                        if (bm == null && allowDefalut) {
                            return getDefaultArtwork(context, small)
                        }
                    }
                } else if (allowDefalut) {
                    bm = getDefaultArtwork(context, small)
                }
                bm
            } finally {
                try {
                    `in`?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return null
    }

    /**
     * 对图片进行合适的缩放
     *
     * @param options
     * @param target
     * @return
     */
    private fun computeSampleSize(options: BitmapFactory.Options, target: Int): Int {
        val w: Int = options.outWidth
        val h: Int = options.outHeight
        val candidateW = w / target
        val candidateH = h / target
        var candidate = candidateW.coerceAtLeast(candidateH)
        if (candidate == 0) {
            return 1
        }
        if (candidate > 1) {
            if (w > target && w / candidate < target) {
                candidate -= 1
            }
        }
        if (candidate > 1) {
            if (h > target && h / candidate < target) {
                candidate -= 1
            }
        }
        return candidate
    }

    /**
     * @param inSampleSize 图片像素的 1/n*n
     */
    fun createBlurredImageFromBitmap(bitmap: Bitmap, inSampleSize: Int): Drawable {
        val rs = RenderScript.create(MusicApplication.context)
        val options = BitmapFactory.Options()
        options.inSampleSize = inSampleSize
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream)
        val imageInByte = stream.toByteArray()
        val bis = ByteArrayInputStream(imageInByte)
        val blurTemplate = BitmapFactory.decodeStream(bis, null, options)
        val input = Allocation.createFromBitmap(rs, blurTemplate)
        val output = Allocation.createTyped(rs, input.type)
        val script =
            ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        script.setRadius(20f)
        script.setInput(input)
        script.forEach(output)
        output.copyTo(blurTemplate)
        return BitmapDrawable(MusicApplication.context!!.resources, blurTemplate)
    }
}