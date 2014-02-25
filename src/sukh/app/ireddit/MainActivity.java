package sukh.app.ireddit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * As of now, all this activity does is create and render a fragment.
 * 
 * @author Sukhdip Sangha
 */
public class MainActivity extends FragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		addFragment();
	}

	void addFragment() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment subredditFragment = PostsFragment.newInstance("bodybuilding", fm);
		ft.add(R.id.fragments_holder, subredditFragment).commit();
	}
}