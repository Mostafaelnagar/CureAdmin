package app.grand.ophthalmicadmin.admin.adminSepcialist;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.databinding.DataBindingUtil;

import com.theartofdev.edmodo.cropper.CropImage;

import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.admin.adminSepcialist.viewModels.AdminSpecialistViewModels;
import app.grand.ophthalmicadmin.admin.doctors.viewModels.AdminDoctorsViewModels;
import app.grand.ophthalmicadmin.base.BaseFragment;
import app.grand.ophthalmicadmin.base.PopUpMenus;
import app.grand.ophthalmicadmin.base.Validate;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.databinding.FragmentAdminAddDoctorsBinding;
import app.grand.ophthalmicadmin.databinding.FragmentAdminAddSpecialisBinding;

import static android.app.Activity.RESULT_OK;


public class AdminAddSpecialistFragment extends BaseFragment  {
    FragmentAdminAddSpecialisBinding addSpecialisBinding;
    AdminSpecialistViewModels specialistViewModels;


    public AdminAddSpecialistFragment() {
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
        addSpecialisBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin__add_specialis, container, false);
        specialistViewModels = new AdminSpecialistViewModels();
        addSpecialisBinding.setAdminSpecialistViewModel(specialistViewModels);
        liveDataListeners();
         return addSpecialisBinding.getRoot();
    }


    private void liveDataListeners() {
        specialistViewModels.getClicksMutableLiveData().observe(this, result -> {
            if (result == View.VISIBLE || result == View.GONE) {
                accessLoadingBar(result);
            }  else if (result == Codes.SHOW_MESSAGE_SUCCESS) {
                showMessage(specialistViewModels.getReturnedMessage(), 0, 0);
                specialistViewModels.goBack(getActivity());
            } else if (result == Codes.SHOW_MESSAGE_ERROR) {
                showMessage(specialistViewModels.getReturnedMessage(), 0, 1);

            } else if (result == Codes.SELECT_PROFILE_IMAGE) {
                CropImage.activity()
                        .start(getContext(), this);
            } else if (result == Codes.CHECK_ERRORS) {
                if (Validate.isInputValid(addSpecialisBinding.inputUserName, addSpecialisBinding.userName, 3, getActivity())
                        && Validate.isInputValid(addSpecialisBinding.inputEmail, addSpecialisBinding.userEmail, 0, getActivity())
                        && Validate.isInputValid(addSpecialisBinding.inputPhone, addSpecialisBinding.userPhone, 3, getActivity())
                        && Validate.isInputValid(addSpecialisBinding.inputPassword, addSpecialisBinding.userPassword, 1, getActivity())
                        && Validate.isInputValid(addSpecialisBinding.inputLabName, addSpecialisBinding.userLabName, 3, getActivity())
                        && Validate.isInputValid(addSpecialisBinding.inputLabNumber, addSpecialisBinding.userLabNumber, 3, getActivity())
                        ) {
                         if (specialistViewModels.filePath != null)
                             specialistViewModels.addNewSpecialist();
                        else {
                            showMessage("Please choose Image", 0, 1);
                        }

                }
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            // Get the Uri of data
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                addSpecialisBinding.uploadedImg.setImageURI(resultUri);
                specialistViewModels.filePath = resultUri;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}
