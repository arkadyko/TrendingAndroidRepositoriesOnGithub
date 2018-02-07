package com.actionnodes.githubreposandroid.activities;

/**
 * Created by Arkady Koplyarov on 06/02/18.
 * @author Arkady Koplyarov (arkad.k@gmail.com)
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.actionnodes.githubreposandroid.fragments.RepoDetailFragment;

import com.actionnodes.githubreposandroid.R;

/**
 * Created by Arkady Koplyarov on 06/02/18.
 * @author Arkady Koplyarov (arkad.k@gmail.com)
 *
 * An activity representing a single detail screen. This activity is only used small-size devices.
 * On large-size devices, item details are presented side-by-side with a list of items
 * in a {@link RepoListActivity}.
 */
public class RepoDetailActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_repo_detail);
		Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
		setSupportActionBar(toolbar);


		// Show the Up button in the action bar.
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(RepoDetailFragment.ARG_REPO_OWNER_LOGIN,
					getIntent().getStringExtra(RepoDetailFragment.ARG_REPO_OWNER_LOGIN));
			arguments.putString(RepoDetailFragment.ARG_REPO_NAME,
					getIntent().getStringExtra(RepoDetailFragment.ARG_REPO_NAME));
			RepoDetailFragment fragment = new RepoDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.repo_detail_container, fragment)
					.commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			// This ID represents the Home or Up button.
			navigateUpTo(new Intent(this, RepoListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
