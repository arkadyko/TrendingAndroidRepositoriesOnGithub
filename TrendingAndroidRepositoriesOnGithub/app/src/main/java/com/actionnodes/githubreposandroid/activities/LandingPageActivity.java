package com.actionnodes.githubreposandroid.activities;

/**
 * Created by Arkady Koplyarov on 06/02/18.
 * @author Arkady Koplyarov (arkad.k@gmail.com)
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.actionnodes.githubreposandroid.common.GithubReposAndroidApp;

import com.actionnodes.githubreposandroid.R;
import com.actionnodes.githubreposandroid.BuildConfig;

/**
 * An activity representing a landing page of the applicatioon.
 */
public class LandingPageActivity extends AppCompatActivity
{
	private FloatingActionButton open;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_landing_page);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		open = (FloatingActionButton) findViewById(R.id.open);
		open.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(LandingPageActivity.this, RepoListActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, RepoListRequestCode);
			}
		});

		SharedPreferences preferences = getSharedPreferences(GithubReposAndroidApp.NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE);
		boolean quitClickedInPrevSession = preferences.getBoolean(GithubReposAndroidApp.SP_QUIT_CLICKED_IN_PREV_SESSION, false);
		if(GithubReposAndroidApp.getInstance().wasAppOpenedBefore()  && !quitClickedInPrevSession) {
			Intent intent = new Intent(LandingPageActivity.this, RepoListActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(intent, RepoListRequestCode);
		}
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_landing_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {

			Snackbar.make(findViewById(R.id.landingPageActivityView),
					"The settings page is not implemented", Snackbar.LENGTH_LONG)
					.setAction("Action", null).show();

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	// If >= 0, this code will be returned in onActivityResult() when the activity exits.
	static public final int RepoListRequestCode = 333;

	//	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
		if (resultCode == Activity.RESULT_OK) {
			if (BuildConfig.BUILD_TYPE.contains(GithubReposAndroidApp.BUILD_TYPE_Debug)) {
				//Toast.makeText(this, "LandingPageActivity.onActivityResult", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
