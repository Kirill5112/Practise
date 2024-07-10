package com.practise.git;

import java.io.IOException;
import java.net.URISyntaxException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mashape.unirest.http.exceptions.UnirestException;

import Git.Operations;

//you can use this app in http://localhost:8080/...  in a service like postman (send api request)
//@GetMapping is "GET" request and @PutMapping is "Put"
@RestController
@RequestMapping("/api")
public class GitController {

	private static Operations operation = new Operations();

	// after launching the program, be sure to start with this request to fill in the list of repositories
	//Remember that this application only provides source:"bitbucket" and purpose "gitlab"
	@GetMapping("/findAll/{source}/{purpose}")
	public String FindAllRepos(@PathVariable String source, @PathVariable String purpose) throws UnirestException {
		return operation.FindAllRepos(source, purpose);
	}
	//you got after "/findAll/{source}/{purpose}" request responce like this: 
	//bitbucket-->MainWorkSpace-->rxiashuv65e0871cb9k6stdv48qpw2/test,
	//rxiashuv65e0871cb9k6stdv48qpw2/oop, rxiashuv65e0871cb9k6stdv48qpw2/emptyrepo
	// and you can write request like http://localhost:8080/api/clone/rxiashuv65e0871cb9k6stdv48qpw2/test  to clone "test" repository
	@PutMapping("/clone/{workspace}/{repo}")
	public String cloneRepo(@PathVariable String workspace, @PathVariable String repo) {
		return operation.cloneRepo(workspace, repo);
	}
	//clone all repositories from bitbucket to localpath
	@PutMapping("/cloneAll")
	public String cloneAll() {
		return operation.CloneAll();
	}
	//send it to gitlab 
	//only use repository name
	@PutMapping("/push/{repo}")
	public String pushRepo(@PathVariable String repo)
			throws IOException, UnirestException, GitAPIException, URISyntaxException {
		return operation.pushRepo(repo);
	}
	//send all
	@PutMapping("/pushAll")
	public String pushAll() throws IOException, UnirestException, GitAPIException, URISyntaxException {
		return operation.pushAll();
	}
}
