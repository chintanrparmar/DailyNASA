package com.crp.dailynasa.di

import com.crp.dailynasa.BuildConfig
import com.crp.dailynasa.data.api.NasaAPI
import com.crp.dailynasa.utils.NetworkHelper
import okhttp3.OkHttpClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

    single(named("BASE_URL")) {
        "https://api.nasa.gov/planetary/"
    }

    single {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        interceptor
    }

    single {
        val client = OkHttpClient().newBuilder()
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            client.addInterceptor(get<HttpLoggingInterceptor>())
        }
        client.build()
    }

    single {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    single {
        Retrofit.Builder().baseUrl(get<String>(named("BASE_URL")))
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .client(get())
            .build()
    }

    single {
        get<Retrofit>().create(NasaAPI::class.java)
    }

    single {
        NetworkHelper(androidContext())
    }
}