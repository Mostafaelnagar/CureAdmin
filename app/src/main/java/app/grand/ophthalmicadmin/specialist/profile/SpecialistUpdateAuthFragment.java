package app.grand.ophthalmicadmin.specialist.profile;


import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;

import app.grand.ophthalmicadmin.PassingObject;
import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.base.BaseFragment;
import app.grand.ophthalmicadmin.base.Params;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.databinding.FragmentUpdateProfileDoctorBinding;
import app.grand.ophthalmicadmin.databinding.FragmentUpdateProfileSpecicalistBinding;
import app.grand.ophthalmicadmin.specialist.profile.viewModels.ProfileViewModels;


public class SpecialistUpdateAuthFragment extends BaseFragment {
    FragmentUpdateProfileSpecicalistBinding updateProfileBinding;
    ProfileViewModels profileViewModels;
    Resources resources;
    private String passingObject;
    private Bundle bundle;

    public SpecialistUpdateAuthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        updateProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_update_profile_specicalist, container, false);
        profileViewModels = new ProfileViewModels();
        updateProfileBinding.setSignUpViewModel(profileViewModels);
        bundle = this.getArguments();
        checkConnection();
        liveDataListeners();
        return updateProfileBinding.getRoot();
    }

    private void liveDataListeners() {
        profileViewModels.getClicksMutableLiveData().observe(this, result -> {
            if (result == View.VISIBLE || result == View.GONE) {
                accessLoadingBar(result);
            } else if (result == Codes.CHECK_ERRORS) {
                if (updateProfileBinding.inputEmail.getError() == null && updateProfileBinding.passwordLayout.getError() == null && updateProfileBinding.inputPhone.getError() == null) {
//                    profileViewModels.signUpAction();
                } else if (updateProfileBinding.inputEmail.getError() != null) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.invalidEmail), Toast.LENGTH_SHORT).show();
                } else if (updateProfileBinding.passwordLayout.getError() != null) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.invalidPassword), Toast.LENGTH_SHORT).show();
                } else if (updateProfileBinding.inputPhone.getError() != null) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.invalidPhone), Toast.LENGTH_SHORT).show();
                }
            } else if (result == Codes.SHOW_MESSAGE_SUCCESS) {
                showMessage(profileViewModels.getReturnedMessage(), 0, 0);
            } else if (result == Codes.SHOW_MESSAGE_ERROR) {
                showMessage(profileViewModels.getReturnedMessage(), 0, 1);
            }
        });
        ConnectionLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isConnected) {
                if (isConnected) {
                    if (bundle != null) {
                        passingObject = bundle.getString(Params.BUNDLE);
                        profileViewModels.setPassingObject(new Gson().fromJson(passingObject, PassingObject.class));
                        if (profileViewModels.getPassingObject().getCode() == Codes.UPDATE_SPECIALIST_AUTH) {
                            profileViewModels.getExtraData();
                        }
                    }
                    updateProfileBinding.btnSignUp.setEnabled(true);
                } else {
                    updateProfileBinding.btnSignUp.setEnabled(false);
                    showMessage(resources.getString(R.string.connection_invaild_msg), 0, 1);
                }
            }
        });
    }


}
