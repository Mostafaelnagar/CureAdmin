package app.grand.ophthalmicadmin.doctor.reservation.viewModels;


import android.util.Log;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import app.grand.ophthalmicadmin.base.BaseViewModel;
import app.grand.ophthalmicadmin.base.MyApplication;
import app.grand.ophthalmicadmin.base.UserPreferenceHelper;
import app.grand.ophthalmicadmin.doctor.reservation.adapters.ReservationsAdapter;
import app.grand.ophthalmicadmin.doctor.reservation.models.ReservationsResponse;

public class ReservationsViewModels extends BaseViewModel {
    private FirebaseFirestore firebaseFirestore;
    public List<ReservationsResponse> reservationsResponseList = new ArrayList<>();
    private ReservationsAdapter reservationsAdapter;

    public ReservationsViewModels() {
        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    @BindingAdapter({"app:reservationsAdapter"})
    public static void getDoctorsBinding(RecyclerView recyclerView, ReservationsAdapter reservationsAdapter) {
        recyclerView.setAdapter(reservationsAdapter);
    }

    @Bindable
    public ReservationsAdapter getReservationsAdapter() {
        return this.reservationsAdapter == null ? this.reservationsAdapter = new ReservationsAdapter() : this.reservationsAdapter;
    }


    public void getReservations() {
        accessLoadingBar(View.VISIBLE);
        Query query = firebaseFirestore.collection("Reservations_Counters")
                .whereEqualTo("doctor_id", UserPreferenceHelper.getInstance(MyApplication.getInstance()).getUserData().getId())
                .whereEqualTo("status", "accept");
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
