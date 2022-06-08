package com.qncube.liveuikit

import android.content.Context
import com.qncube.liveroomcore.QNLiveCallBack
import com.qncube.liveroomcore.QNLiveRoomEngine
import com.qncube.liveroomcore.datasource.RoomDataSource
import com.qncube.liveroomcore.getCode
import com.qncube.liveroomcore.mode.QNLiveRoomInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


internal object QNLiveRoomUIKitInner {

    fun joinRoom(
        context: Context,
        liveRoomId: String,
        callBack: QNLiveCallBack<QNLiveRoomInfo>?
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val info = RoomDataSource().refreshRoomInfo(liveRoomId)
                if (info.anchorInfo.userId == QNLiveRoomEngine.getCurrentUserInfo().userId) {
                    RoomPushActivity.start(context, liveRoomId, callBack)
                } else {
                    RoomPullActivity.start(context, liveRoomId, callBack)
                }
            } catch (e: Exception) {
                callBack?.onError(e.getCode(), e.message)
                e.printStackTrace()
            }
        }
      //  RoomPullActivity.start(context, liveRoomId, callBack)
    }

    fun createAndJoinRoom(context: Context, callBack: QNLiveCallBack<QNLiveRoomInfo>?) {
        RoomPushActivity.start(context, callBack)
    }
}