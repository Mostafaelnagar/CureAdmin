package app.grand.ophthalmicadmin.admin.doctors;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.List;

import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.admin.doctors.viewModels.AdminDoctorsViewModels;
import app.grand.ophthalmicadmin.base.BaseFragment;
import app.grand.ophthalmicadmin.base.PopUpMenus;
import app.grand.ophthalmicadmin.base.Validate;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.databinding.FragmentAdminAddDoctorsBinding;
import app.grand.ophthalmicadmin.databinding.FragmentAdminDoctorsBinding;

import static android.app.Activity.RESULT_OK;


public class AdminAddDoctorsFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {
    FragmentAdminAddDoctorsBinding adminDoctorsBinding;
    AdminDoctorsViewModels adminDoctorsViewModels;


    public AdminAddDoctorsFragment() {
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
        adminDoctorsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin__add_doctors, container, false);
        adminDoctorsViewModels = new AdminDoctorsViewModels();
        adminDoctorsBinding.setAdminDoctorsViewModel(adminDoctorsViewModels);
        adminDoctorsBinding.chFriday.setOnCheckedChangeListener(this);
        adminDoctorsBinding.chThursday.setOnCheckedChangeListener(this);
        adminDoctorsBinding.chWednesday.setOnCheckedChangeListener(this);
        adminDoctorsBinding.chTuesday.setOnCheckedChangeListener(this);
        adminDoctorsBinding.chMonday.setOnCheckedChangeListener(this);
        adminDoctorsBinding.chSunday.setOnCheckedChangeListener(this);
        adminDoctorsBinding.chSaturday.setOnCheckedChangeListener(this);
        liveDataListeners();
        adminDoctorsViewModels.getDepartment();
        return adminDoctorsBinding.getRoot();
    }


    private void liveDataListeners() {
        adminDoctorsViewModels.getClicksMutableLiveData().observe(this, result -> {
            if (result == View.VISIBLE || result == View.GONE) {
                accessLoadingBar(result);
            } else if (result == Codes.DEPARTMENTS) {
                showDepartment();
            } else if (result == Codes.SHOW_MESSAGE_SUCCESS) {
                showMessage(adminDoctorsViewModels.getReturnedMessage(), 0, 0);
                adminDoctorsViewModels.goBack(getActivity());
            } else if (result == Codes.SHOW_MESSAGE_ERROR) {
                showMessage(adminDoctorsViewModels.getReturnedMessage(), 0, 1);

            } else if (result == Codes.SELECT_PROFILE_IMAGE) {
                CropImage.activity()
                        .start(getContext(), this);
            } else if (result == Codes.CHECK_ERRORS) {
                if (Validate.isInputValid(adminDoctorsBinding.inputUserName, adminDoctorsBinding.userName, 3, getActivity())
                        && Validate.isInputValid(adminDoctorsBinding.inputEmail, adminDoctorsBinding.userEmail, 0, getActivity())
                        && Validate.isInputValid(adminDoctorsBinding.inputPhone, adminDoctorsBinding.userPhone, 3, getActivity())
                        && Validate.isInputValid(adminDoctorsBinding.inputPassword, adminDoctorsBinding.userPassword, 1, getActivity())
                        && Validate.isInputValid(adminDoctorsBinding.inputDepartment, adminDoctorsBinding.userDepartment, 3, getActivity())
                        && Validate.isInputValid(adminDoctorsBinding.inputPatient, adminDoctorsBinding.userPatient, 3, getActivity())
                        && Validate.isInputValid(adminDoctorsBinding.inputDegree, adminDoctorsBinding.userDegree, 3, getActivity())) {
                    if (adminDoctorsViewModels.getWorkingDays().size() > 0)
                        if (adminDoctorsViewModels.filePath != null)
                            adminDoctorsViewModels.addNewDoctor();
                        else {
                            showMessage("Please choose Image", 0, 1);
                        }
                    else {
                        showMessage("Please choose at least one day", 0, 1);
                    }
                }
            }
        });
    }

    private void showDepartment() {
        PopUpMenus.showDepartment(getActivity(), adminDoctorsBinding.userDepartment, adminDoctorsViewModels.getDepartmentList()).
                setOnMenuItemClickListener(item -> {
                    adminDoctorsViewModels.getAddDoctorRequest().setDepartment(adminDoctorsViewModels.getDepartmentList().get(item.getItemId()).getId());
                    adminDoctorsBinding.userDepartment.setText(adminDoctorsViewModels.getDepartmentList().get(item.getItemId()).getName());
                    return false;
                });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.ch_Saturday:
                if (isChecked)
                    adminDoctorsViewModels.getWorkingDays().add(adminDoctorsBinding.chSaturday.getText().toString());
                else
                    adminDoctorsViewModels.getWorkingDays().remove(adminDoctorsBinding.chSaturday.getText().toString());
                break;
            case R.id.ch_Sunday:
                if (isChecked)
                    adminDoctorsViewModels.getWorkingDays().add(adminDoctorsBinding.chSunday.getText().toString());
                else
                    adminDoctorsViewModels.getWorkingDays().remove(adminDoctorsBinding.chSunday.getText().toString());
                break;
            case R.id.ch_Monday:
                if (isChecked)
                    adminDoctorsViewModels.getWorkingDays().add(adminDoctorsBinding.chMonday.getText().toString());
                else
                    adminDoctorsViewModels.getWorkingDays().remove(adminDoctorsBinding.chMonday.getText().toString());
                break;
            case R.id.ch_Tuesday:
                if (isChecked)
                    adminDoctorsViewModels.getWorkingDays().add(adminDoctorsBinding.chTuesday.getText().toString());
                else
                    adminDoctorsViewModels.getWorkingDays().remove(adminDoctorsBinding.chTuesday.getText().toString());
                break;
            case R.id.ch_Wednesday:
                if (isChecked)
                    adminDoctorsViewModels.getWorkingDays().add(adminDoctorsBinding.chWednesday.getText().toString());
                else
                    adminDoctorsViewModels.getWorkingDays().remove(adminDoctorsBinding.chWednesday.getText().toString());
                break;
            case R.id.ch_Thursday:
                if (isChecked)
                    adminDoctorsViewModels.getWorkingDays().add(adminDoctorsBinding.chThursday.getText().toString());
                else
                    adminDoctorsViewModels.getWorkingDays().remove(adminDoctorsBinding.chThursday.getText().toString());
                break;
            case R.id.ch_Friday:
                if (isChecked)
                    adminDoctorsViewModels.getWorkingDays().add(adminDoctorsBinding.chFriday.getText().toString());
                else
                    adminDoctorsViewModels.getWorkingDays().remove(adminDoctorsBinding.chFriday.getText().toString());
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            // Get the Uri of data
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                adminDoctorsBinding.uploadedImg.setImageURI(resultUri);
                adminDoctorsViewModels.filePath = resultUri;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}
