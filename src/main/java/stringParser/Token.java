package stringParser;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

import TokenService.TokenService;

public class Token {

	public static String accessToken="";
	public static void setToken() throws UnirestException {
	HttpResponse<String> responce=TokenService.refreshAccessToken();
	String body=responce.getBody();
	body=body.substring(body.indexOf("access_token")+16);
	int i=0;
	while (!Character.valueOf(body.charAt(i)).equals('"')) {
		accessToken += body.charAt(i++);
	}
	}
}
