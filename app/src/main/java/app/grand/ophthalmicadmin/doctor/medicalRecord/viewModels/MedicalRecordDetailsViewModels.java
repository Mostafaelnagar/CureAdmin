package app.grand.ophthalmicadmin.doctor.medicalRecord.viewModels;


import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import app.grand.ophthalmicadmin.BR;
import app.grand.ophthalmicadmin.PassingObject;
import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.base.BaseViewModel;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.doctor.models.XRays;
import app.grand.ophthalmicadmin.doctor.reservation.models.ReservationsResponse;


public class MedicalRecordDetailsViewModels extends BaseViewModel {
    private PassingObject passingObject;
    private ReservationsResponse reservationsResponse;
    private FirebaseFirestore db;
    private XRays rays;

    public MedicalRecordDetailsViewModels() {
        db = FirebaseFirestore.getInstance();
        rays = new XRays();
        passingObject = new PassingObject();
        reservationsResponse = new ReservationsResponse();
    }

    public ReservationsResponse getReservationsResponse() {
        return reservationsResponse;
    }

    public void setReservationsResponse(ReservationsResponse reservationsResponse) {
        this.reservationsResponse = reservationsResponse;
    }

    public PassingObject getPassingObject() {
        return passingObject;
    }

    @Bindable
    public void setPassingObject(PassingObject passingObject) {
        setReservationsResponse(passingObject.getReservationsResponse());
        notifyPropertyChanged(app.grand.ophthalmicadmin.BR.passingObject);
        this.passingObject = passingObject;
    }

    public XRays getRays() {
        return rays;
    }

    public void setRays(XRays rays) {
        this.rays = rays;
    }

    public void getPatientRays() {
        accessLoadingBar(View.VISIBLE);
        DocumentReference docRef = db.collection("X-Rays").document(getReservationsResponse().getDoc_id());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                accessLoadingBar(View.GONE);
                setRays(documentSnapshot.toObject(XRays.class));
                notifyChange();
            }
        });
    }

    @BindingAdapter({"rayImage"})
    public static void loadImage(ImageView view, String countryImage) {
        if (!TextUtils.isEmpty(countryImage))
            Picasso.get().load(countryImage).placeholder(R.color.overlayBackground).into(view);
    }

    public void toBack() {
        getClicksMutableLiveData().setValue(Codes.BACK);
    }
}
