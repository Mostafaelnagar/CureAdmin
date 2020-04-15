package app.grand.ophthalmicadmin.admin.adminReservations.viewModels;


import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.grand.ophthalmicadmin.PassingObject;
import app.grand.ophthalmicadmin.admin.adminReservations.adapters.AdminReservationsAdapter;
import app.grand.ophthalmicadmin.base.BaseViewModel;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.doctor.reservation.models.ReservationsResponse;


public class AdminReservationsViewModels extends BaseViewModel {
    private FirebaseFirestore firebaseFirestore;
    public List<ReservationsResponse> reservationsResponseList = new ArrayList<>();
    private AdminReservationsAdapter reservationsAdapter;
    private PassingObject passingObject;

    public AdminReservationsViewModels() {
        passingObject = new PassingObject();
        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    public PassingObject getPassingObject() {
        return passingObject;
    }

    @Bindable
    public void setPassingObject(PassingObject passingObject) {
        notifyPropertyChanged(BR.passingObject);
        this.passingObject = passingObject;
    }

    public void getReservations(String status) {
        accessLoadingBar(View.VISIBLE);
        Query query;
        if (status.equals("")) {
            query = firebaseFirestore.collection("Reservations").whereEqualTo("doctor_id", getPassingObject().getObject());
        } else
            query = firebaseFirestore.collection("Reservations")
                    .whereEqualTo("status", status);

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
                        notifyChange();
                    }
                }
            }
        });

    }

    @BindingAdapter({"app:reservationsAdapter"})
    public static void getDoctorsBinding(RecyclerView recyclerView, AdminReservationsAdapter reservationsAdapter) {
        recyclerView.setAdapter(reservationsAdapter);
    }

    @Bindable
    public AdminReservationsAdapter getReservationsAdapter() {
        return this.reservationsAdapter == null ? this.reservationsAdapter = new AdminReservationsAdapter() : this.reservationsAdapter;
    }

    public void rejectReserve(String docId, String rejectReason) {
        accessLoadingBar(View.VISIBLE);
        Map<String, Object> rejectMap = new HashMap<>();
        rejectMap.put("status", "reject");
        firebaseFirestore.collection("Reservations").document(docId).update(rejectMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        addToReject(docId, rejectReason);
                    }
                });
    }

    private void addToReject(String docId, String rejectReason) {
        Map<String, Object> rejectMap = new HashMap<>();
        rejectMap.put("reject_reason", rejectReason);
        firebaseFirestore.collection("Reservations_Reject_Reasons").document(docId).set(rejectMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        accessLoadingBar(View.GONE);
                        saveNotifications(docId, "user", rejectReason);
                        setReturnedMessage("Rejected Successfully");
                        getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_SUCCESS);
                    }
                });

    }

    public void acceptReserve(String docId) {
        accessLoadingBar(View.VISIBLE);
        Map<String, Object> acceptMap = new HashMap<>();
        acceptMap.put("status", "accept");
        firebaseFirestore.collection("Reservations").document(docId).update(acceptMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        accessLoadingBar(View.GONE);
                        saveNotifications(docId, "doctor", null);
                        setReturnedMessage("Accepted Successfully");
                        getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_SUCCESS);
                    }
                });
    }

    private void saveNotifications(String id, String type, @Nullable String reason) {
        Map<String, Object> reserve = new HashMap<>();
        reserve.put("title", "New reservation");
        if (type.equals("doctor")) {
            reserve.put("body", "new reservation from " + getReservationsAdapter().reservationsResponseList.get(getReservationsAdapter().lastIndex).getPatient().getUser_name());
            reserve.put("user_id", getReservationsAdapter().reservationsResponseList.get(getReservationsAdapter().lastIndex).getDoctor().getId());
            reserve.put("to", "doctor");
        } else {
            reserve.put("body", "We are sorry your reservation has been rejected due to " + reason);
            reserve.put("user_id", getReservationsAdapter().reservationsResponseList.get(getReservationsAdapter().lastIndex).getPatient().getId());
            reserve.put("to", "user");
        }
        reserve.put("reserve_id", id);
        DocumentReference documentReference = firebaseFirestore.collection("Notifications").document();
        documentReference.set(reserve).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                } else {
                    accessLoadingBar(View.GONE);
                    setReturnedMessage(task.getException().getMessage());
                    getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_ERROR);
                }
            }
        });
    }
}
