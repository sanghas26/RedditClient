package sukh.app.ireddit;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * While this looks like a lot of code, all this class actually does is load the
 * posts in to the listview.
 * 
 * @author Sukhdip Sangha
 */
public class PostsFragment extends Fragment {

	ListView postsList;
	View footer;
	PostAdapter adapter;
	Handler handler;
	static FragmentManager fm;

	String subreddit;
	List<Post> posts;
	PostsHolder postsHolder;

	public PostsFragment() {
		handler = new Handler();
		posts = new ArrayList<Post>();
	}

	public static Fragment newInstance(String subreddit, FragmentManager _fm) {
		PostsFragment pf = new PostsFragment();
		pf.subreddit = subreddit;
		pf.postsHolder = new PostsHolder(pf.subreddit);
		fm = _fm;
		return pf;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.posts, container, false);
		postsList = (ListView) v.findViewById(R.id.posts_list);
		footer = inflater.inflate(R.layout.footer, postsList, false);

		return v;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initialize();
	}

	private void initialize() {
		// This should run only once for the fragment as the
		// setRetainInstance(true) method has been called on
		// this fragment

		if (posts.size() == 0) {

			// Must execute network tasks outside the UI
			// thread. So create a new thread.

			new Thread() {
				public void run() {
					posts.addAll(postsHolder.fetchPosts());
					// UI elements should be accessed only in
					// the primary thread, so we must use the
					// handler here.

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

	/**
	 * This method creates the adapter from the list of posts , and assigns it
	 * to the list.
	 */
	private void createAdapter() {

		// Make sure this fragment is still a part of the activity.
		if (getActivity() == null)
			return;

		// Create our custom adapter and set it with our ListView +
		// ScrollListener
		adapter = new PostAdapter(posts);

		// we must add our footer before setting adapter, or error occurs
		postsList.addFooterView(footer);
		postsList.setAdapter(adapter);
		postsList.setOnScrollListener(adapter);
		postsList.setOnItemClickListener(adapter);

		// now we remove footer so it doesn't show right away
		postsList.removeFooterView(footer);
		Log.d("running", "setting adapter");

	}

	/**
	 * This class extends ArrayAdapter with its own OnScrollListener which adds
	 * more posts if it reaches the end of the list. An ASyncTask takes care of
	 * fetchMorePosts then updates the adapter on the postExecute() method.
	 * 
	 * @author Sukh
	 * 
	 */
	protected class PostAdapter extends ArrayAdapter<Post> implements
			OnScrollListener, OnItemClickListener {
		private int previousTotal = 0;
		private boolean loading = true;

		public PostAdapter(List<Post> posts) {
			super(getActivity(), R.layout.post_item, posts);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.post_item, null);
			}

			Post thePost = getItem(position);

			TextView postTitleTextView = (TextView) convertView
					.findViewById(R.id.post_title);
			postTitleTextView.setText(thePost.getTitle());

			TextView postDetailsTextView = (TextView) convertView
					.findViewById(R.id.post_details);
			postDetailsTextView.setText(thePost.getDetails());

			TextView postScoreTextView = (TextView) convertView
					.findViewById(R.id.post_score);
			postScoreTextView.setText(thePost.getScore());

			return convertView;
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if (loading) {
				// update previous total, adding in the footer
				if (totalItemCount > previousTotal + 1) {
					loading = false;
					previousTotal = totalItemCount;
					Log.d("running", "item count: " + totalItemCount);
				}
			}
			if (!loading
					&& (firstVisibleItem + visibleItemCount >= totalItemCount)) {
				new AddPostsTask().execute();
				loading = true;
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}

		/**
		 * An AsyncTask which takes care of grabbing more posts and updating our
		 * adapter, making sure it is on the UI Thread.
		 * 
		 * @author Sukh
		 * 
		 */
		private class AddPostsTask extends AsyncTask<Void, Void, Void> {
			@Override
			protected void onPreExecute() {
				postsList.addFooterView(footer);
				super.onPreExecute();
			}

			@Override
			protected Void doInBackground(Void... params) {
				Log.i("running", "start fetchMorePosts");
				posts.addAll(postsHolder.fetchMorePosts());
				Log.i("running", "end fetchMorePosts");
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				Log.i("running", "start adapterChange");
				postsList.removeFooterView(footer);
				notifyDataSetChanged();
				Log.i("running", "end adapterChange");
				super.onPostExecute(result);
			}

		}

		@Override
		public void onItemClick(AdapterView<?> adapter, View v, int position,
				long arg3) {
			Post selectedPost = (Post) adapter.getItemAtPosition(position);
			Log.d("running", "Clicked on:" + selectedPost.getTitle());
			showClickedPost(selectedPost);
		}
		
		private void showClickedPost(Post _post) {			
			FragmentTransaction ft = fm.beginTransaction();
			Fragment cf = CommentsFragment.newInstance(_post);
			ft.replace(R.id.fragments_holder, cf);
			ft.addToBackStack(null);
			ft.commit();
		}

	}

}
