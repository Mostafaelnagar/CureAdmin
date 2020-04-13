package app.grand.ophthalmicadmin.auth;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.auth.viewModels.ForgetPasswordsViewModels;
import app.grand.ophthalmicadmin.base.BaseFragment;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.databinding.FragmentEmailConfirmationBinding;


public class EmailConfirmationFragment extends BaseFragment {
    FragmentEmailConfirmationBinding confirmationBinding;
    ForgetPasswordsViewModels passwordsViewModels;

    public EmailConfirmationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        confirmationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_email_confirmation, container, false);
        passwordsViewModels = new ForgetPasswordsViewModels();
        confirmationBinding.setForgetViewModels(passwordsViewModels);
        checkConnection();
        liveDataListeners();
        return confirmationBinding.getRoot();

    }

    private void liveDataListeners() {
        passwordsViewModels.getClicksMutableLiveData().observe(this, result -> {
            if (result == Codes.BACK) {
                ((Activity) getActivity()).finish();
            } else if (result == View.VISIBLE || result == View.GONE) {
                accessLoadingBar(result);
            } else if (result == Codes.SHOW_MESSAGE_ERROR) {
                showMessage(passwordsViewModels.getReturnedMessage(), 0, 1);
            } else if (result == Codes.SHOW_MESSAGE_SUCCESS) {
                showMessage(passwordsViewModels.getReturnedMessage(), 0, 0);
                passwordsViewModels.goBack(getActivity());
            }
        });
        ConnectionLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isConnected) {
                if (isConnected) {
                    confirmationBinding.btnForget.setEnabled(true);
                } else {
                    confirmationBinding.btnForget.setEnabled(false);
                    showMessage(getActivity().getResources().getString(R.string.connection_invaild_msg), 0, 1);
                }
            }
        });
    }
}
