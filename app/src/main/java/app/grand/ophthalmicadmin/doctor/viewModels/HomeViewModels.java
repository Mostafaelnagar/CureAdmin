package app.grand.ophthalmicadmin.doctor.viewModels;


import app.grand.ophthalmicadmin.base.BaseViewModel;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;

public class HomeViewModels extends BaseViewModel {
    public void toReservations() {
        getClicksMutableLiveData().setValue(Codes.DOCTOR_RESERVATIONS);
    }

    public void toPost() {
        getClicksMutableLiveData().setValue(Codes.DOCTOR_POSTS);
    }

    public void toProfile() {
        getClicksMutableLiveData().setValue(Codes.DOCTOR_PROFILE);
    }

    public void toLogout() {
        getClicksMutableLiveData().setValue(Codes.LOG_OUT);
    }
    public void toNotifications() {
        getClicksMutableLiveData().setValue(Codes.ADMIN_NOTIFICATIONS);
    }

}
