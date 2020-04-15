package app.grand.ophthalmicadmin.notifications.viewModels;


import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import app.grand.ophthalmicadmin.base.BaseViewModel;
import app.grand.ophthalmicadmin.base.MyApplication;
import app.grand.ophthalmicadmin.base.UserPreferenceHelper;
import app.grand.ophthalmicadmin.doctor.reservation.models.ReservationsResponse;
import app.grand.ophthalmicadmin.notifications.adapters.NotificationsAdapter;
import app.grand.ophthalmicadmin.notifications.models.NotificationsData;


public class NotificationsViewModels extends BaseViewModel {
    private FirebaseFirestore firebaseFirestore;
    NotificationsAdapter notificationsAdapter;
    List<NotificationsData> notificationsData = new ArrayList<>();

    public NotificationsViewModels() {
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public List<NotificationsData> getNotificationsData() {
        return notificationsData;
    }

    public void setNotificationsData(List<NotificationsData> notificationsData) {
        this.notificationsData = notificationsData;
    }

    public void getNotifications() {
        accessLoadingBar(View.VISIBLE);
        Query query;
        if (UserPreferenceHelper.getInstance(MyApplication.getInstance()).getUserData().getType().equals("3")) {
            query = firebaseFirestore.collection("Notifications")
                    .whereEqualTo("to", "manager");
        } else if (UserPreferenceHelper.getInstance(MyApplication.getInstance()).getUserData().getType().equals("2")) {
            query = firebaseFirestore.collection("Notifications")
                    .whereEqualTo("to", "special");
        } else {
            query = firebaseFirestore.collection("Notifications")
                    .whereEqualTo("user_id", UserPreferenceHelper.getInstance(MyApplication.getInstance()).getUserData().getId());

        }
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
                                NotificationsData object = doc.getDocument().toObject(NotificationsData.class);
                                notificationsData.add(object);
                            }
                        }
                        getNotificationsAdapter().updateData(notificationsData);
                        notifyChange();
                    }
                }
            }
        });
    }

    @BindingAdapter({"app:notificationsAdapter"})
    public static void getCatBinding(RecyclerView recyclerView, NotificationsAdapter notificationsAdapter) {
        recyclerView.setAdapter(notificationsAdapter);

    }

    @Bindable
    public NotificationsAdapter getNotificationsAdapter() {
        return this.notificationsAdapter == null ? this.notificationsAdapter = new NotificationsAdapter() : this.notificationsAdapter;
    }


}
