package sukh.app.ireddit;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class CommentsHolder {

	/**
	 * We will be fetching JSON data from the API.
	 */
	private final String URL_TEMPLATE = "http://www.reddit.com/r/SUBREDDIT_NAME/"
			+ "TOPIC_NAME/" + ".json";

	Post parentPost;
	String url;
	String after;

	CommentsHolder(Post _post) {
		parentPost = _post;
		after = "";
		generateURL();
	}

	/**
	 * Generates the actual URL from the template based on the subreddit name
	 * and the 'after' property.
	 */
	private void generateURL() {
		url = "http://www.reddit.com" + parentPost.getPermalink() + ".json";		
	}

	/**
	 * Returns a list of Post objects after fetching data from Reddit using the
	 * JSON API.
	 * 
	 * @return
	 */
	List<Comment> fetchComments() {
		String raw = RemoteData.readContents(url);
		List<Comment> list = new ArrayList<Comment>();
		try {
			JSONObject obj = new JSONArray(raw).getJSONObject(1);
			JSONArray children = obj.getJSONObject("data").getJSONArray("children");
			Log.d("running", "JSONArray size: " + children.length());
			for (int i = 0; i < children.length(); i++) {
				JSONObject cur = children.getJSONObject(i)
						.getJSONObject("data");
				Comment c = new Comment();
				if (cur.optJSONObject("replies") != null) {
					//TODO: ADD ALL REPLIES TO LIST
					Log.d("running", "This comment has more replies...");
				}
				c.author = cur.optString("author");
				c.body = cur.optString("body");
				c.ups = cur.optInt("ups");
				c.subreddit = cur.optString("subreddit");
				c.id = cur.optString("id");
				//if (c.body != null)
					list.add(c);
			}
		} catch (Exception e) {
			Log.e("fetchComments()", e.toString());
		}
		return list;
	}

	/**
	 * This is to fetch the next set of posts using the 'after' property
	 * 
	 * @return
	 */
	List<Comment> fetchMoreComments() {
		generateURL();
		return fetchComments();
	}
}
