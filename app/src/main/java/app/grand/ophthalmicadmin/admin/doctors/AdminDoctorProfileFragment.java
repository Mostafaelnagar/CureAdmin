package app.grand.ophthalmicadmin.admin.doctors;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;

import app.grand.ophthalmicadmin.PassingObject;
import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.admin.doctors.viewModels.AdminDoctorsViewModels;
import app.grand.ophthalmicadmin.base.BaseFragment;
import app.grand.ophthalmicadmin.base.MovementManager;
import app.grand.ophthalmicadmin.base.Params;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.databinding.FragmentAdminDoctorProfileBinding;
import app.grand.ophthalmicadmin.databinding.FragmentAdminDoctorsBinding;


public class AdminDoctorProfileFragment extends BaseFragment {
    FragmentAdminDoctorProfileBinding adminDoctorsBinding;
    AdminDoctorsViewModels adminDoctorsViewModels;
    private String passingObject;
    private Bundle bundle;


    public AdminDoctorProfileFragment() {
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
        adminDoctorsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_doctor_profile, container, false);
        adminDoctorsViewModels = new AdminDoctorsViewModels();
        adminDoctorsBinding.setAdminDoctorsViewModel(adminDoctorsViewModels);
        bundle = this.getArguments();
        if (bundle != null) {
            passingObject = bundle.getString(Params.BUNDLE);
            adminDoctorsViewModels.setPassingObject(new Gson().fromJson(passingObject, PassingObject.class));
            adminDoctorsViewModels.getDoctorProfile();
        }
        liveDataListeners();
        return adminDoctorsBinding.getRoot();
    }


    private void liveDataListeners() {
        adminDoctorsViewModels.getClicksMutableLiveData().observe(this, result -> {
            if (result == View.VISIBLE || result == View.GONE) {
                accessLoadingBar(result);
            }
        });


    }
}
