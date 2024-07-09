package stringParser;

import java.util.ArrayList;

public class Repos {
	public static ArrayList<String> NamesParser(String responce) {
		ArrayList<String> repos = new ArrayList<String>();
		int x = responce.indexOf("size") + 6;
		String sizer = "";
		while (!Character.valueOf(responce.charAt(x)).equals(',')) {
			sizer += responce.charAt(x++);
		}
		int size = Integer.parseInt(sizer);
		int j = 0;
		int n = responce.length();
		char[] fullname = "full_name".toCharArray();
		int i = x;
		while (i < n && repos.size() != size) {
			if (responce.charAt(i) == fullname[j])
				j++;
			else
				j = 0;
			if (j == 9) {
				i += 4;
				String repo = "";
				while (!Character.valueOf(responce.charAt(i)).equals('"')) {
					repo += responce.charAt(i++);
				}
				repos.add(repo);
				j = 0;
			}
			i++;
		}
		return repos;
	}

	public static String WorkspaceParser(String responce) {
		String workspace = "";
		responce = responce.substring(responce.indexOf("display_name") + 15);
		int j = 0;
		while (!Character.valueOf(responce.charAt(j)).equals('"')) {
			workspace += responce.charAt(j++);
		}
		return workspace;
	}
}
