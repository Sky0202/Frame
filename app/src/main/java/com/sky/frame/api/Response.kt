package cn.com.tcsl.fengxing.fresh.api

import com.google.gson.annotations.SerializedName

data class Response<T>(
    @field:SerializedName("result")
    val result: Int,
    @field:SerializedName("errorCode")
    val errorCode: String,
    @field:SerializedName("msg")
    val message: String,
    @field:SerializedName("data")
    val data: T
) {
    fun isSuccess(): Boolean {
        return result == 1
    }
}