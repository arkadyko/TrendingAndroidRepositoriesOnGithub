package com.actionnodes.githubreposandroid.models;

/**
 * Created by Arkady Koplyarov on 06/02/18.
 * @author Arkady Koplyarov (arkad.k@gmail.com)
 */

public class GithubTrendingRepo
{
	public Long id; // "id"
	public String name; // "name"
	public String fullName; // "full_name"
	public String language; // "language"
	public String description; // "description"
	public String url; // "url"
	public String htmlUrl; // "html_url"
	public Integer stargazersCount; // "stargazers_count"
	public GithubOwner owner; // "owner"

	@Override
	public String toString() {
		return(owner.login +" " + fullName + " " +  url);
	}
}
