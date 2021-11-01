package cytech.android.mathinspector;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements Fragment1.OnFragment1Listener{
    private static final String TAG_1 = "1";
    private static final String TAG_2 = "2";
    Fragment1 fragment1;
    Fragment2 fragment2;

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    // handle action bar button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btnAbout) {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View formElementsView = inflater.inflate(R.layout.about, null, false);
            //Create a dialog
            new AlertDialog.Builder(this)
                    .setView(formElementsView)
                    .setTitle(R.string.about_title)
                    .setPositiveButton("Close",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // add fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            fragment1 = (Fragment1) fragmentManager.getFragment(savedInstanceState, "fragment1");
            fragment2 = (Fragment2) fragmentManager.getFragment(savedInstanceState, "fragment2");
        }
        else {
            fragment1 = (Fragment1) fragmentManager.findFragmentByTag(TAG_1);
            if (fragment1 == null) {
                fragment1 = new Fragment1();
                fragmentManager.beginTransaction().add(R.id.fragment1_container, fragment1, TAG_1).commit();
            }

            fragment2 = (Fragment2) fragmentManager.findFragmentByTag(TAG_2);
            if (fragment2 == null) {
                fragment2 = new Fragment2();
                fragmentManager.beginTransaction().add(R.id.fragment2_container, fragment2, TAG_2).commit();
            }
        }

    }

    // The Activity handles receiving a message from one Fragment
    // and passing it on to the other Fragment
    @Override
    public void messageFromFragment1(String data, int type) {
        fragment2.receiveMessage(data, type);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "fragment1", fragment1);
        getSupportFragmentManager().putFragment(outState, "fragment2", fragment2);
    }
}