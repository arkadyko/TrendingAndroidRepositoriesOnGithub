package com.actionnodes.githubreposandroid.fragments;

/**
 * Created by Arkady Koplyarov on 06/02/18.
 * @author Arkady Koplyarov (arkad.k@gmail.com)
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.actionnodes.githubreposandroid.models.GithubIssue;
import com.actionnodes.githubreposandroid.common.GithubReposAndroidApp;
import com.actionnodes.githubreposandroid.R;
import com.actionnodes.githubreposandroid.activities.RepoDetailActivity;
import com.actionnodes.githubreposandroid.activities.RepoListActivity;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * A fragment representing a single detail screen.
 * This fragment is either contained in a {@link RepoListActivity}
 * in two-pane mode (on tablets) or a {@link RepoDetailActivity}
 * on handsets.
 */

public class RepoDetailFragment extends Fragment
{
	static final private String TAG = "RepoDetailFragment";

	/**
	 * The fragment arguments representing the item IDs that this fragment represents.
	 */
	public static final String ARG_REPO_OWNER_LOGIN = "repoOwnerLogin";
	public static final String ARG_REPO_NAME = "repoName";

	/**
	 * The content this fragment is presenting.
	 */
	private String repoOwnerLogin;
	private String repoName;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public RepoDetailFragment() {
	}

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
	 */
	private boolean mTwoPane;

	/**
	 * The application
	 */
	GithubReposAndroidApp mApp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mApp = GithubReposAndroidApp.getInstance();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// It's needed for the list/detail view in landscape orientation.
		View rootView = inflater.inflate(R.layout.repo_detail_list, container, false);

		return rootView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (getArguments().containsKey(ARG_REPO_OWNER_LOGIN) &&
				getArguments().containsKey(ARG_REPO_NAME))
		{

			// Show the title of the repo.
			// Load the dummy content specified by the fragment arguments.
			// (In a real-world scenario, use a Loader to load content from a content provider.)
			repoOwnerLogin = getArguments().getString(ARG_REPO_OWNER_LOGIN);
			repoName = getArguments().getString(ARG_REPO_NAME);

			CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
			if (appBarLayout != null) {
				appBarLayout.setTitle(repoOwnerLogin + ": " + repoName);
			}

			mApp.compositeDisposable.add(
					mApp.githubAPI.getIssues(repoOwnerLogin, repoName)
							.subscribeOn(Schedulers.io())
							.observeOn(AndroidSchedulers.mainThread())
							.subscribeWith(
									getIssuesObserver()));

			// Detect if using two-pane mode.
			if (getActivity().findViewById(R.id.repo_detail_container) != null) {
				// The detail container view will be present only in the large-screen layouts (res/values-w500dp).
				// If this view is present, then the activity should be in two-pane mode.
				mTwoPane = true;
			}
		}
	}


	private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<GithubIssue> items) {
		if(items != null) {
			recyclerView.setAdapter(new RepoDetailFragment.SimpleItemRecyclerViewAdapter(
					items
			));
		}
	}

	public class SimpleItemRecyclerViewAdapter
			extends RecyclerView.Adapter<RepoDetailFragment.SimpleItemRecyclerViewAdapter.ViewHolder> {

		private final List<GithubIssue> mValues;

		public SimpleItemRecyclerViewAdapter(List<GithubIssue> items) {
			mValues = items;
		}

		@Override
		public RepoDetailFragment.SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.repo_detail_list_content, parent, false);
			return new RepoDetailFragment.SimpleItemRecyclerViewAdapter.ViewHolder(view);
		}

		@Override
		public void onBindViewHolder(final RepoDetailFragment.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
			holder.mIdView.setText(mValues.get(position).id);
			holder.mTitleView.setText(mValues.get(position).title);
			//holder.mCommentsUrlView.setText(mValues.get(position).comments_url);
			holder.mCommentView.setText(mValues.get(position).comment);

			holder.mView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), "Item: " + holder.toString(), Toast.LENGTH_LONG).show();
				}
			});
		}

		@Override
		public int getItemCount() {
			return mValues.size();
		}

		public class ViewHolder extends RecyclerView.ViewHolder {
			public final View mView;

			public final TextView mIdView;
			public final TextView mTitleView;
			//public final TextView mCommentsUrlView = null;
			public final TextView mCommentView;

			public ViewHolder(View view) {
				super(view);
				mView = view;
				mIdView = (TextView) view.findViewById(R.id.issue_id);
				mTitleView = (TextView) view.findViewById(R.id.issue_title);
				//mCommentsUrlView = (TextView) view.findViewById(R.id.issue_comments_url);
				mCommentView = (TextView) view.findViewById(R.id.comment);
			}

			@Override
			public String toString() {
				return 	mIdView.getText() + ", "
						+ mTitleView.getText() + ", "
						//+ mCommentsUrlView.getText() + ", "
						+ mCommentView.getText();
			}
		}
	}

	/**
	 * Creates an observer to be used for handling a user repo selection event
	 * and for showing the repo details.
	 * @return An observer.
	 */
	private DisposableSingleObserver<List<GithubIssue>> getIssuesObserver() {
		return new DisposableSingleObserver<List<GithubIssue>>() {
			@Override
			public void onSuccess(List<GithubIssue> value) {
				// Show the list of details (issues) of the repo.
				View recyclerView = getActivity().findViewById(R.id.repo_details_list);
				assert recyclerView != null;
				if(recyclerView != null && value != null && !value.isEmpty()) {
					setupRecyclerView((RecyclerView) recyclerView, value);
				}
			}

			@Override
			public void onError(Throwable e) {
				e.printStackTrace();
				Toast.makeText(getActivity(), "Can not load issues", Toast.LENGTH_SHORT).show();
			}
		};
	}

}
