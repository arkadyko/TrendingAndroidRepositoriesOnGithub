package com.actionnodes.githubreposandroid.apis;

/**
 * Created by Arkady Koplyarov on 06/02/18.
 * @author Arkady Koplyarov (arkad.k@gmail.com)
 */

import com.actionnodes.githubreposandroid.models.GithubIssue;
import com.actionnodes.githubreposandroid.models.GithubTrendingResponse;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GithubAPI
{
	String ENDPOINT = "https://api.github.com";

	@GET("search/repositories")
	Single<GithubTrendingResponse> getTrendingKotlin(
			@Query("q") String filter,
			@Query("sort") String sortBy);

	@GET("/repos/{owner}/{repo}/issues")
	Single<List<GithubIssue>> getIssues(
			@Path("owner") String owner,
			@Path("repo") String repository);
}
