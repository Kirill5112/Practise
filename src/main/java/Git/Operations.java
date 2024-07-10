package Git;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import gitLab.Projects;
import stringParser.Repos;
import stringParser.Token;

import static GettingStarted.User.*;

public class Operations {
	private String workspace = "";
	public static ArrayList<String> repos = new ArrayList<String>();
	private static final String accessToken = Token.setToken();
	private final static UsernamePasswordCredentialsProvider user = new UsernamePasswordCredentialsProvider(
			"x-token-auth", accessToken);

	public String FindAllRepos(String source, String purpose) throws UnirestException {
		if (!source.equals(Source.BITBUCKET.getGit()) || !purpose.equals(Source.GITLAB.getGit()))
			throw new UnsupportedOperationException();
		HttpResponse<JsonNode> response = Unirest
				.get("https://api.bitbucket.org/2.0/repositories/"+bitbucketWorkpace)
				.header("Accept", "application/json").header("Authorization", "Bearer " + accessToken).asJson();
		if (response.getStatus() == 401)
			return "Bad Request";
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

	public String pullRepo(String repo) {
		Git git;
		try {
			git = Git.open(new File(localPath + repo));
		} catch (IOException e) {
			return "there is no such local repository";
		}
		try {
			List<String> localBranches = new ArrayList<String>();
			List<Ref> refs = git.branchList().call();
			for (Ref ref : refs) {
				localBranches.add(ref.getName().substring(ref.getName().lastIndexOf("/") + 1, ref.getName().length()));
			}
			List<String> branches = fetchGitBranches(baseUrlBitBucket + repo + ".git");
			for (String branch : branches) {
				if (branch != "main") {
					git.pull().setCredentialsProvider(user).call();
					git.branchCreate().setForce(true).setName(branch).setStartPoint(repo + "/" + branch).call();
					git.checkout().setName(branch).call();
				}
			}
			for (String branch : branches) {
				git.pull().setRemoteBranchName(branch).setCredentialsProvider(user).call();
			}
		} catch (GitAPIException e) {
			e.printStackTrace();
		} finally {
			git.close();
		}
		return "";
	}

	// clone by workspace token
	// https://support.atlassian.com/bitbucket-cloud/docs/using-workspace-access-tokens/
	public String cloneRepo(String workspace, String repo) {
		String repoUrl = "https://x-token-auth" + "@bitbucket.org" + "/" + workspace + "/" + repo;
		String localpath = localPath + repo;
		File tempGitDirectory;
		tempGitDirectory = new File(localpath);
		if (tempGitDirectory.exists()) {
			pullRepo(repo);
			return "Repository:" + repo + " pulled successfully.";
		}
		try {
			List<String> branches = fetchGitBranches(baseUrlBitBucket + repo + ".git");
			Git.cloneRepository().setURI(repoUrl).setDirectory(new File(localpath)).setRemote(repo)
					.setCredentialsProvider(user).setCloneAllBranches(true).call().close();
			Git git = Git.open(new File(localpath));
			for (String branch : branches) {
				if (branch != "main") {
					git.pull().setCredentialsProvider(user).call();
					git.branchCreate().setForce(true).setName(branch).setStartPoint(repo + "/" + branch).call();
					git.checkout().setName(branch).call();
				}
			}
			return ("Repository:" + repo + " cloned successfully.");
		} catch (GitAPIException | IOException e) {
			e.printStackTrace();
		}
		return "Repository" + repo + " wasn't clone";
	}

	public String CloneAll() {
		int n = repos.size();
		if (repos.isEmpty())
			return "there are no repositories";
		for (int i = 0; i < n; i++) {
			String repo = repos.get(i);
			String workspace = repo.substring(0, repo.indexOf('/'));
			String reppo = repo.substring(repo.indexOf('/') + 1);
			cloneRepo(workspace, reppo);
		}
		return "Success";
	}

	public String pushRepo(String repo) throws UnirestException {
		Git git;
		try {
			git = Git.open(new File(localPath + repo));
		} catch (IOException e) {
			return "there is no such local repository";
		}
		String ret = "";
		List<String> repos = Projects.getAllProjects();
		if (!repos.contains(repo)) {
			Projects.createRepo(repo);
		}
		try {
			RemoteAddCommand remoteAddCommand = git.remoteAdd();
			remoteAddCommand.setName("origins");
			remoteAddCommand.setUri(new URIish(baseUrlGitLAb + repo + ".git"));
			remoteAddCommand.call();

			PushCommand pushCommand = git.push();
			pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider("PRIVATE-TOKEN", privateToken));
			pushCommand.setPushAll();
			pushCommand.setRemote("origins");
			pushCommand.call();
			ret = "Repository:" + repo + " pushed successfully";
		} catch (TransportException e) {
			ret = "Created empty repository";
		} catch (GitAPIException | URISyntaxException e) {
		} finally {
			git.close();
		}
		return ret;
	}

	public String pushAll() throws IOException, UnirestException, GitAPIException, URISyntaxException {
		int n = repos.size();
		if (repos.isEmpty())
			return "there are no repositories to push";
		for (int i = 0; i < n; i++) {
			String repo = repos.get(i);
			String reppo = repo.substring(repo.indexOf('/') + 1);
			pushRepo(reppo);
		}
		return "Success";
	}

	public static List<String> fetchGitBranches(String gitUrl) {
		Collection<Ref> refs;
		List<String> branches = new ArrayList<String>();
		try {
			refs = Git.lsRemoteRepository().setHeads(true).setRemote(gitUrl).setCredentialsProvider(user).call();
			for (Ref ref : refs) {
				branches.add(ref.getName().substring(ref.getName().lastIndexOf("/") + 1, ref.getName().length()));
			}
			Collections.sort(branches);
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
		return branches;
	}
}
