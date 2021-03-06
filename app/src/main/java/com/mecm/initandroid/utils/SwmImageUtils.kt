package com.mecm.initandroid.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.mecm.initandroid.R
import com.mecm.moneybag.utils.SwmFileUtil
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException


object SwmImageUtils {
    /**
     * 打开手机相册
     * @return
     */
    fun openImage(activity: Activity, maxSelectNum: Int, isCamear: Boolean) {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.picture_default_style)
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(3)// 每行显示个数
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .isCamera(isCamear)
                .enableCrop(true)// 是否裁剪 true or false
                .circleDimmedLayer(true)// 是否圆形裁剪 true or false
                .previewVideo(true)// 是否可预览视频
                .compress(true)// 是否压缩
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(PictureConfig.CHOOSE_REQUEST)//结果回调onActivityResult code
    }

    /**
     * 打开手机相册
     * @return
     */
    fun openImageNoCrop(activity: Activity, maxSelectNum: Int, isCamear: Boolean) {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.picture_default_style)
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(3)// 每行显示个数
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .isCamera(isCamear)
                .enableCrop(false)// 是否裁剪 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .previewVideo(true)// 是否可预览视频
                .compress(true)// 是否压缩
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(PictureConfig.CHOOSE_REQUEST)//结果回调onActivityResult code
    }

    // 打开照相机
    fun openCamera(activity: Activity, id: String) {
        val path = activity.filesDir.path + "/chatrecord" + "/" + id
        SwmFileUtil.createDirBy(activity.filesDir.path)
        SwmFileUtil.createDirBy(activity.filesDir.path + "/chatrecord")
        SwmFileUtil.createDirBy(activity.filesDir.path + "/chatrecord" + "/" + id)

        PictureSelector.create(activity)
                .openCamera(PictureMimeType.ofImage())
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .setOutputCameraPath(path)// 自定义拍照保存路径,可不填
                .compress(true)// 是否压缩 true or false
                .compressSavePath(path)//压缩图片保存地址
                .forResult(PictureConfig.CHOOSE_REQUEST)
    }

    /**
     * 打开手机相册
     * @return
     */
    fun openImageForChat(activity: Activity) {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.picture_default_style)
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(3)// 每行显示个数
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .isCamera(false)
                .enableCrop(false)// 是否裁剪 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .compress(true)// 是否压缩
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(PictureConfig.CHOOSE_REQUEST)//结果回调onActivityResult code
    }


    fun bitmapToBlob(bmp: Bitmap): ByteArray? {
        val os = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, os)
        return os.toByteArray()
    }

    /**
     * 此操作是异步。 mFile永远为空，可以复制到相应代码处操作
     * @param urlpath
     * @return File
     * 根据图片url获取图片对象
     */
    fun getFileByUrl(context: Context, urlpath: String) {
        try {
            Glide.with(context).asFile().load(urlpath).into(object : SimpleTarget<File>() {
                override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                    // 压缩文件
                    Luban.with(context)
                            .load(resource)
                            .ignoreBy(100)
                            .setCompressListener(object : OnCompressListener {
                                override fun onStart() {
                                }

                                override fun onSuccess(file: File) {
                                    SwmLogUtils.e("file.path= " + file.path)
//                                    DbUtils.updateMsgProImage(context, msgID, file.path)
                                }

                                override fun onError(e: Throwable) {
                                }
                            }).launch()
                }
            })
        } catch (e: IOException) {
            e.printStackTrace()
            SwmLogUtils.e("getFileByUrl: = " + e.message)
        }
    }

}
