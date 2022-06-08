package com.qncube.uikitlinkmic

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qncube.linkmicservice.QNMicLinker
import kotlinx.android.synthetic.main.kit_item_linker.view.*

class LinkerAdapter() :
    BaseQuickAdapter<QNMicLinker, BaseViewHolder>(R.layout.kit_item_linker, ArrayList()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val helper = super.onCreateViewHolder(parent, viewType)
        val tempLp: LinearLayout.LayoutParams =
            helper.itemView.tempView.layoutParams as LinearLayout.LayoutParams
        tempLp.height = LinkerUIHelper.micBottomUIMargin
        helper.itemView.tempView.layoutParams = tempLp

        val sfLp = helper.itemView.tempSurfaceContainer.layoutParams
        sfLp.width = LinkerUIHelper.uiMicWidth
        sfLp.height = LinkerUIHelper.uiMicHeight
        helper.itemView.tempSurfaceContainer.layoutParams = sfLp

//        val llLp: FrameLayout.LayoutParams =
//            helper.itemView.llContiner.layoutParams as FrameLayout.LayoutParams
//        llLp.marginEnd = LinkerUIHelper.micRightUIMargin
//        helper.itemView.llContiner.layoutParams = llLp
        return helper
    }

    override fun convert(helper: BaseViewHolder, item: QNMicLinker) {

        if (!item.isOpenCamera) {
            //没有摄像头
            helper.itemView.ivMicStatusOut.visibility = View.INVISIBLE
            helper.itemView.ivMicStatusInner.visibility = View.VISIBLE
            helper.itemView.ivAvatarInner.visibility = View.VISIBLE

        } else {
            //开摄像头
            helper.itemView.ivMicStatusOut.visibility = View.VISIBLE
            helper.itemView.ivMicStatusInner.visibility = View.INVISIBLE
            helper.itemView.ivAvatarInner.visibility = View.INVISIBLE
        }

        Glide.with(mContext).load(item.user.avatar)
            .into(helper.itemView.ivAvatarInner)
        helper.itemView.ivMicStatusInner.isSelected = item.isOpenMicrophone
        helper.itemView.ivMicStatusOut.isSelected = item.isOpenMicrophone
    }

}




