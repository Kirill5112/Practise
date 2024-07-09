package Git;

public enum Source {
	BITBUCKET("bitbucket"), GITLAB("gitlab"), GITHUB("github");

	private String git;

	Source(String git) {
		this.git = git;
	}

	public String getGit() {
		return git;
	}
}
