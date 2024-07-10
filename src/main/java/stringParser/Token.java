package stringParser;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

import TokenService.TokenService;

public class Token {

	public static String setToken() {
		String accessToken = "";
		try {
			accessToken = "";
			HttpResponse<String> responce;
			responce = TokenService.refreshAccessToken();
			String body = responce.getBody();
			body = body.substring(body.indexOf("access_token") + 16);
			int i = 0;
			while (!Character.valueOf(body.charAt(i)).equals('"')) {
				accessToken += body.charAt(i++);
			}
		} catch (UnirestException e) {
			e.printStackTrace();
		}

		return accessToken;
	}
}
