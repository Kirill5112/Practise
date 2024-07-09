package TokenService;

import java.util.HashMap;
import java.util.Map;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

//See here to learn more
//https://support.atlassian.com/bitbucket-cloud/docs/use-oauth-on-bitbucket-cloud/

public class TokenService {
	private static final String clientId = "fS8DMw9N2ZqbqLNEf6";
	private static final String clientSecret = "VWyAntLys8hSRxD9HVzyjbrpURFD9n8E";
	private static final String refreshToken = "8cVpYaNn4xRvn4WgfG";
	
	public static HttpResponse<String> refreshAccessToken() throws UnirestException {
		Map<String, Object> fields = new HashMap<>();
		fields.put("grant_type", "refresh_token");
		fields.put("refresh_token", refreshToken);
		HttpResponse<String> response = Unirest.post("https://bitbucket.org/site/oauth2/access_token")
				.basicAuth(clientId, clientSecret).fields(fields).asString();
		return response;
	}
}
