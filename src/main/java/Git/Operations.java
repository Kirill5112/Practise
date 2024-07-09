package Git;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import com.mashape.unirest.http.exceptions.UnirestException;

import gitLab.Projects;

import static com.practise.git.GitController.repos;

public class Operations {

	private static final String localPath = "C://Users//defaultuser0//Desktop//clone repos//";
	private static final String baseUrl = "https://gitlab.com/test1390527/";
	private static final String privateToken = "glpat-MhGbQnrfi2esKbFcErwt";

	// clone by workspace token
	// https://support.atlassian.com/bitbucket-cloud/docs/using-workspace-access-tokens/
	public String cloneRepo(String workspace, String repo, String accessToken) {
		String repoUrl = "https://x-token-auth" + "@bitbucket.org" + "/" + workspace + "/" + repo;
		String localpath = localPath + repo;
		File tempGitDirectory;
		try {
			tempGitDirectory = new File(localpath);
			if (tempGitDirectory.exists()) {
				FileUtils.deleteDirectory(tempGitDirectory);
			}
		} catch (IOException e) {

		}
		try {
			Git.cloneRepository().setURI(repoUrl).setDirectory(new File(localpath)).setRemote(repo)
					.setCredentialsProvider(new UsernamePasswordCredentialsProvider("x-token-auth", accessToken))
					.setCloneAllBranches(true).call();
			return ("Repository:" + repo + " cloned successfully.");
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
		return "Repository" + repo + " wasn't clone";
	}

	public String CloneAll(String accessToken) {
		int n = repos.size();
		if (repos.isEmpty())
			return "there are no repositories";
		for (int i = 0; i < n; i++) {
			String repo = repos.get(i);
			String workspace = repo.substring(0, repo.indexOf('/'));
			String reppo = repo.substring(repo.indexOf('/') + 1);
			cloneRepo(workspace, reppo, accessToken);
		}
		return "Success";
	}

	public String pushRepo(String repo) throws IOException, UnirestException, GitAPIException, URISyntaxException {
		List<String> repos = Projects.getAllProjects();
		if (!repos.contains(repo)) {
			Projects.createRepo(repo);
		}
		Git git = Git.open(new File(localPath + repo));
		if (git.getRepository().isBare())
			return "Repository:" + repo + " pushed successfully";
		RemoteAddCommand remoteAddCommand = git.remoteAdd();
		remoteAddCommand.setName("origins");
		remoteAddCommand.setUri(new URIish(baseUrl + repo + ".git"));
		remoteAddCommand.call();

		PushCommand pushCommand = git.push();
		pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider("PRIVATE-TOKEN", privateToken));
		pushCommand.setPushAll();
		pushCommand.setRemote("origins");
		pushCommand.call();

		return "Repository:" + repo + " pushed successfully";
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

}
