package sukh.app.ireddit;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class CommentsHolder {

	/**
	 * We will be fetching JSON data from the API.
	 */

	Post parentPost;
	List<Comment> comments;
	String url;
	String after;
	String raw;

	CommentsHolder(Post post) {
		parentPost = post;
		after = "";
		generateURL();
	}

	/**
	 * Generates the actual URL from the template based on the post permalink
	 */
	private void generateURL() {
		url = "http://www.reddit.com" + parentPost.getPermalink() + ".json";
	}

	/**
	 * Return list of comments in JSON format
	 * 
	 * @return
	 */
	List<Comment> startFetch() {
		raw = RemoteData.readContents(url);
		JSONArray jsonComments = new JSONArray();

		try {
			JSONObject obj = new JSONArray(raw).getJSONObject(1);
			jsonComments = obj.getJSONObject("data").getJSONArray("children");
		} catch (Exception e) {
			Log.e("fetchComments()", e.toString());
		}

		return fetchComments(jsonComments, 0);
	}

	/**
	 * Returns a list of Comment objects after fetching data from Reddit using
	 * the JSON API.
	 * 
	 * @return
	 */
	private List<Comment> fetchComments(JSONArray comments, int replyNumber) {
		List<Comment> commentList = new ArrayList<Comment>();
		try {
			for (int i = 0; i < comments.length(); i++) {
				JSONObject cur;
				if (comments.getJSONObject(i).optString("kind")
						.equalsIgnoreCase("more")) {
					Log.d("running", "more comments...");
					cur = comments.getJSONObject(i).getJSONObject("data");
					Comment c = createMoreComment(cur, replyNumber);
					commentList.add(c);					
				} else {
					cur = comments.getJSONObject(i).getJSONObject("data");
					Comment c = createComment(cur, replyNumber);

					Log.d("running", c.author + " commented with " + c.body);

					if (!cur.optString("replies").equalsIgnoreCase("")) {

						Log.d("running", "this comment has replies...");

						JSONArray jsonReplies = cur.getJSONObject("replies")
								.getJSONObject("data").getJSONArray("children");

						List<Comment> replyComments = fetchComments(
								jsonReplies, ++replyNumber);

						Log.d("running",
								"Total replies list: " + replyComments.size()
										+ " replies");

						c.replies = replyComments;
					}
					if (c.body != null)
						commentList.add(c);
				}
			}
		} catch (Exception e) {
			Log.e("running", e.toString());
		}
		return commentList;
	}

	private Comment createComment(JSONObject cur, int replyNumber) {
		Comment c = new Comment();

		c.moreComment = false;
		c.author = cur.optString("author");
		c.body = StringEscapeUtils.unescapeHtml4(cur.optString("body"));
		c.ups = cur.optInt("ups");
		c.subreddit = cur.optString("subreddit");
		c.id = cur.optString("id");
		c.replyNumber = replyNumber;

		return c;
	}

	private MoreComment createMoreComment(JSONObject cur, int replyNumber) {
		MoreComment c = new MoreComment();

		c.moreComment = true;
		c.body = "Load more comments...";
		c.replyNumber = replyNumber;
		c.count = cur.optInt("count");
		c.parent_id = cur.optString("parent_id");
		c.id = cur.optString("id");
		c.name = cur.optString("name");
		c.children = new ArrayList<String>();

		try {
			JSONArray jsonChildren = cur.getJSONArray("children");
			for (int i = 0; i < jsonChildren.length(); i++) {
				String child = jsonChildren.get(i).toString();
				c.children.add(child);
			}
		} catch (Exception e) {
			Log.e("running", e.toString());
		}
		return c;
	}

}
