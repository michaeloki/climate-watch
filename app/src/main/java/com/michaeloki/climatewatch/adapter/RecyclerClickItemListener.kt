package com.michaeloki.climatewatch.adapter

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class RecyclerItemClickListener(context: Context, recyclerView: RecyclerView, listener:OnItemClickListener):RecyclerView.OnItemTouchListener {
    private val mListener:OnItemClickListener = listener


    var mGestureDetector: GestureDetector = GestureDetector(context, object:GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent):Boolean {
            return true
        }

        override fun onLongPress(e:MotionEvent) {
            val child = recyclerView.findChildViewUnder(e.x, e.y)
            if (child != null && mListener != null)
            {
                mListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child))
            }
        }
    })

    interface OnItemClickListener {
        fun onItemClick(view: View, position:Int)
        fun onLongItemClick(view:View, position:Int)
    }

    override fun onInterceptTouchEvent(view:RecyclerView, e:MotionEvent):Boolean {
        val childView = view.findChildViewUnder(e.x, e.y)
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e))
        {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView))
            return true
        }
        return false
    }
    override fun onTouchEvent(view:RecyclerView, motionEvent:MotionEvent) {}
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept:Boolean) {}
}