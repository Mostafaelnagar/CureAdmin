package app.grand.ophthalmicadmin.admin.viewModels;


import app.grand.ophthalmicadmin.base.BaseViewModel;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;

public class AdminViewModels extends BaseViewModel {
    public void toReservations() {
        getClicksMutableLiveData().setValue(Codes.ADMIN_RESERVATIONS);
    }

    public void toDepartments() {
        getClicksMutableLiveData().setValue(Codes.ADMIN_DEPARTAMENTS);
    }

    public void toSpecialist() {
        getClicksMutableLiveData().setValue(Codes.ADMIN_SEPIALIST);
    }

    public void toDoctors() {
        getClicksMutableLiveData().setValue(Codes.ADMIN_DOCTORS);
    }

    public void toNotifications() {
        getClicksMutableLiveData().setValue(Codes.ADMIN_NOTIFICATIONS);
    }

    public void toProfile() {
        getClicksMutableLiveData().setValue(Codes.Specialist_PROFILE);
    }
}
