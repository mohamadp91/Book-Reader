package com.example.bookreader.di

import android.content.Context
import androidx.room.Room
import com.example.bookreader.BuildConfig
import com.example.bookreader.data.db.ReaderDb
import com.example.bookreader.data.db.UserDao
import com.example.bookreader.repository.UserRepository
import com.example.bookreader.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient =
        createSupabaseClient(Constants.SUPABASE_URL, BuildConfig.SUPABASE_KEY) {
            install(GoTrue)
            install(Postgrest)
        }

    @Provides
    @Singleton
    fun provideGoTrueClient(client: SupabaseClient): GoTrue = client.gotrue

    @Provides
    @Singleton
    fun providePostgrestClient(client: SupabaseClient): Postgrest = client.postgrest

    @Provides
    @Singleton
    fun provideUserRepository(
        goTrue: GoTrue,
        postgrest: Postgrest,
        userDao: UserDao
    ): UserRepository =
        UserRepository(goTrue, postgrest,userDao)

    @Provides
    @Singleton
    fun provideReaderDb(@ApplicationContext context: Context): ReaderDb =
        Room.databaseBuilder(
            context,
            ReaderDb::class.java,
            "weather_db"
        ).build()

    @Provides
    @Singleton
    fun provideUserDao(readerDb: ReaderDb) = readerDb.userDao()
}