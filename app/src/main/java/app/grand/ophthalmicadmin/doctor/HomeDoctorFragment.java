package app.grand.ophthalmicadmin.doctor;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.base.BaseFragment;
import app.grand.ophthalmicadmin.base.MovementManager;
import app.grand.ophthalmicadmin.base.UserPreferenceHelper;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.databinding.FragmentHomeDoctorBinding;
import app.grand.ophthalmicadmin.doctor.viewModels.HomeViewModels;


public class HomeDoctorFragment extends BaseFragment {
    FragmentHomeDoctorBinding doctorBinding;
    HomeViewModels homeViewModels;


    public HomeDoctorFragment() {
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
        doctorBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_doctor, container, false);
        homeViewModels = new HomeViewModels();
        doctorBinding.setHomeViewModel(homeViewModels);
        liveDataListeners();
        return doctorBinding.getRoot();
    }


    private void liveDataListeners() {
        homeViewModels.getClicksMutableLiveData().observe(this, result -> {
            if (result == View.VISIBLE) {
                accessLoadingBar(result);
            } else if (result == Codes.SHOW_MESSAGE_ERROR) {
                showMessage(homeViewModels.getReturnedMessage(), 1, 1);
            } else if (result == Codes.DOCTOR_RESERVATIONS) {
                MovementManager.startActivity(getActivity(), result);
            } else if (result == Codes.DOCTOR_POSTS) {
                MovementManager.startActivity(getActivity(), result);
            } else if (result == Codes.DOCTOR_PROFILE) {
                MovementManager.startActivity(getActivity(), result);
            } else if (result == Codes.LOG_OUT) {
                logOut();
            }else if (result == Codes.ADMIN_NOTIFICATIONS) {
                MovementManager.startActivity(getActivity(), result);
            }
        });

    }

    private void logOut() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        Map<String, Object> map = new HashMap<>();
        map.put("token", FieldValue.delete());
        accessLoadingBar(View.VISIBLE);
        firebaseFirestore.collection("Users").document(UserPreferenceHelper.getInstance(getActivity()).getUserData().getId()).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                accessLoadingBar(View.GONE);
                firebaseAuth.signOut();
                MovementManager.loggout(getActivity());
            }
        });

    }
}
