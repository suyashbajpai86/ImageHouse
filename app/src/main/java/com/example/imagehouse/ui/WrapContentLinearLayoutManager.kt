package com.example.imagehouse.ui

import android.content.Context
import android.graphics.PointF
import android.view.InflateException
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

class WrapContentLinearLayoutManager @JvmOverloads constructor(
    context: Context?,
    orientation: Int,
    reverseLayout: Boolean,
    private val extraLayoutSpace: Int = INVALID_EXTRA_SPACE // !! This could be costly as it increases LayoutManager's pre-caching limit
) : LinearLayoutManager(context, orientation, reverseLayout) {

    companion object{
        const val INVALID_EXTRA_SPACE = -1
    }

    /**
     * Disable predictive animations. There is a bug in RecyclerView which causes views that
     * are being reloaded to pull invalid ViewHolders from the internal recycler stack if the
     * adapter size has decreased since the ViewHolder was recycled.
     */
    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        } catch (e: InflateException) {
            e.printStackTrace()
        }
    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State,
                                        position: Int) {
        val smoothScroller: RecyclerView.SmoothScroller = TopSnappedSmoothScroller(recyclerView.context)
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }

    private inner class TopSnappedSmoothScroller(context: Context) : LinearSmoothScroller(context) {
        override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
            return this@WrapContentLinearLayoutManager
                .computeScrollVectorForPosition(targetPosition)
        }

        override fun getVerticalSnapPreference(): Int {
            return SNAP_TO_START
        }
    }

    /**
     * See LinearLayoutManager.calculateExtraLayoutSpace() for more in formation
     * This is acceptable in REI as we are caching the ViewHolders and this does not cause significant
     * performance overhead.
     */
    override fun calculateExtraLayoutSpace(state: RecyclerView.State, extraLayoutSpace: IntArray) {
        if(this.extraLayoutSpace == INVALID_EXTRA_SPACE){
            super.calculateExtraLayoutSpace(state, extraLayoutSpace)
        }else{
            extraLayoutSpace[0] = 0
            extraLayoutSpace[1] = this.extraLayoutSpace
        }
    }
}