package app.grand.ophthalmicadmin.specialist.reservations.itemViewModels;

import androidx.databinding.Bindable;

import app.grand.ophthalmicadmin.base.BaseViewModel;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.doctor.models.XRays;
import app.grand.ophthalmicadmin.doctor.reservation.models.ReservationsResponse;


public class SpecialistReservationsItemViewModel extends BaseViewModel {
    private XRays reserveModel;

    public SpecialistReservationsItemViewModel(XRays reserveModel) {
        this.reserveModel = reserveModel;
    }


    @Bindable
    public XRays getReserveModel() {
        return reserveModel;
    }

    public void toReservations() {
        getClicksMutableLiveData().setValue(Codes.MEDICAL_RECORD);
    }

    public void toUpdateRayStatus() {
        getClicksMutableLiveData().setValue(Codes.Specialist_UPDTAE_IMAGE);
    }

}
