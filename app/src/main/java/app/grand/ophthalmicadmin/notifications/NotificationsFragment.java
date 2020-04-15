package app.grand.ophthalmicadmin.notifications;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.base.BaseFragment;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.databinding.FragmentNotificationsBinding;
import app.grand.ophthalmicadmin.notifications.viewModels.NotificationsViewModels;


public class NotificationsFragment extends BaseFragment {
    FragmentNotificationsBinding notificationsBinding;
    NotificationsViewModels notificationsViewModels;


    public NotificationsFragment() {
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
        notificationsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_notifications, container, false);
        notificationsViewModels = new NotificationsViewModels();
        notificationsBinding.setNotifyViewModel(notificationsViewModels);
        liveDataListeners();
        checkConnection();
        return notificationsBinding.getRoot();
    }


    private void liveDataListeners() {
        notificationsViewModels.getClicksMutableLiveData().observe(this, result -> {
            if (result == View.VISIBLE || result == View.GONE) {
                accessLoadingBar(result);
            } else if (result == Codes.SHOW_MESSAGE_ERROR) {
                showMessage(notificationsViewModels.getReturnedMessage(), 0, 1);
            } else if (result == Codes.BACK) {
                ((Activity) getActivity()).finish();
            }
        });

        ConnectionLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isConnected) {
                if (isConnected) {
                    notificationsViewModels.getNotifications();
                } else {
                    showMessage(getActivity().getResources().getString(R.string.connection_invaild_msg), 0, 1);
                }
            }
        });

    }

}
