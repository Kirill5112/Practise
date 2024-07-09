package Git;

import java.io.File;
import java.io.IOException;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;


import static com.practise.git.GitController.repos;

public class Operations {

	// clone by workspace token
	// https://support.atlassian.com/bitbucket-cloud/docs/using-workspace-access-tokens/

	public String cloneRepo(String workspace, String repo, String accessToken) {
		String repoUrl = "https://x-token-auth" + "@bitbucket.org" + "/" + workspace + "/" + repo;
		String localpath = "C:\\Users\\defaultuser0\\Desktop\\clone repos\\" + repo;
		File tempGitDirectory;
		try {
			tempGitDirectory = new File(localpath);
			if (tempGitDirectory.exists()) {
				FileUtils.deleteDirectory(tempGitDirectory);
			}
		} catch (IOException e) {

		}
		try {
			Git.cloneRepository().setURI(repoUrl).setDirectory(new File(localpath))
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
		return "All";
	}


}
