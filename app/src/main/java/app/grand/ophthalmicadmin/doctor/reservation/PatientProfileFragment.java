package app.grand.ophthalmicadmin.doctor.reservation;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;

import app.grand.ophthalmicadmin.PassingObject;
import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.base.BaseFragment;
import app.grand.ophthalmicadmin.base.MovementManager;
import app.grand.ophthalmicadmin.base.Params;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.databinding.FragmentPatientProfileBinding;
import app.grand.ophthalmicadmin.doctor.reservation.viewModels.PatientProfileViewModels;


public class PatientProfileFragment extends BaseFragment {
    FragmentPatientProfileBinding patientProfileBinding;
    PatientProfileViewModels patientProfileViewModels;
    Bundle bundle;
    String passingObject;

    public PatientProfileFragment() {
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
        patientProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_patient_profile, container, false);
        patientProfileViewModels = new PatientProfileViewModels();
        patientProfileBinding.setPatientViewModel(patientProfileViewModels);
        bundle = this.getArguments();
        if (bundle != null) {
            passingObject = bundle.getString(Params.BUNDLE);
            patientProfileViewModels.setPassingObject(new Gson().fromJson(passingObject, PassingObject.class));
        }
        liveDataListeners();
        checkConnection();
        return patientProfileBinding.getRoot();
    }


    private void liveDataListeners() {
        patientProfileViewModels.getClicksMutableLiveData().observe(this, result -> {
            if (result == View.VISIBLE || result == View.GONE) {
                accessLoadingBar(result);
            } else if (result == Codes.SHOW_MESSAGE_ERROR) {
                showMessage(patientProfileViewModels.getReturnedMessage(), 0, 1);
            } else if (result == Codes.SHOW_MESSAGE_SUCCESS) {
                showMessage(patientProfileViewModels.getReturnedMessage(), 0, 0);
                patientProfileViewModels.goBack(getActivity());
            } else if (result == Codes.MEDICAL_RECORD) {
                MovementManager.startActivityWithObject(getActivity(), result, new PassingObject(patientProfileViewModels.getPassingObject().getObjectClass().getId()));
            }
        });

        ConnectionLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isConnected) {
                if (isConnected) {

                } else {
                    showMessage(getActivity().getResources().getString(R.string.connection_invaild_msg), 0, 1);
                }
            }
        });


    }


}
