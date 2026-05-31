package com.lazysyntax.nutron.data.remote

expect val BASE_HOST: String

object NetworkConstants {
   /*
    val AUTH_BASE_URL = "http://$BASE_HOST:8081/api/v1"
    val DATA_BASE_URL = "http://$BASE_HOST:8082/api/v1"
    */

    val AUTH_BASE_URL = "http://auth.nutron.local:8081/api/v1"
    val DATA_BASE_URL = "http://nutrition.nutron.local:8082/api/v1"
}