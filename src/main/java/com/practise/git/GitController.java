package com.practise.git;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import Git.Operations;
import Git.Source;
import stringParser.Repos;
import stringParser.Token;

import static stringParser.Token.accessToken;

@RestController
@RequestMapping("/api")
public class GitController {

	private static Operations operation = new Operations();
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(GitController.class);
	public static ArrayList<String> repos = new ArrayList<String>();
	private String workspace = "";

	@GetMapping("/findAll/{source}/{purpose}")
	public String FindAllRepos(@PathVariable String source, @PathVariable String purpose) throws UnirestException {
		if (!source.equals(Source.BITBUCKET.getGit()) || !purpose.equals(Source.GITLAB.getGit()))
			throw new UnsupportedOperationException();
		Token.setToken();
		HttpResponse<JsonNode> response = Unirest
				.get("https://api.bitbucket.org/2.0/repositories/rxiashuv65e0871cb9k6stdv48qpw2")
				.header("Accept", "application/json").header("Authorization", "Bearer " + accessToken).asJson();
		if (response.getStatus() == 401)
			return "Токен устарел";
		String body = response.getBody().toString();
		workspace = Repos.WorkspaceParser(body);
		repos = Repos.NamesParser(body);
		String ret = source + "-->" + workspace + "-->";
		for (String repo : repos) {
			ret += repo + ", ";
		}
		ret = ret.substring(0, ret.length() - 2);
		return ret;
	}

	@GetMapping("/clone/{workspace}/{repo}")
	public String cloneRepo(@PathVariable String workspace, @PathVariable String repo) {
		return operation.cloneRepo(workspace, repo, accessToken);
	}

	@GetMapping("/cloneAll")
	public String cloneAll() {
		return operation.CloneAll(accessToken);
	}

	@GetMapping("/push/{repo}")
	public String pushRepo(@PathVariable String repo) throws IOException, UnirestException, GitAPIException, URISyntaxException {
			return operation.pushRepo(repo);
	}
	
	@GetMapping("/pushAll")
	public String pushAll() throws IOException, UnirestException, GitAPIException, URISyntaxException {
		return operation.pushAll();
	}
}
