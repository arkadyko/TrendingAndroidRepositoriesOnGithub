package com.actionnodes.githubreposandroid.common;

/**
 * Created by Arkady Koplyarov on 06/02/18.
 * @author Arkady Koplyarov (arkad.k@gmail.com)
 */

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.actionnodes.githubreposandroid.models.GithubTrendingRepo;
import com.actionnodes.githubreposandroid.models.GithubTrendingRepoDeserializer;
import com.actionnodes.githubreposandroid.apis.GithubAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import io.reactivex.disposables.CompositeDisposable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * An application class representing this application.
 */
public class GithubReposAndroidApp extends Application
{
	public static final String NAME_SHARED_PREFERENCE = "manulife-challenge-app-shared-preferences-file-name";
	public static final String SP_APP_WAS_OPENED_BEFORE = "app-was-opened-before-shared-preferences-name";
	public static final String SP_QUIT_CLICKED_IN_PREV_SESSION = "quit-clicked-in-prev-session";

	static private GithubReposAndroidApp singleton;

	static public GithubReposAndroidApp getInstance() {
		return singleton;
	}

	public static final String FLAVOR_UseBackButton = "UseBackButton";
	public static final String FLAVOR_DontUseBackButton = "DontUseBackButton";

	public static final String BUILD_TYPE_Debug = "debug";
	public static final String BUILD_TYPE_Release = "release";

	private boolean appWasOpenedBefore;

	@Override
	public void onCreate() {
		super.onCreate();
		singleton = this;

		SharedPreferences preferences = getSharedPreferences(GithubReposAndroidApp.NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE);
		appWasOpenedBefore = preferences.getBoolean(GithubReposAndroidApp.SP_APP_WAS_OPENED_BEFORE, false);
		preferences.edit().putBoolean(GithubReposAndroidApp.SP_APP_WAS_OPENED_BEFORE, true).commit();

		createGithubAPI();
	}

	public boolean wasAppOpenedBefore() {
		return appWasOpenedBefore;
	}


	public void onTerminate() {
		if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
			compositeDisposable.dispose();
		}

		super.onTerminate();
	}


	public GithubAPI githubAPI;
	public CompositeDisposable compositeDisposable = new CompositeDisposable();

	private void createGithubAPI() {
		Gson gson = new GsonBuilder()
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
				.registerTypeAdapter(GithubTrendingRepo.class, new GithubTrendingRepoDeserializer())
				.create();

		OkHttpClient okHttpClient = new OkHttpClient.Builder()
				.addInterceptor(new Interceptor() {
					@Override
					public okhttp3.Response intercept(Chain chain) throws IOException {
						Request originalRequest = chain.request();

						Request.Builder builder = originalRequest.newBuilder();
						//builder.header("Authorization", Credentials.basic(username, password));

						Request newRequest = builder.build();
						return chain.proceed(newRequest);
					}
				}).build();

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(GithubAPI.ENDPOINT)
				.client(okHttpClient)
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create(gson))
				.build();

		githubAPI = retrofit.create(GithubAPI.class);
	}

}
