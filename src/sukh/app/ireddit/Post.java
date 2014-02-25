package sukh.app.ireddit;
 
/**
 * This is a class that holds the data of the JSON objects
 * returned by the Reddit API.
 * 
 * @author Sukhdip Sangha
 */
public class Post {
     
    String subreddit;
    String title;
    String author;
    String selftext;
    int points;
    int numComments;
    String permalink;
    String url;    
    String domain;
    String id;
     
    String getDetails(){
        String details=author
                       +" posted this and got "
                       +numComments
                       +" replies";
        return details;    
    }
     
    String getTitle(){
        return title;
    }
     
    String getScore(){
        return Integer.toString(points);
    }
    
    String getPermalink(){
    	return permalink;
    }
    
    String getSelftext(){
    	return selftext;
    }
}