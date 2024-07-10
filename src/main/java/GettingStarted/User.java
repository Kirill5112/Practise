package GettingStarted;

public class User {
	//bitbucket
	//oauth2 the official guide here:
	//https://support.atlassian.com/bitbucket-cloud/docs/use-oauth-on-bitbucket-cloud/
	//you need to write the refresh token only once
	public static final String clientId = "fS8DMw9N2ZqbqLNEf6";
	public static final String clientSecret = "VWyAntLys8hSRxD9HVzyjbrpURFD9n8E";
	public static final String refreshToken = "8cVpYaNn4xRvn4WgfG";
	
	//workspace you can get on https://bitbucket.org/{your_Workspace}/workspace/overview/
	public static final String bitbucketWorkpace = "rxiashuv65e0871cb9k6stdv48qpw2";
	//Username in Personal bitbucket settings in Account settings in Bitbucket profile settings
	public static final String userName="rxiashuv65e0871cb9k6stdv-admin";
	// you no need to change this field but you can make sure that you wrote it down correctly 
	//by going to any repository, press clone button and watch https, after last / in "baseUrlBitBucket" field must be: "{reponame}"+".git"
	public static final String baseUrlBitBucket = "https://"+userName+"@bitbucket.org/"+bitbucketWorkpace+"/";
	
	//GitLab
	//workspace you can get on https://gitlab.com/{your workspace} by going on groups and press at workspace
	public static final String gitLabWorkspace="test1390527";
	// then you press you account icon -> preferences-> Access tokens and create new token 
	// in Select scopes you at least need: api, read_api, read_repository, write_repository, ai_features
	//then watch and write your access token ({privateToken}) here
	public static final String privateToken= "glpat-MhGbQnrfi2esKbFcErwt";
	// you don't need to change baseUrlGitLAb
	public static final String baseUrlGitLAb = "https://gitlab.com/"+gitLabWorkspace+"/";
	
	//the path to the folder where you want to clone repositories locally
	public static final String localPath = "C://Users//defaultuser0//Desktop//clone repos//";
}
