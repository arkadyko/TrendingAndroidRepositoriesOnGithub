package com.actionnodes.githubreposandroid.models;

/**
 * Created by Arkady Koplyarov on 06/02/18.
 * @author Arkady Koplyarov (arkad.k@gmail.com)
 */

import java.util.List;

public class GithubTrendingResponse
{
	public String totalCount; // "total_count"
	public List<GithubTrendingRepo> items; // "items"
}
