package com.sky.frame.api

import android.system.ErrnoException
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.MalformedJsonException
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.regex.Pattern

/**
 * Common class used by API responses.
 * @param <T> the type of the response object
</T> */
@Suppress("unused") // T is used in extending classes
sealed class ApiResponse<T> {
    companion object {
        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return ApiErrorResponse(getMessage(error).toString())
        }

        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204) {
                    ApiEmptyResponse()
                } else {
                    ApiSuccessResponse(body, response.headers().get("link"))
                }
            } else {
                val msg = response.errorBody()?.string()
                val errorMsg = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                ApiErrorResponse(errorMsg ?: "未知错误，请联系管理员或稍后重试")
            }
        }
    }
}

/**
 * separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null.
 */
class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiSuccessResponse<T>(
    val body: T,
    val links: Map<String, String>
) : ApiResponse<T>() {
    constructor(body: T, linkHeader: String?) : this(
        body = body,
        links = linkHeader?.extractLinks() ?: emptyMap()
    )

    val nextPage: Int? by lazy(LazyThreadSafetyMode.NONE) {
        links[NEXT_LINK]?.let { next ->
            val matcher = PAGE_PATTERN.matcher(next)
            if (!matcher.find() || matcher.groupCount() != 1) {
                null
            } else {
                try {
                    Integer.parseInt(matcher.group(1)!!)
                } catch (ex: NumberFormatException) {
                    null
                }
            }
        }
    }

    companion object {
        private val LINK_PATTERN = Pattern.compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"")
        private val PAGE_PATTERN = Pattern.compile("\\bpage=(\\d+)")
        private const val NEXT_LINK = "next"

        private fun String.extractLinks(): Map<String, String> {
            val links = mutableMapOf<String, String>()
            val matcher = LINK_PATTERN.matcher(this)

            while (matcher.find()) {
                val count = matcher.groupCount()
                if (count == 2) {
                    links[matcher.group(2)!!] = matcher.group(1)!!
                }
            }
            return links
        }

    }
}

data class ApiErrorResponse<T>(val errorMessage: String) : ApiResponse<T>()

fun getMessage(t: Throwable): String? {
    var message: String? = null
    if (t is HttpException) {
        val response = t.response()
        val code = response!!.code()
        if (code == 404) {
            message = "404 无法提供服务，请检查服务是否正常"
        } else if (code == 403) {
            message = "403 服务资源无法访问，请检查服务是否正确"
        }
    } else if (t is MalformedJsonException) {
        // 格式不正确，可能是错误的地址或服务异常，返回了非标准的 json 数据
        message = "无效的请求结果，请检查服务地址是否填写正确或服务是否正常"
    } else if (t is SocketTimeoutException) {
        message = "网络请求超时，请检查网络或服务是否正常"
    } else if (t is ConnectException) {
        message = "网络错误，请检查当前网络能否正常上网"
    } else if (t is SocketException) {
        message = "网络请求中断，请检查网络或稍后重试"
    } else if (t is ErrnoException) {
        if (t.message!!.contains("Connection reset by peer")) {
            message = "网络请求中断，请检查网络或稍后重试"
        }
    } else if (t is JsonSyntaxException) {
        message = "服务异常，请检查服务是否正常"
    } else if (t is UnknownHostException) {
        message = "服务地址不正确，请检查服务地址"
    } else if (t is IllegalArgumentException) {
        message = "无效的服务地址，请检查"
    } else if (t is NullPointerException) {
        message = "未知错误，请联系管理员或稍后重试"
    } else {
        message = "未知错误，请联系管理员或稍后重试"
    }
    return message
}
