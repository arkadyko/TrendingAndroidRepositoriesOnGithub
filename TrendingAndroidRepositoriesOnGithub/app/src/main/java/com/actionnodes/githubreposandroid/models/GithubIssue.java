package com.actionnodes.githubreposandroid.models;

/**
 * Created by Arkady Koplyarov on 06/02/18.
 * @author Arkady Koplyarov (arkad.k@gmail.com)
 */

import com.google.gson.annotations.SerializedName;

public class GithubIssue
{
	public String id;
	public String title;
	public String comments_url;

	@SerializedName("body")
	public String comment;

	@Override
	public String toString() {
		return id +  " - " + title;
	}
}


