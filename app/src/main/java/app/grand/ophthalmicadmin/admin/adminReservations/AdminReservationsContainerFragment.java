package app.grand.ophthalmicadmin.admin.adminReservations;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.admin.adminReservations.adapters.ViewPagerAdapter;
import app.grand.ophthalmicadmin.admin.adminReservations.viewModels.AdminReservationsViewModels;
import app.grand.ophthalmicadmin.base.BaseFragment;
import app.grand.ophthalmicadmin.databinding.FragmentAdminReservationsContainerBinding;


public class AdminReservationsContainerFragment extends BaseFragment {
    FragmentAdminReservationsContainerBinding containerBinding;
    AdminReservationsViewModels homeViewModels;


    public AdminReservationsContainerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        containerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_reservations_container, container, false);
        homeViewModels = new AdminReservationsViewModels();
        containerBinding.setHomeViewModel(homeViewModels);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        containerBinding.pager.setOffscreenPageLimit(1);
        containerBinding.pager.setAdapter(pagerAdapter);
        containerBinding.tabs.setupWithViewPager(containerBinding.pager);
        liveDataListeners();
        return containerBinding.getRoot();
    }


    private void liveDataListeners() {
        homeViewModels.getClicksMutableLiveData().observe(this, result -> {
            if (result == View.VISIBLE || result == View.GONE) {
                accessLoadingBar(result);
            }

        });
    }

}
