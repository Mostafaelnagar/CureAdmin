package app.grand.ophthalmicadmin.admin.doctors;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.databinding.DataBindingUtil;

import java.util.ArrayList;
import java.util.List;

import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.admin.adminReservations.viewModels.AdminReservationsViewModels;
import app.grand.ophthalmicadmin.admin.doctors.viewModels.AdminDoctorsViewModels;
import app.grand.ophthalmicadmin.base.BaseFragment;
import app.grand.ophthalmicadmin.base.MovementManager;
import app.grand.ophthalmicadmin.base.PopUpMenus;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.databinding.FragmentAdminAddDoctorsBinding;
import app.grand.ophthalmicadmin.databinding.FragmentAdminDoctorsBinding;
import app.grand.ophthalmicadmin.databinding.FragmentAdminFinishedReservationsBinding;


public class AdminDoctorsFragment extends BaseFragment  {
    FragmentAdminDoctorsBinding adminDoctorsBinding;
    AdminDoctorsViewModels adminDoctorsViewModels;


    public AdminDoctorsFragment() {
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
        adminDoctorsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_doctors, container, false);
        adminDoctorsViewModels = new AdminDoctorsViewModels();
        adminDoctorsBinding.setAdminDoctorsViewModel(adminDoctorsViewModels);
        liveDataListeners();
        adminDoctorsViewModels.getDoctors();
        return adminDoctorsBinding.getRoot();
    }


    private void liveDataListeners() {
        adminDoctorsViewModels.getClicksMutableLiveData().observe(this, result -> {
            if (result == View.VISIBLE || result == View.GONE) {
                accessLoadingBar(result);
            }else if (result==Codes.ADD_DOCTOR){
                MovementManager.startActivity(getActivity(),result);
            }
        });


    }
}
