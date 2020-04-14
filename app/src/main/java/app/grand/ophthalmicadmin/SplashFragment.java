package app.grand.ophthalmicadmin;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import app.grand.ophthalmicadmin.base.BaseFragment;
import app.grand.ophthalmicadmin.base.MovementManager;
import app.grand.ophthalmicadmin.base.UserPreferenceHelper;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.databinding.FragmentSplashBinding;


public class SplashFragment extends BaseFragment {
    FragmentSplashBinding homeBinding;
    SplashScreenViewModel screenViewModel;


    public SplashFragment() {
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


        homeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false);
        screenViewModel = new SplashScreenViewModel();
        homeBinding.setSplashScreenViewModel(screenViewModel);
        liveDataListeners();
        return homeBinding.getRoot();
    }


    private void liveDataListeners() {
        screenViewModel.getClicksMutableLiveData().observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        if (UserPreferenceHelper.getInstance(getActivity()).getUserData() != null) {
                            if (UserPreferenceHelper.getInstance(getActivity()).getUserData().getType().equals("1"))
                                MovementManager.startBaseActivity(getActivity(), Codes.DOCTOR_HOME);
                            else if (UserPreferenceHelper.getInstance(getActivity()).getUserData().getType().equals("3"))
                                MovementManager.startBaseActivity(getActivity(), Codes.ADMIN_HOME);
                            else if (UserPreferenceHelper.getInstance(getActivity()).getUserData().getType().equals("2"))
                                MovementManager.startBaseActivity(getActivity(), Codes.Specialist_HOME);
                        } else
                            MovementManager.startBaseActivity(getActivity(), Codes.LOGIN_SCREEN);
                    }
                }
        );
    }


}
