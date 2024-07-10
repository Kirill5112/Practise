package gitLab;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jayway.jsonpath.JsonPath;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import static GettingStarted.User.*;

public class Projects {

	public static List<String> getAllProjects() throws UnirestException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("private_token", privateToken);
		parameters.put("owned", true);
		parameters.put("visibility", "private");
		HttpResponse<String> response = Unirest.get("https://gitlab.com/api/v4/projects").queryString(parameters)
				.asString();
		return JsonPath.read(response.getBody(), "$.[*].name");
	}

	public static void createRepo(String repo) throws UnirestException {
		Map<String, Object> fields = new HashMap<>();
		fields.put("name", repo);
		fields.put("path", repo);
		fields.put("namespace_id", 89772728);// namespace_name test
		fields.put("initialize_with_readme", false);
		fields.put("description", "Created by my git API");
		fields.put("visibility", "private");
		Unirest.post("https://gitlab.com/api/v4/projects").header("PRIVATE-TOKEN", privateToken).fields(fields)
				.asString();
	}
}
