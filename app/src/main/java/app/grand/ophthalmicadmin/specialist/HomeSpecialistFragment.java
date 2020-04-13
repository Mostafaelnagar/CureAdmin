package app.grand.ophthalmicadmin.specialist;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.base.BaseFragment;
import app.grand.ophthalmicadmin.base.MovementManager;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.databinding.FragmentHomeDoctorBinding;
import app.grand.ophthalmicadmin.databinding.FragmentHomeSpecialistBinding;
import app.grand.ophthalmicadmin.specialist.viewModels.SpecialistViewModels;


public class HomeSpecialistFragment extends BaseFragment {
    FragmentHomeSpecialistBinding doctorBinding;
    SpecialistViewModels homeViewModels;


    public HomeSpecialistFragment() {
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
        doctorBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_specialist, container, false);
        homeViewModels = new SpecialistViewModels();
        doctorBinding.setHomeViewModel(homeViewModels);
        liveDataListeners();
        return doctorBinding.getRoot();
    }


    private void liveDataListeners() {
        homeViewModels.getClicksMutableLiveData().observe(this, result -> {
            if (result == View.VISIBLE) {
                accessLoadingBar(result);
            } else if (result == Codes.SHOW_MESSAGE_ERROR) {
                showMessage(homeViewModels.getReturnedMessage(), 1, 1);
            } else if (result == Codes.Specialist_RESERVATIONS) {
                MovementManager.startActivity(getActivity(), result);
            } else if (result == Codes.Specialist_NOTIFICATIONS) {
                MovementManager.startActivity(getActivity(), result);
            } else if (result == Codes.Specialist_PROFILE) {
                MovementManager.startActivity(getActivity(), result);
            }
        });

    }

}
