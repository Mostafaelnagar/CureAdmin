package app.grand.ophthalmicadmin.auth;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.auth.viewModels.AuthViewModels;
import app.grand.ophthalmicadmin.base.BaseFragment;
import app.grand.ophthalmicadmin.base.MovementManager;
import app.grand.ophthalmicadmin.base.Validate;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.databinding.FragmentLoginBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment {
    FragmentLoginBinding loginBinding;
    AuthViewModels authViewModels;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        loginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        authViewModels = new AuthViewModels();
        checkConnection();
        loginBinding.setLoginViewModel(authViewModels);
        liveDataListeners();

        return loginBinding.getRoot();

    }


    private void liveDataListeners() {
        authViewModels.getClicksMutableLiveData().observe(this, result -> {
            if (result == Codes.SEND_CODE_SCREEN) {
                MovementManager.startActivity(getActivity(), result);
            } else if (result == Codes.DOCTOR_HOME) {
                MovementManager.startBaseActivity(getActivity(), result);
            } else if (result == Codes.Specialist_HOME) {
                MovementManager.startBaseActivity(getActivity(), result);
            } else if (result == Codes.ADMIN_HOME) {
                MovementManager.startBaseActivity(getActivity(), result);
            } else if (result == Codes.SHOW_MESSAGE_SUCCESS) {
                showMessage(authViewModels.getReturnedMessage(), 0, 0);
            } else if (result == Codes.SHOW_MESSAGE_ERROR) {
                showMessage(authViewModels.getReturnedMessage(), 0, 1);
            } else if (result == Codes.CHECK_ERRORS) {
                if (Validate.isInputValid(loginBinding.inputUserName, loginBinding.userName, 0, getActivity())
                        && Validate.isInputValid(loginBinding.inputPassword, loginBinding.password, 1, getActivity())) {
                    authViewModels.loginAction();
                }
            } else if (result == View.VISIBLE || result == View.GONE) {
                accessLoadingBar(result);
            }
        });
        ConnectionLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isConnected) {
                if (isConnected) {
                    loginBinding.btnLogin.setEnabled(true);
                } else {
                    loginBinding.btnLogin.setEnabled(false);
                    showMessage(getActivity().getResources().getString(R.string.connection_invaild_msg), 0, 1);
                }
            }
        });
    }

}
