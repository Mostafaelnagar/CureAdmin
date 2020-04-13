package app.grand.ophthalmicadmin.doctor.reservation.viewModels;


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
import app.grand.ophthalmicadmin.doctor.reservation.adapters.ReservationsAdapter;
import app.grand.ophthalmicadmin.doctor.reservation.adapters.ReservationsDetailsAdapter;
import app.grand.ophthalmicadmin.doctor.reservation.models.ReservationsResponse;

public class ReservationsDetailsViewModels extends BaseViewModel {
    private FirebaseFirestore firebaseFirestore;
    public List<ReservationsResponse> reservationsResponseList = new ArrayList<>();
    private ReservationsDetailsAdapter reservationsAdapter;
    PassingObject passingObject;

    public ReservationsDetailsViewModels() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        passingObject = new PassingObject();
    }

    @BindingAdapter({"app:reservationsAdapter"})
    public static void getDoctorsBinding(RecyclerView recyclerView, ReservationsDetailsAdapter reservationsAdapter) {
        recyclerView.setAdapter(reservationsAdapter);
    }

    @Bindable
    public ReservationsDetailsAdapter getReservationsAdapter() {
        return this.reservationsAdapter == null ? this.reservationsAdapter = new ReservationsDetailsAdapter() : this.reservationsAdapter;
    }

    public PassingObject getPassingObject() {
        return passingObject;
    }

    @Bindable
    public void setPassingObject(PassingObject passingObject) {
        notifyPropertyChanged(app.grand.ophthalmicadmin.BR.passingObject);
        this.passingObject = passingObject;
    }

    public void getReservations() {
        accessLoadingBar(View.VISIBLE);
        Query query = firebaseFirestore.collection("Reservations")
                .whereEqualTo("doctor_id", UserPreferenceHelper.getInstance(MyApplication.getInstance()).getUserData().getId())
                .whereEqualTo("status", "accept").whereEqualTo("date", getPassingObject().getObject());
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

                        getReservationsAdapter().updateData(reservationsResponseList);
                    }
                }
            }
        });

    }

}
