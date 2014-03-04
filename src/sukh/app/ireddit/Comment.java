package sukh.app.ireddit;

import java.util.List;

import org.json.JSONObject;

/**
 * This is a class that holds the comment data of the JSON objects
 * returned by the Reddit API.
 * 
 * @author Sukhdip Sangha
 */

public class Comment {
	String subreddit;
	String subreddit_id;
	String id;
	String author;
	String parent_id;
	String body;
	String body_html;
	String link_id;
	String name;	

	List<Comment> repliesList;
	JSONObject replies;
	
	int ups;
	int downs;
	
	public String getSubreddit() {
		return subreddit;
	}
	public void setSubreddit(String subreddit) {
		this.subreddit = subreddit;
	}
	public String getSubreddit_id() {
		return subreddit_id;
	}
	public void setSubreddit_id(String subreddit_id) {
		this.subreddit_id = subreddit_id;
	}
	public JSONObject getReplies() {
		return replies;
	}
	public void setReplies(JSONObject replies) {
		this.replies = replies;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getBody_html() {
		return body_html;
	}
	public void setBody_html(String body_html) {
		this.body_html = body_html;
	}
	public String getLink_id() {
		return link_id;
	}
	public void setLink_id(String link_id) {
		this.link_id = link_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getUps() {
		return ups;
	}
	public void setUps(int ups) {
		this.ups = ups;
	}
	public int getDowns() {
		return downs;
	}
	public void setDowns(int downs) {
		this.downs = downs;
	}
	
	
}
