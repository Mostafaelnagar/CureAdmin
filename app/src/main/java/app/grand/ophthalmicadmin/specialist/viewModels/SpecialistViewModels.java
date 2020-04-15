package app.grand.ophthalmicadmin.specialist.viewModels;


import app.grand.ophthalmicadmin.base.BaseViewModel;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;

public class SpecialistViewModels extends BaseViewModel {
    public void toReservations() {
        getClicksMutableLiveData().setValue(Codes.Specialist_RESERVATIONS);
    }

    public void toProfile() {
        getClicksMutableLiveData().setValue(Codes.Specialist_PROFILE);
    }

    public void toLogout() {
        getClicksMutableLiveData().setValue(Codes.LOG_OUT);
    }

    public void toNotifications() {
        getClicksMutableLiveData().setValue(Codes.ADMIN_NOTIFICATIONS);
    }
}
