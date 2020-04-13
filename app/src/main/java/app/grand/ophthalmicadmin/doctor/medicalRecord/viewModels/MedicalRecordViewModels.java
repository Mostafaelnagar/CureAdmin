package app.grand.ophthalmicadmin.doctor.medicalRecord.viewModels;


import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import app.grand.ophthalmicadmin.BR;
import app.grand.ophthalmicadmin.PassingObject;
import app.grand.ophthalmicadmin.base.BaseViewModel;
import app.grand.ophthalmicadmin.base.MyApplication;
import app.grand.ophthalmicadmin.base.UserPreferenceHelper;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.doctor.medicalRecord.adapters.MedicalAdapter;
import app.grand.ophthalmicadmin.doctor.reservation.models.ReservationsResponse;


public class MedicalRecordViewModels extends BaseViewModel {
    private FirebaseFirestore firebaseFirestore;
    public List<ReservationsResponse> reservationsResponseList = new ArrayList<>();
    private MedicalAdapter medicalAdapter;
    private PassingObject passingObject;

    public MedicalRecordViewModels() {
        passingObject = new PassingObject();
        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    public PassingObject getPassingObject() {
        return passingObject;
    }

    @Bindable
    public void setPassingObject(PassingObject passingObject) {
        notifyPropertyChanged(app.grand.ophthalmicadmin.BR.passingObject);
        this.passingObject = passingObject;
    }

    @BindingAdapter({"app:medicalAdapter"})
    public static void getDoctorsBinding(RecyclerView recyclerView, MedicalAdapter medicalAdapter) {
        recyclerView.setAdapter(medicalAdapter);
    }

    @Bindable
    public MedicalAdapter getMedicalAdapter() {
        return this.medicalAdapter == null ? this.medicalAdapter = new MedicalAdapter() : this.medicalAdapter;
    }

    public void getMedicalRecords() {
        accessLoadingBar(View.VISIBLE);
        Query query = firebaseFirestore.collection("Reservations").whereEqualTo("user_id", getPassingObject().getObject());
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {

                } else {
                    if (queryDocumentSnapshots.isEmpty()) {
                        accessLoadingBar(View.GONE);
                    } else {
                        accessLoadingBar(View.GONE);
                        for (final DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                ReservationsResponse object = doc.getDocument().toObject(ReservationsResponse.class);
                                object.setDoc_id(doc.getDocument().getId());
                                reservationsResponseList.add(object);
                            }
                        }
                        getMedicalAdapter().updateData(reservationsResponseList);
                    }
                }
            }
        });

    }

    public void toBack() {
        getClicksMutableLiveData().setValue(Codes.BACK);
    }
}
