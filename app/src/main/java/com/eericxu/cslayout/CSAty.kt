package com.eericxu.cslayout

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.eericxu.cslibrary.ScaleViewGesture
import com.eericxu.cslibrary.createAnimator
import com.eericxu.cslibrary.finishShareAnim
import com.eericxu.cslibrary.keyparms.KeyParams
import com.eericxu.cslibrary.startShareAnim
import kotlinx.android.synthetic.main.aty_cs.*

class CSAty : BaseAty() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.aty_cs)
        csLayout.visibility = View.INVISIBLE
        val parm = intent.getParcelableExtra<KeyParams>("imgView")
        val params = iv_cover.layoutParams
        val p = Point()
        window.windowManager.defaultDisplay.getSize(p)
        params.height = (p.x * (parm.rect.height() * 1f / parm.rect.width())).toInt()
        iv_cover.layoutParams = params
        Glide.with(this)
                .load(intent.getIntExtra("img", R.mipmap.img_1))
                .into(iv_cover)

        ScaleViewGesture(this).bindToView(iv_cover, csLayout)?.onClick = {
            Toast.makeText(it.context, "Click", Toast.LENGTH_SHORT).show()
        }

        val builder = StringBuilder()
        for (i in 0..100) {
            builder.append("以敦煌为圆心的东北东\n")
        }
        tv_content.text = builder.toString()
    }

    var anim: Animator? = null
    //父类中重写onWindowFocusChanged 当window第一次获取焦点时执行
    override fun onFirstFocus() {
        val animator = createAnimator(true, intent, "imgView", iv_cover)
        (animator as ValueAnimator).addUpdateListener {
            tv_content.translationY = iv_cover.translationY * 0.6f
            tv_content.translationX = iv_cover.translationX
        }
        csLayout.visibility = View.VISIBLE
        anim = startShareAnim(
                csLayout,
                createAnimator(true, intent, "tvTit", tv_title),
                animator
        )
    }


    override fun finish() {
        if (anim != null && anim?.isRunning == true)
            return
        val animator = createAnimator(false, intent, "imgView", iv_cover)
        (animator as ValueAnimator).addUpdateListener {
            tv_content.translationY = iv_cover.translationY * 0.6f
            tv_content.translationX = iv_cover.translationX
        }
        finishShareAnim(
                csLayout,
                createAnimator(false, intent, "tvTit", tv_title),
                animator,
                onAnimEnd = {
                    superFinish()
                })
    }

    private fun superFinish() {
        super.finish()
        overridePendingTransition(0, R.anim.exit_fade)
    }
}