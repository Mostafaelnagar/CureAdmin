package app.grand.ophthalmicadmin.specialist.reservations.viewModels;


import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.grand.ophthalmicadmin.PassingObject;
import app.grand.ophthalmicadmin.base.BaseViewModel;
import app.grand.ophthalmicadmin.base.MyApplication;
import app.grand.ophthalmicadmin.base.UserPreferenceHelper;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.doctor.models.XRays;
import app.grand.ophthalmicadmin.specialist.reservations.adapters.SpecialistReservationsAdapter;

public class SpecialistReservationsViewModels extends BaseViewModel {
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    public List<XRays> reservationsResponseList = new ArrayList<>();
    private SpecialistReservationsAdapter reservationsAdapter;
    private PassingObject passingObject;
    public Uri filePath;
    public String resultDesc, result;

    public SpecialistReservationsViewModels() {
        passingObject = new PassingObject();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public PassingObject getPassingObject() {
        return passingObject;
    }

    @Bindable
    public void setPassingObject(PassingObject passingObject) {
        notifyPropertyChanged(app.grand.ophthalmicadmin.BR.passingObject);
        this.passingObject = passingObject;
    }

    @BindingAdapter({"app:reservationsAdapter"})
    public static void getDoctorsBinding(RecyclerView recyclerView, SpecialistReservationsAdapter reservationsAdapter) {
        recyclerView.setAdapter(reservationsAdapter);
    }

    @Bindable
    public SpecialistReservationsAdapter getReservationsAdapter() {
        return this.reservationsAdapter == null ? this.reservationsAdapter = new SpecialistReservationsAdapter() : this.reservationsAdapter;
    }

    public void selectImage() {
        getClicksMutableLiveData().setValue(Codes.SELECT_PROFILE_IMAGE);
    }

    public void getReservations() {
        accessLoadingBar(View.VISIBLE);
        Query query = firebaseFirestore.collection("X-Rays")
                .whereEqualTo("status", false);
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
                                XRays object = doc.getDocument().toObject(XRays.class);
                                object.setDocId(doc.getDocument().getId());
                                firebaseFirestore.collection("Users").document(object.getPatient_id()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                object.setUser_name(document.getString("user_name"));
                                                object.setImage(document.getString("image"));
                                                object.setPhone(document.getString("phone"));
                                                reservationsResponseList.add(object);
                                                getReservationsAdapter().updateData(reservationsResponseList);
                                            }
                                        }

                                    }
                                });
                            }
                        }
                    }
                }
            }
        });

    }

    public void updateRaysImage() {
        if (filePath != null) {

            accessLoadingBar(View.VISIBLE);
            StorageReference ref
                    = storageReference
                    .child(
                            "rays/"
                                    + getPassingObject().getRaysModel().getDocId());
            final UploadTask uploadTask = ref.putFile(filePath);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    setReturnedMessage(e.getMessage());
                    getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_ERROR);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return ref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                updateRayImage(task.getResult().toString());
                            } else {
                                setReturnedMessage(task.getException().getMessage());
                                getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_ERROR);
                            }
                        }
                    });
                }
            });
        }
    }

    private void updateRayImage(String imgUrl) {
        if (!TextUtils.isEmpty(imgUrl)) {
            //create hashMap to store name and image of user
            Map<String, Object> user_Map = new HashMap<>();
            user_Map.put("x_ray_image", imgUrl);
            user_Map.put("x_ray_desc", resultDesc);
            user_Map.put("x_ray_result", result);
            user_Map.put("status", true);
            //create collection (users table ) and insert user id and Map
            firebaseFirestore.collection("X-Rays").document(getPassingObject().getRaysModel().getDocId()).update(user_Map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        accessLoadingBar(View.GONE);
                        saveNotifications(getPassingObject().getRaysModel().getDocId());
                        setReturnedMessage("Image Updated Successfully");
                        getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_SUCCESS);
                    } else {
                        setReturnedMessage(task.getException().getMessage());
                        getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_ERROR);
                    }
                    accessLoadingBar(View.GONE);

                }
            });
        }
    }

    private void saveNotifications(String id) {
        Map<String, Object> reserve = new HashMap<>();
        reserve.put("title", "X Ray Updated");
        reserve.put("body", "Check your X ray results");
        reserve.put("user_id", getPassingObject().getRaysModel().getPatient_id());
        reserve.put("to", "user");
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
