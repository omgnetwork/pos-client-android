package network.omisego.omgwallet.custom

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 19/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import network.omisego.omgwallet.R
import network.omisego.omgwallet.extension.logi

class BalanceItemAnimator : DefaultItemAnimator() {
    // stateless interpolators re-used for every change animation
    private val accelerateInterpolator = AccelerateInterpolator()
    private val decelerateInterpolator = DecelerateInterpolator()

    // Maps to hold running animators. These are used when running a new change
    // animation on an item that is already being animated. mRunningAnimators is
    // used to cancel the previous animation. mAnimatorMap is used to construct
    // the new change animation based on where the previous one was at when it
    // was interrupted.
    private val animatorMap: MutableMap<RecyclerView.ViewHolder, AnimatorInfo> = mutableMapOf()

    // This allows our custom change animation on the contents of the holder instead
    // of the default behavior of replacing the viewHolder entirely
    override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder) = true

    override fun obtainHolderInfo(): ItemHolderInfo = BalanceInfo()

    private fun getItemHolderInfo(viewHolder: RecyclerView.ViewHolder, balanceInfo: BalanceInfo): BalanceInfo {
        val tvAmount = viewHolder.itemView.findViewById<TextView>(R.id.tvAmount)
        return balanceInfo.copy(text = tvAmount.text.toString())
    }

    override fun recordPreLayoutInformation(state: RecyclerView.State, viewHolder: RecyclerView.ViewHolder, changeFlags: Int, payloads: MutableList<Any>): ItemHolderInfo {
        val oldBalanceInfo = super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads) as BalanceInfo
        return getItemHolderInfo(viewHolder, oldBalanceInfo)
    }

    override fun recordPostLayoutInformation(state: RecyclerView.State, viewHolder: RecyclerView.ViewHolder): ItemHolderInfo {
        val newBalanceInfo = super.recordPostLayoutInformation(state, viewHolder) as BalanceInfo
        return getItemHolderInfo(viewHolder, newBalanceInfo)
    }

    override fun animateChange(oldHolder: RecyclerView.ViewHolder, newHolder: RecyclerView.ViewHolder, preInfo: ItemHolderInfo, postInfo: ItemHolderInfo): Boolean {
        if (oldHolder != newHolder) {
            // use default behavior if not re-using view holders
            return super.animateChange(oldHolder, newHolder, preInfo, postInfo)
        }

        val container = newHolder.itemView.findViewById<ViewGroup>(R.id.layoutBalance)
        val tvAmount = newHolder.itemView.findViewById<TextView>(R.id.tvAmount)

        val oldInfo = (preInfo as BalanceInfo)
        val newInfo = (postInfo as BalanceInfo)
        val oldText = oldInfo.text
        val newText = newInfo.text

        val runningInfo = animatorMap[newHolder]

        runningInfo?.changeSet?.cancel()

        val startPosition = 50f

        val animMoveIn = ObjectAnimator.ofFloat(tvAmount, View.TRANSLATION_Y, startPosition, 0f)
        val animMoveOut = ObjectAnimator.ofFloat(tvAmount, View.TRANSLATION_Y, 0f, -startPosition)
        val animFadeIn = ObjectAnimator.ofFloat(tvAmount, View.ALPHA, 0f, 1f)
        val animFadeOut = ObjectAnimator.ofFloat(tvAmount, View.ALPHA, 1f, 0f)

        animMoveIn.interpolator = decelerateInterpolator
        animMoveOut.interpolator = accelerateInterpolator
        animFadeIn.interpolator = decelerateInterpolator
        animFadeOut.interpolator = accelerateInterpolator

        animMoveOut.removeAllUpdateListeners()
        animMoveOut.addUpdateListener {
            logi("update ${it.animatedFraction}")
            if (it.animatedFraction == 1.0f) {
                tvAmount.text = newText
            } else if(it.animatedFraction == 0.0f) {
                tvAmount.text = oldText
            }
        }

        val moveSet = AnimatorSet().apply { playSequentially(animMoveOut, animMoveIn) }
        val fadeSet = AnimatorSet().apply { playSequentially(animFadeOut, animFadeIn) }
        val changeSet = AnimatorSet().apply { playTogether(fadeSet, moveSet) }

        changeSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                dispatchAnimationFinished(newHolder)
                animatorMap.remove(newHolder)
            }
        })
        changeSet.start()
        tvAmount.invalidate()

        // Store info about this animation to be re-used if a succeeding change event
        animatorMap[newHolder] = AnimatorInfo(changeSet, animFadeIn, animFadeOut, animMoveIn, animMoveOut)
        return true
    }

    override fun endAnimation(item: RecyclerView.ViewHolder) {
        super.endAnimation(item)
        animatorMap.filter { it.key == item }.forEach { it.value.changeSet.cancel() }
    }

    override fun isRunning(): Boolean {
        return super.isRunning() || !animatorMap.isEmpty()
    }

    override fun endAnimations() {
        super.endAnimations()
        animatorMap.forEach { it.value.changeSet.cancel() }
    }

    /**
     * Holds child animator objects for any change animation. Used when a new change
     * animation interrupts one already in progress; the new one is constructed to start
     * from where the previous one was at when the interruption occurred.
     */
    private data class AnimatorInfo(
        var changeSet: AnimatorSet,
        var animFadeIn: ObjectAnimator,
        var animFadeOut: ObjectAnimator,
        var animMoveIn: ObjectAnimator,
        var animMoveOut: ObjectAnimator
    )

    private data class BalanceInfo(val text: String = "") : ItemHolderInfo()
}