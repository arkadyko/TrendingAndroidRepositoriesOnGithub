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
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionnodes.githubreposandroid.BuildConfig;
import com.actionnodes.githubreposandroid.common.GithubReposAndroidApp;
import com.actionnodes.githubreposandroid.models.GithubTrendingRepo;
import com.actionnodes.githubreposandroid.models.GithubTrendingResponse;
import com.actionnodes.githubreposandroid.R;
import com.actionnodes.githubreposandroid.fragments.RepoDetailFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;

/**
 * An activity representing a list of repositories. This activity
 * has different presentations for small and large-size devices. On
 * small, the activity presents a list of items, which when touched,
 * lead to a {@link RepoDetailActivity} representing
 * item details. On lager devices, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RepoListActivity extends AppCompatActivity
{
	static final private String TAG = "RepoListActivity";

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
	 */
	private boolean mTwoPane;

	/**
	 * The application
	 */
	GithubReposAndroidApp mApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trending_repo_list);

		mApp = GithubReposAndroidApp.getInstance();

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitle(getTitle());

		FloatingActionButton quit = (FloatingActionButton) findViewById(R.id.quit);
		quit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Set the Quit-button-clicked flag in the persistent storage.
				SharedPreferences preferences = getSharedPreferences(GithubReposAndroidApp.NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE);
				preferences.edit().putBoolean(GithubReposAndroidApp.SP_QUIT_CLICKED_IN_PREV_SESSION, true).commit();

				setResultOK();
				finish();
			}
		});

		// Show the Up button in the action bar.
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		// Detect if using two-pane mode.
		if (findViewById(R.id.repo_detail_container) != null) {
			// The detail container view will be present only in the large-screen layouts (res/values-w500dp).
			// If this view is present, then the activity should be in two-pane mode.
			mTwoPane = true;
		}

		// Clean the Quit-button-clicked flag in the persistent storage.
		SharedPreferences preferences = getSharedPreferences(GithubReposAndroidApp.NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE);
		preferences.edit().putBoolean(GithubReposAndroidApp.SP_QUIT_CLICKED_IN_PREV_SESSION, false).commit();

		// Get and show repos.
		getTrendingKotlinRepos();
	}

	@Override
	public void onBackPressed() {
		if(BuildConfig.FLAVOR.equals(GithubReposAndroidApp.FLAVOR_UseBackButton)) {
			// Handle the Back button press as if user clicked the quit button.
			SharedPreferences preferences = getSharedPreferences(GithubReposAndroidApp.NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE);
			preferences.edit().putBoolean(GithubReposAndroidApp.SP_QUIT_CLICKED_IN_PREV_SESSION, true).commit();

			super.onBackPressed();
		}
		else //if (BuildConfig.FLAVOR.contains(GithubReposAndroidApp.FLAVOR_DontUseBackButton))
		{
			// Don't let user close the activity by the Back button
			// and so force the user to press the Quit button, to quit the activity.
			// To provide this behaviour, I override this method, not calling super:

			//super.onBackPressed();

			Toast.makeText(this, "To quit the activity, press the Quit button", Toast.LENGTH_LONG).show();
		}
	}

	private void setResultOK() {
		Intent resultIntent = new Intent();
		setResult(Activity.RESULT_OK, resultIntent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			// This ID represents the Home or Up button.

			if(BuildConfig.FLAVOR.equals(GithubReposAndroidApp.FLAVOR_UseBackButton)) {
				// Handle the Home or Up button click as if user clicked the quit button.
				SharedPreferences preferences = getSharedPreferences(GithubReposAndroidApp.NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE);
				preferences.edit().putBoolean(GithubReposAndroidApp.SP_QUIT_CLICKED_IN_PREV_SESSION, true).commit();

				navigateUpFromSameTask(this);
			}
			else //if (BuildConfig.FLAVOR.contains(GithubReposAndroidApp.FLAVOR_DontUseBackButton))
			{
				// Don't let user close the activity by the Home or Up button
				// and so force the user to press the Quit button, to quit the activity.
				// To provide this behaviour, I comment-out the following line:

				//navigateUpFromSameTask(this);

				Toast.makeText(this, "To quit the activity, press the Quit button", Toast.LENGTH_LONG).show();
			}

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<GithubTrendingRepo> items) {
		if(items != null) {
			recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(
					items
			));
		}
	}

	public class SimpleItemRecyclerViewAdapter
			extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

		private final List<GithubTrendingRepo> mValues;

		public SimpleItemRecyclerViewAdapter(List<GithubTrendingRepo> items) {
			mValues = items;
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.trending_repo_list_content, parent, false);
			return new ViewHolder(view);
		}

		@Override
		public void onBindViewHolder(final ViewHolder holder, int position) {
			holder.mItem = mValues.get(position);

			Picasso.with(RepoListActivity.this).load(holder.mItem.owner.avatarUrl).into(holder.mAvatarImageView);
			holder.mFullNameView.setText( mValues.get(position).fullName );
			holder.mLanguageView.setText( mValues.get(position).language );
			holder.mDescriptionView.setText( mValues.get(position).description );
			holder.mUrlView.setText( mValues.get(position).url );
			holder.mStargazersCountView.setText( Integer.toString(mValues.get(position).stargazersCount) );

			holder.mView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mTwoPane) {
						Bundle arguments = new Bundle();
						arguments.putString(RepoDetailFragment.ARG_REPO_OWNER_LOGIN, holder.mItem.owner.login);
						arguments.putString(RepoDetailFragment.ARG_REPO_NAME, holder.mItem.name);
						RepoDetailFragment fragment = new RepoDetailFragment();
						fragment.setArguments(arguments);
						getSupportFragmentManager().beginTransaction()
								.replace(R.id.repo_detail_container, fragment)
								.commit();
					} else {
						Context context = v.getContext();
						Intent intent = new Intent(context, RepoDetailActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra(RepoDetailFragment.ARG_REPO_OWNER_LOGIN, holder.mItem.owner.login);
						intent.putExtra(RepoDetailFragment.ARG_REPO_NAME, holder.mItem.name);
						context.startActivity(intent);
					}
				}
			});
		}

		@Override
		public int getItemCount() {
			return mValues.size();
		}

		public class ViewHolder extends RecyclerView.ViewHolder {
			public final View mView;

			public final ImageView mAvatarImageView;
			public final TextView mFullNameView;
			public final TextView mLanguageView;
			public final TextView mDescriptionView;
			public final TextView mUrlView;
			public final TextView mStargazersCountView;

			public GithubTrendingRepo mItem;

			public ViewHolder(View view) {
				super(view);
				mView = view;
				mAvatarImageView = (ImageView) view.findViewById(R.id.avatar);
				mFullNameView = (TextView) view.findViewById(R.id.full_name);
				mLanguageView = (TextView) view.findViewById(R.id.language);
				mDescriptionView = (TextView) view.findViewById(R.id.description);
				mUrlView = (TextView) view.findViewById(R.id.url);
				mStargazersCountView = (TextView) view.findViewById(R.id.stargazers_count);
			}

			@Override
			public String toString() {
				return  mFullNameView.getText() + ", " + mUrlView.getText();
			}
		}
	}

	/**
	 * Get and show trending Android Kotlin repositories in Github.
 	 */
	public void getTrendingKotlinRepos() {
		// Creating an URL like (it works OK in a browser):
		// https://api.github.com/search/repositories?q=created:>2018-02-04+language:Kotlin&sort=stars
		mApp.compositeDisposable.add(
			mApp.githubAPI.getTrendingKotlin(  "language:Kotlin", "stars")
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeWith(
						getGithubTrendingResponseObserver())
		);
	}

	private DisposableSingleObserver<GithubTrendingResponse> getGithubTrendingResponseObserver() {
		return new DisposableSingleObserver<GithubTrendingResponse>() {
			@Override
			public void onSuccess(GithubTrendingResponse value) {
				// Show the list of repos.
				View recyclerView = findViewById(R.id.repo_list);
				assert recyclerView != null;
				setupRecyclerView((RecyclerView) recyclerView, value.items);
			}

			@Override
			public void onError(Throwable e) {
				e.printStackTrace();
				Toast.makeText(RepoListActivity.this, "Can not load repositories", Toast.LENGTH_SHORT).show();
			}
		};
	}

}




















