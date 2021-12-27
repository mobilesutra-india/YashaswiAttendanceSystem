package seedcommando.com.yashaswi.manageractivity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;

import seedcommando.com.yashaswi.R;

/**
 * Created by commando1 on 8/28/2017.
 */

public class TabListener implements ActionBar.TabListener {

    Fragment fragment;

    public TabListener(Fragment fragment) {
// TODO Auto-generated constructor stub
        this.fragment = fragment;
    }
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
// TODO Auto-generated method stub
        ft.replace(R.id.fragment_container, fragment);
    }
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
// TODO Auto-generated method stub
        ft.remove(fragment);
    }
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
// TODO Auto-generated method stub

    }
}
