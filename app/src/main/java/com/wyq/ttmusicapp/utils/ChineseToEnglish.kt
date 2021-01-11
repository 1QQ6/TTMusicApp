package com.wyq.ttmusicapp.utils

import android.util.Log
import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination

object ChineseToEnglish {
    private const val TAG = "ChineseToEnglish"

    /**
     * 返回一个字的拼音
     */
    fun toPinYin(hanzi: Char): String? {
        val hanyuPinyin = HanyuPinyinOutputFormat()
        hanyuPinyin.caseType = HanyuPinyinCaseType.LOWERCASE
        hanyuPinyin.toneType = HanyuPinyinToneType.WITHOUT_TONE
        hanyuPinyin.vCharType = HanyuPinyinVCharType.WITH_U_UNICODE
        var pinyinArray: Array<String?>? = null
        try {
            //是否在汉字范围内
            if (hanzi.toInt() in 0x4e00..0x9fa5) {
                pinyinArray = PinyinHelper.toHanyuPinyinStringArray(hanzi, hanyuPinyin)
            }
        } catch (e: BadHanyuPinyinOutputFormatCombination) {
            e.printStackTrace()
            Log.e(TAG, "toPinYin: hanzi = $hanzi")
            Log.e(
                TAG,
                "toPinYin: pinyinArray.toString() = " + pinyinArray.toString()
            )
        }
        //将获取到的拼音返回
        return if (pinyinArray != null && pinyinArray.isNotEmpty()) {
            pinyinArray[0]
        } else {
            Log.e(TAG, "toPinYin: hanzi = $hanzi")
            "#"
        }
    }

    //字符串转换成拼音
    fun stringToPingYin(input: String?): String? {
        if (input == null) {
            return null
        }
        var result: String? = null
        for (i in input.indices) {
            //是否在汉字范围内
            if (input[i].toInt() in 0x4e00..0x9fa5) {
                result += toPinYin(input[i])
            } else {
                result += input[i]
            }
        }
        if (result!!.length > 4) {
            result = result.substring(4, result.length)
        }
        return result
    }

    /**
     * 汉字转拼音
     */
    fun stringToPinyinSpecial(input: String?): String? {
        if (input == null) {
            return null
        }
        var result: String? = null
        for (i in input.indices) {
            //是否在汉字范围内
            if (input[i].toInt() in 0x4e00..0x9fa5) {
                result += toPinYin(input[i])
            } else {
                result += input[i]
            }
        }
        if (result!!.length > 4) {
            result = result.substring(4, result.length)
        }
        //如果首字母不在[a,z]和[A,Z]内则首字母改为‘#’
        if (result.toUpperCase()[0] !in 'A'..'Z') {
            val builder = StringBuilder(result)
            builder.replace(0, 1, "#")
            result = builder.toString()
        }
        return result
    }
}