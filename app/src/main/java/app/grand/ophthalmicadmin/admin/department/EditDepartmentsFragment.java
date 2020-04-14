package app.grand.ophthalmicadmin.admin.department;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;

import app.grand.ophthalmicadmin.PassingObject;
import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.admin.department.viewModels.SettingsViewModels;
import app.grand.ophthalmicadmin.base.BaseFragment;
import app.grand.ophthalmicadmin.base.Params;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.databinding.AddNewDepartmentDialogBinding;
import app.grand.ophthalmicadmin.databinding.EditDepartmentDialogBinding;

import static android.app.Activity.RESULT_OK;


public class EditDepartmentsFragment extends BaseFragment {
    EditDepartmentDialogBinding departmentsBinding;
    SettingsViewModels settingsViewModels;
    private String passingObject;
    private Bundle bundle;


    public EditDepartmentsFragment() {
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
        departmentsBinding = DataBindingUtil.inflate(inflater, R.layout.edit_department_dialog, container, false);
        settingsViewModels = new SettingsViewModels();
        departmentsBinding.setDepartmentViewModel(settingsViewModels);
        bundle = this.getArguments();
        if (bundle != null) {
            passingObject = bundle.getString(Params.BUNDLE);
            settingsViewModels.setPassingObject(new Gson().fromJson(passingObject, PassingObject.class));
        }
        liveDataListeners();
        checkConnection();
        return departmentsBinding.getRoot();
    }


    private void liveDataListeners() {
        settingsViewModels.getClicksMutableLiveData().observe(this, result -> {
            if (result == View.VISIBLE || result == View.GONE) {
                accessLoadingBar(result);
            } else if (result == Codes.SHOW_MESSAGE_ERROR) {
                showMessage(settingsViewModels.getReturnedMessage(), 0, 1);
            } else if (result == Codes.SHOW_MESSAGE_SUCCESS) {
                showMessage(settingsViewModels.getReturnedMessage(), 0, 0);
                settingsViewModels.goBack(getActivity());
                Params.DATA_CHANGE = true;
            }
        });

        ConnectionLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isConnected) {
                if (isConnected) {
                    settingsViewModels.getDepartments();
                } else {
                    showMessage(getActivity().getResources().getString(R.string.connection_invaild_msg), 0, 1);
                }
            }
        });


    }


}
