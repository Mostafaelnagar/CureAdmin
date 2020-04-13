package app.grand.ophthalmicadmin.admin.adminReservations.adapters;


import android.content.res.Resources;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.admin.adminReservations.AdminWaitingReservationsFragment;
import app.grand.ophthalmicadmin.admin.adminReservations.AdminOnGoingReservationsFragment;
import app.grand.ophthalmicadmin.admin.adminReservations.AdminFinishedReservationsFragment;
import app.grand.ophthalmicadmin.base.MyApplication;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new AdminFinishedReservationsFragment();
        } else if (position == 1) {
            return new AdminWaitingReservationsFragment();
        } else
            return new AdminOnGoingReservationsFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        Resources resources = MyApplication.getInstance().getResources();
        if (position == 0) {
            title = resources.getString(R.string.oldReservations);
        } else if (position == 1) {
            title = resources.getString(R.string.currentReservations);
        } else if (position == 2) {
            title = resources.getString(R.string.futureReservations);
        }
        return title;
    }
}
