package sukh.app.ireddit;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class CommentsFragment extends Fragment {
	
	Post post;
	List<Comment> comments;
	CommentsHolder commentsHolder;
	Handler handler;
	CommentAdapter adapter;

	View header;
	ListView commentsList;

	public CommentsFragment() {
		comments = new ArrayList<Comment>();
		handler = new Handler();
	}

	public static Fragment newInstance(Post _post) {
		CommentsFragment cf = new CommentsFragment();
		cf.post = _post;		
		cf.commentsHolder = new CommentsHolder(_post);
		return cf;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initialize();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.comments, container, false);
		commentsList = (ListView) v.findViewById(R.id.comments_list);
		header = inflater.inflate(R.layout.comment_header, commentsList, false);

		return v;
	}

	private void initialize() {		
		if (comments.size() == 0) {
			new Thread() {
				public void run() {					
					comments.addAll(commentsHolder.fetchComments());
					Log.d("running", "comments list size: " + comments.size() + " comments");
					handler.post(new Runnable() {
						public void run() {
							createAdapter();
						}
					});
				}
			}.start();
		} else {
			createAdapter();
		}
	}

	private void createAdapter() {
		// Create our custom adapter and set it with our ListView
		adapter = new CommentAdapter(comments);

		// we must add our footer before setting adapter, or error occurs
		//commentsList.addFooterView(footer);		

		commentsList.addHeaderView(header);
		initializeHeader();
		commentsList.setAdapter(adapter);
	}
	
	private void initializeHeader() {
		TextView commentHeaderTitle = (TextView) header.findViewById(R.id.comment_header_title);
		commentHeaderTitle.setText(post.getTitle());
		
		TextView commentHeaderScore = (TextView) header.findViewById(R.id.comment_header_score);
		commentHeaderScore.setText("" + post.getScore());
		
		TextView commentHeaderBody = (TextView) header.findViewById(R.id.comment_header_body);
		commentHeaderBody.setText(post.getSelftext());
	}

	protected class CommentAdapter extends ArrayAdapter<Comment> implements
			OnScrollListener, OnItemClickListener {
		
		public CommentAdapter(List<Comment> comments) {
			super(getActivity(), R.layout.comment_item, comments);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.comment_item, null);
			}
			Comment theComment = getItem(position);
			
			TextView commentUserTextView = (TextView) convertView.findViewById(R.id.comment_user);
			commentUserTextView.setText(theComment.getAuthor());
			Log.i("running", "comment_user: " + theComment.getAuthor());
			
			TextView commentScoreTextView = (TextView) convertView.findViewById(R.id.comment_score);
			commentScoreTextView.setText("" + theComment.getUps());
			Log.i("running", "ups : " + theComment.getUps());
			
			TextView commentContentTextView = (TextView) convertView.findViewById(R.id.comment_content);
			commentContentTextView.setText(theComment.getBody());
			Log.i("running", "body : " + theComment.getBody());
			
			return convertView;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onScrollStateChanged(AbsListView arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

	}

}
