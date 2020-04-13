package app.grand.ophthalmicadmin.doctor.medicalRecord;


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
import app.grand.ophthalmicadmin.base.Params;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.databinding.FragmentMedicalRecordDetailsBinding;
import app.grand.ophthalmicadmin.doctor.medicalRecord.viewModels.MedicalRecordDetailsViewModels;


public class MedicalRecordDetailsFragment extends BaseFragment {
    FragmentMedicalRecordDetailsBinding recordBinding;
    MedicalRecordDetailsViewModels recordViewModels;
    Bundle bundle;
    String passingObject;

    public MedicalRecordDetailsFragment() {
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
        recordBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_medical_record_details, container, false);
        recordViewModels = new MedicalRecordDetailsViewModels();
        recordBinding.setMedicalRecordViewModel(recordViewModels);
        bundle = this.getArguments();
        if (bundle != null) {
            passingObject = bundle.getString(Params.BUNDLE);
            recordViewModels.setPassingObject(new Gson().fromJson(passingObject, PassingObject.class));
        }
        liveDataListeners();
        checkConnection();
        return recordBinding.getRoot();
    }


    private void liveDataListeners() {
        recordViewModels.getClicksMutableLiveData().observe(this, result -> {
            if (result == View.VISIBLE || result == View.GONE) {
                accessLoadingBar(result);
            }  else if (result == Codes.SHOW_MESSAGE_ERROR) {
                showMessage(recordViewModels.getReturnedMessage(), 1, 1);
            }
        });

        ConnectionLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isConnected) {
                if (isConnected) {
                    recordViewModels.getPatientRays();
                } else {
                    showMessage(getActivity().getResources().getString(R.string.connection_invaild_msg), 0, 1);
                }
            }
        });


    }


}
