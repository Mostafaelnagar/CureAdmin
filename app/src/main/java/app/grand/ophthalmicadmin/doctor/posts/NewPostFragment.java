package app.grand.ophthalmicadmin.doctor.posts;


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
import app.grand.ophthalmicadmin.databinding.FragmentNewPostBinding;
import app.grand.ophthalmicadmin.doctor.medicalRecord.viewModels.MedicalRecordDetailsViewModels;
import app.grand.ophthalmicadmin.doctor.posts.viewModels.NewPostViewModels;


public class NewPostFragment extends BaseFragment {
    FragmentNewPostBinding newPostBinding;
    NewPostViewModels newPostViewModels;


    public NewPostFragment() {
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
        newPostBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_post, container, false);
        newPostViewModels = new NewPostViewModels();
        newPostBinding.setPostViewModel(newPostViewModels);

        liveDataListeners();
        checkConnection();
        return newPostBinding.getRoot();
    }


    private void liveDataListeners() {
        newPostViewModels.getClicksMutableLiveData().observe(this, result -> {
            if (result == View.VISIBLE || result == View.GONE) {
                accessLoadingBar(result);
            } else if (result == Codes.SHOW_MESSAGE_ERROR) {
                showMessage(newPostViewModels.getReturnedMessage(), 1, 1);
            } else if (result == Codes.SHOW_MESSAGE_SUCCESS) {
                showMessage(newPostViewModels.getReturnedMessage(), 1, 0);
                newPostViewModels.goBack(getActivity());
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
