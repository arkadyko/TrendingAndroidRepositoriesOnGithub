package com.actionnodes.githubreposandroid.models;

/**
 * Created by Arkady Koplyarov on 06/02/18.
 * @author Arkady Koplyarov (arkad.k@gmail.com)
 */

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class GithubTrendingRepoDeserializer implements JsonDeserializer<GithubTrendingRepo>
{
	@Override
	public GithubTrendingRepo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		GithubTrendingRepo repo = new GithubTrendingRepo();

		JsonObject repoJsonObject =  json.getAsJsonObject();
		JsonElement el;

		el = repoJsonObject.get("id");          repo.id = el.isJsonNull() ? (-1) : el.getAsLong();
		el = repoJsonObject.get("name");        repo.name = el.isJsonNull() ? "" : el.getAsString();
		el = repoJsonObject.get("full_name");   repo.fullName = el.isJsonNull() ? "" : el.getAsString();
		el = repoJsonObject.get("language");    repo.language = el.isJsonNull() ? "" : el.getAsString();
		el = repoJsonObject.get("description"); repo.description = el.isJsonNull() ? "" : el.getAsString(); // May be Null Json!
		el = repoJsonObject.get("url");         repo.url = el.isJsonNull() ? "" : el.getAsString();
		el = repoJsonObject.get("html_url");    repo.htmlUrl = el.isJsonNull() ? "" : el.getAsString();
		el = repoJsonObject.get("stargazers_count"); repo.stargazersCount = el.isJsonNull() ? 0 : el.getAsInt();

		JsonElement ownerJsonElement = repoJsonObject.get("owner");
		JsonObject ownerJsonObject = ownerJsonElement.getAsJsonObject();
		repo.owner = new GithubOwner();
		el = ownerJsonObject.get("login");      repo.owner.login = el.isJsonNull() ? "" : el.getAsString();
		el = ownerJsonObject.get("avatar_url"); repo.owner.avatarUrl = el.isJsonNull() ? "" : el.getAsString();

		return repo;
	}
}

