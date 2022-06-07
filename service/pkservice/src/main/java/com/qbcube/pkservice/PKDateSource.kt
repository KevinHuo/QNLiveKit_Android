package com.qbcube.pkservice

import com.nucube.http.OKHttpService
import com.qbcube.pkservice.mode.PKInfo
import com.qbcube.pkservice.mode.PKOutline
import org.json.JSONObject

class PKDateSource {

    suspend fun startPk(
        init_room_id: String,
        recv_room_id: String,
        recv_user_id: String
    ): PKOutline {
        val jsonObj = JSONObject()
        jsonObj.put("init_room_id", init_room_id)
        jsonObj.put("recv_room_id", recv_room_id)
        jsonObj.put("recv_user_id", recv_user_id)
        return OKHttpService.post("/client/relay/start", jsonObj.toString(), PKOutline::class.java)
    }

    suspend fun recevPk(relay_id: String): PKOutline {
        return OKHttpService.get("/client/relay/${relay_id}/token", null, PKOutline::class.java)
    }

    suspend fun stopPk(relay_id: String) {
        OKHttpService.post("/client/relay/${relay_id}/stop", "{}", Any::class.java)
    }

    suspend fun ackACKPk(relay_id: String) {
        OKHttpService.post("/client/relay/${relay_id}/started", "{}", Any::class.java)
    }

    suspend fun getPkInfo(relay_id: String): PKInfo {
        return OKHttpService.get("/client/relay/${relay_id}", null, PKInfo::class.java)
    }
}