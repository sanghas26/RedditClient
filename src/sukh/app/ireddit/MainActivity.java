package sukh.app.ireddit;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
 
/**
 * As of now, all this activity does is create and
 * render a fragment.
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
     
    void addFragment(){
        getSupportFragmentManager()
            .beginTransaction()
            .add(R.id.fragments_holder
                 , PostsFragment.newInstance("bodybuilding"))
            .commit();
    }
}