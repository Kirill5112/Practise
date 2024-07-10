package com.practise.git;

import java.io.IOException;
import java.net.URISyntaxException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mashape.unirest.http.exceptions.UnirestException;

import Git.Operations;

@RestController
@RequestMapping("/api")
public class GitController {

	private static Operations operation = new Operations();
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(GitController.class);

	// Сначала нужно получать список репозиториев, чтобы он заполнился
	@GetMapping("/findAll/{source}/{purpose}")
	public String FindAllRepos(@PathVariable String source, @PathVariable String purpose) throws UnirestException {
		return operation.FindAllRepos(source, purpose);
	}

	@GetMapping("/clone/{workspace}/{repo}")
	public String cloneRepo(@PathVariable String workspace, @PathVariable String repo) {
		return operation.cloneRepo(workspace, repo);
	}

	@GetMapping("/cloneAll")
	public String cloneAll() {
		return operation.CloneAll();
	}

	@GetMapping("/push/{repo}")
	public String pushRepo(@PathVariable String repo)
			throws IOException, UnirestException, GitAPIException, URISyntaxException {
		return operation.pushRepo(repo);
	}

	@GetMapping("/pushAll")
	public String pushAll() throws IOException, UnirestException, GitAPIException, URISyntaxException {
		return operation.pushAll();
	}
}
