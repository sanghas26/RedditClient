package sukh.app.ireddit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class PostContentsFragment extends Fragment {

	String permalink;
	Post post;
	TextView postTitleView;
	TextView postSelfTextView;

	public PostContentsFragment() {
	}

	public static Fragment newInstance(Post _post) {
		PostContentsFragment pcf = new PostContentsFragment();
		pcf.post = _post;
		return pcf;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("running", "Setting view for post");
		View v = inflater.inflate(R.layout.post_contents, container, false);
		postTitleView = (TextView) v.findViewById(R.id.post_contents_title);
		postTitleView.setText(post.getTitle());
		
		postSelfTextView = (TextView) v.findViewById(R.id.post_contents_selftext);
		postSelfTextView.setText(post.getSelftext());

		return v;
	}

}
