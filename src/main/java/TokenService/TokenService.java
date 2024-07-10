package TokenService;

import java.util.HashMap;
import java.util.Map;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import static GettingStarted.User.*;

public class TokenService {

	
	public static HttpResponse<String> refreshAccessToken() throws UnirestException {
		Map<String, Object> fields = new HashMap<>();
		fields.put("grant_type", "refresh_token");
		fields.put("refresh_token", refreshToken);
		HttpResponse<String> response = Unirest.post("https://bitbucket.org/site/oauth2/access_token")
				.basicAuth(clientId, clientSecret).fields(fields).asString();
		return response;
	}
}
