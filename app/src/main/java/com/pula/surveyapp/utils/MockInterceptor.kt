package com.pula.surveyapp.utils

import com.pula.surveyapp.BuildConfig
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull

class MockInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (BuildConfig.DEBUG) {
            val uri = chain.request().url.toUri().toString()

            chain.request().body

            if (uri.endsWith("upload")){
                val responseString = getUploadSuccess

                return chain.proceed(chain.request())
                    .newBuilder()
                    .code(SUCCESS_CODE)
                    .protocol(Protocol.HTTP_2)
                    .message(responseString)
                    .body(
                        ResponseBody.create(
                            "application/json".toMediaTypeOrNull(),
                            responseString.toByteArray()))
                    .addHeader("content-type", "application/json")
                    .build()
            }

        } else {
            //just to be on safe side.
            throw IllegalAccessError("MockInterceptor is only meant for Testing Purposes and " +
                    "bound to be used only with DEBUG mode")
        }

        return chain.proceed(chain.request())
    }



}

const val SUCCESS_CODE=200

const val getUploadSuccess = """
[{
	"success": "uploaded successfully"
}]
"""