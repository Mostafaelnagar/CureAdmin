package app.grand.ophthalmicadmin.specialist.viewModels;


import app.grand.ophthalmicadmin.base.BaseViewModel;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;

public class SpecialistViewModels extends BaseViewModel {
    public void toReservations() {
        getClicksMutableLiveData().setValue(Codes.Specialist_RESERVATIONS);
    }

    public void toPost() {
        getClicksMutableLiveData().setValue(Codes.Specialist_NOTIFICATIONS);
    }

    public void toProfile() {
        getClicksMutableLiveData().setValue(Codes.Specialist_PROFILE);
    }
}
