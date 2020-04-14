package app.grand.ophthalmicadmin.admin.department;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.admin.department.viewModels.SettingsViewModels;
import app.grand.ophthalmicadmin.base.BaseFragment;
import app.grand.ophthalmicadmin.base.MovementManager;
import app.grand.ophthalmicadmin.base.Params;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.databinding.FragmentDepartmentsBinding;


public class DepartmentsFragment extends BaseFragment {
    FragmentDepartmentsBinding departmentsBinding;
    SettingsViewModels settingsViewModels;


    public DepartmentsFragment() {
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
        departmentsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_departments, container, false);
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
            } else if (result == Codes.ADD_NEW_DEPARTMENT) {
                MovementManager.startActivity(getActivity(), result);
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

    @Override
    public void onResume() {
        super.onResume();
        if (Params.DATA_CHANGE) {
            settingsViewModels.getDepartmentmodelList().clear();
            settingsViewModels.getDepartments();
            Params.DATA_CHANGE = false;
        }
    }
}
