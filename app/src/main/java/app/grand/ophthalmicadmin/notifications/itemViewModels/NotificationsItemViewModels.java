package app.grand.ophthalmicadmin.notifications.itemViewModels;

import android.widget.ImageView;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;

import app.grand.ophthalmicadmin.base.BaseViewModel;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.notifications.models.NotificationsData;


public class NotificationsItemViewModels extends BaseViewModel {
    private NotificationsData notificationsData;

    public NotificationsItemViewModels(NotificationsData notificationsData) {
        this.notificationsData = notificationsData;
    }

    @Bindable
    public NotificationsData getNotificationsData() {
        return notificationsData;
    }


    public void itemAction() {
        if (notificationsData.getTo().equals("manager")) {
            getClicksMutableLiveData().setValue(Codes.ADMIN_RESERVATIONS);
        } else if (notificationsData.getTo().equals("doctor")) {
            getClicksMutableLiveData().setValue(Codes.DOCTOR_RESERVATIONS);
        } else {
            getClicksMutableLiveData().setValue(Codes.Specialist_RESERVATIONS);
        }
    }


}
