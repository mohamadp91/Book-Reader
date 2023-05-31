package com.example.bookreader.di

import com.example.bookreader.BuildConfig
import com.example.bookreader.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.gotrue
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGoTrueClient() : GoTrue =
        createSupabaseClient(Constants.SUPABASE_URL,BuildConfig.SUPABASE_KEY){
            install(GoTrue)
        }.gotrue

}