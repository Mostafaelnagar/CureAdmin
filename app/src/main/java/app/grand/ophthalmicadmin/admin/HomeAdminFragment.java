package app.grand.ophthalmicadmin.admin;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.admin.viewModels.AdminViewModels;
import app.grand.ophthalmicadmin.base.BaseFragment;
import app.grand.ophthalmicadmin.base.MovementManager;
import app.grand.ophthalmicadmin.base.UserPreferenceHelper;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.databinding.FragmentHomeAdminBinding;
import app.grand.ophthalmicadmin.databinding.FragmentHomeSpecialistBinding;
import app.grand.ophthalmicadmin.specialist.viewModels.SpecialistViewModels;


public class HomeAdminFragment extends BaseFragment {
    FragmentHomeAdminBinding adminBinding;
    AdminViewModels adminViewModels;


    public HomeAdminFragment() {
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
        adminBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_admin, container, false);
        adminViewModels = new AdminViewModels();
        adminBinding.setHomeViewModel(adminViewModels);
        liveDataListeners();
        return adminBinding.getRoot();
    }


    private void liveDataListeners() {
        adminViewModels.getClicksMutableLiveData().observe(this, result -> {
            if (result == View.VISIBLE) {
                accessLoadingBar(result);
            } else if (result == Codes.SHOW_MESSAGE_ERROR) {
                showMessage(adminViewModels.getReturnedMessage(), 1, 1);
            } else if (result == Codes.ADMIN_RESERVATIONS) {
                MovementManager.startActivity(getActivity(), result);
            } else if (result == Codes.ADMIN_DEPARTAMENTS) {
                MovementManager.startActivity(getActivity(), result);
            } else if (result == Codes.ADMIN_SEPIALIST) {
                MovementManager.startActivity(getActivity(), result);
            } else if (result == Codes.ADMIN_DOCTORS) {
                MovementManager.startActivity(getActivity(), result);
            } else if (result == Codes.ADMIN_NOTIFICATIONS) {
                MovementManager.startActivity(getActivity(), result);
            } else if (result == Codes.ADMIN_PROFILE) {
                MovementManager.startActivity(getActivity(), result);
            }else if (result == Codes.LOG_OUT) {
                logOut();
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
