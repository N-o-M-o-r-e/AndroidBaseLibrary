package com.no_more.base.utils

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.no_more.base.R

/*
- `AUTOMATIC`: Lựa chọn an toàn cho hầu hết các trường hợp.
- `RESOURCE`: Nhanh nhất nếu ảnh luôn có kích thước cố định.
- `DATA`: Cân bằng giữa tốc độ và dung lượng khi ảnh có nhiều kích thước.
- `ALL`: Hiệu năng cao nhất nhưng tốn dung lượng nhất.
- `NONE`: Chỉ dùng trong các trường hợp đặc biệt.
 */

@SuppressLint("CheckResult")
fun ImageView.loadImageWithGlide(
    data: Any?,
    strategy: DiskCacheStrategy = DiskCacheStrategy.AUTOMATIC,
    enableCrossFade: Boolean = true,
    @DrawableRes placeholder: Int? = R.drawable.glide_place_holder,
    @DrawableRes errorDrawable: Int? = R.drawable.glide_error
) {

    val requestBuilder = Glide.with(context).load(data).diskCacheStrategy(strategy).apply {
            placeholder?.let { placeholder(it) }
            errorDrawable?.let { error(it) }
        }.listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?, model: Any?, target: Target<Drawable>, isFirstResource: Boolean
            ): Boolean {
                logE("__GLIDE", "❌ Load failed: ${e?.localizedMessage}")
                return false
            }

            override fun onResourceReady(
                resource: Drawable,
                model: Any,
                target: Target<Drawable>?,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                logD("__GLIDE", "✅ Load success from: $dataSource")
                return false
            }
        })

    if (enableCrossFade) {
        requestBuilder.transition(DrawableTransitionOptions.withCrossFade())
    } else {
        requestBuilder.dontAnimate()
    }

    requestBuilder.into(this)
}

/** Clear ImageView để tránh memory leak */
fun ImageView.clearImage() {
    Glide.with(context).clear(this)
}



