package app.grand.ophthalmicadmin.admin.department;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.theartofdev.edmodo.cropper.CropImage;

import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.admin.department.viewModels.SettingsViewModels;
import app.grand.ophthalmicadmin.base.BaseFragment;
import app.grand.ophthalmicadmin.base.Params;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.databinding.AddNewDepartmentDialogBinding;
import app.grand.ophthalmicadmin.databinding.FragmentDepartmentsBinding;

import static android.app.Activity.RESULT_OK;


public class AddNewDepartmentsFragment extends BaseFragment {
    AddNewDepartmentDialogBinding departmentsBinding;
    SettingsViewModels settingsViewModels;


    public AddNewDepartmentsFragment() {
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
        departmentsBinding = DataBindingUtil.inflate(inflater, R.layout.add_new_department_dialog, container, false);
        settingsViewModels = new SettingsViewModels();
        departmentsBinding.setDepartmentViewModel(settingsViewModels);
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
            } else if (result == Codes.SELECT_PROFILE_IMAGE) {
                CropImage.activity()
                        .start(getContext(), this);
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

    // Override onActivityResult method
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            // Get the Uri of data
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                departmentsBinding.uploadedImg.setImageURI(resultUri);
                settingsViewModels.filePath = resultUri;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}
