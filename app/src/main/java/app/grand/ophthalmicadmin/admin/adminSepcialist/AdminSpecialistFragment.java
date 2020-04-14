package app.grand.ophthalmicadmin.admin.adminSepcialist;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.admin.adminSepcialist.viewModels.AdminSpecialistViewModels;
import app.grand.ophthalmicadmin.admin.doctors.viewModels.AdminDoctorsViewModels;
import app.grand.ophthalmicadmin.base.BaseFragment;
import app.grand.ophthalmicadmin.base.MovementManager;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.databinding.FragmentAdminDoctorsBinding;
import app.grand.ophthalmicadmin.databinding.FragmentAdminSpecialistBinding;


public class AdminSpecialistFragment extends BaseFragment {
    FragmentAdminSpecialistBinding specialistBinding;
    AdminSpecialistViewModels specialistViewModels;


    public AdminSpecialistFragment() {
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
        specialistBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_specialist, container, false);
        specialistViewModels = new AdminSpecialistViewModels();
        specialistBinding.setAdminSpecialViewModel(specialistViewModels);
        liveDataListeners();
        specialistViewModels.getSpecialist();
        return specialistBinding.getRoot();
    }


    private void liveDataListeners() {
        specialistViewModels.getClicksMutableLiveData().observe(this, result -> {
            if (result == View.VISIBLE || result == View.GONE) {
                accessLoadingBar(result);
            } else if (result == Codes.ADD_SPECIALIST) {
                MovementManager.startActivity(getActivity(), result);
            }
        });


    }
}
