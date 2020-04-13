package app.grand.ophthalmicadmin.admin.adminReservations;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.admin.adminReservations.viewModels.AdminReservationsViewModels;
import app.grand.ophthalmicadmin.base.BaseFragment;
import app.grand.ophthalmicadmin.databinding.FragmentAdminFinishedReservationsBinding;


public class AdminFinishedReservationsFragment extends BaseFragment {
    FragmentAdminFinishedReservationsBinding oldReservationsBinding;
    AdminReservationsViewModels adminReservationsViewModels;


    public AdminFinishedReservationsFragment() {
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
        oldReservationsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_finished_reservations, container, false);
        adminReservationsViewModels = new AdminReservationsViewModels();
        oldReservationsBinding.setOldViewModel(adminReservationsViewModels);
        liveDataListeners();
        checkConnection();
        adminReservationsViewModels.getReservations("finished");
        return oldReservationsBinding.getRoot();
    }


    private void liveDataListeners() {
        adminReservationsViewModels.getClicksMutableLiveData().observe(this, result -> {
            if (result == View.VISIBLE || result == View.GONE) {
                accessLoadingBar(result);
            }
        });


    }

}
