package app.grand.ophthalmicadmin.admin.adminSepcialist.viewModels;


import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.grand.ophthalmicadmin.PassingObject;
import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.admin.adminSepcialist.adapters.AdminSpecialistAdapter;
import app.grand.ophthalmicadmin.admin.adminSepcialist.models.AddSpecialistRequest;
import app.grand.ophthalmicadmin.admin.doctors.adapters.AdminDoctorsAdapter;
import app.grand.ophthalmicadmin.admin.doctors.models.AddDoctorRequest;
import app.grand.ophthalmicadmin.admin.doctors.models.AdminDoctorProfileResponse;
import app.grand.ophthalmicadmin.admin.doctors.models.DepartmentModel;
import app.grand.ophthalmicadmin.auth.model.UserData;
import app.grand.ophthalmicadmin.base.BaseViewModel;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;


public class AdminSpecialistViewModels extends BaseViewModel {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    public List<UserData> specialistUserDataList = new ArrayList<>();
    private AdminSpecialistAdapter adminSpecialistAdapter;
    private AddSpecialistRequest addSpecialistRequest;
    public Uri filePath;
    private StorageReference storageReference;

    public AdminSpecialistViewModels() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        addSpecialistRequest = new AddSpecialistRequest();
    }


    public void getSpecialist() {
        accessLoadingBar(View.VISIBLE);
        Query query = firebaseFirestore.collection("Users")
                .whereEqualTo("type", "2");
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
                                UserData object = doc.getDocument().toObject(UserData.class);
                                object.setDocId(doc.getDocument().getId());
                                specialistUserDataList.add(object);
                            }
                        }

                        getAdminSpecialistAdapter().updateData(specialistUserDataList);
                        notifyChange();
                    }
                }
            }
        });

    }


    //add new doctor actions
    public void checkErrors() {
        getClicksMutableLiveData().setValue(Codes.CHECK_ERRORS);
    }

    public void addNewSpecialist() {
        accessLoadingBar(View.VISIBLE);
        auth.createUserWithEmailAndPassword(getAddSpecialistRequest().getEmail(), getAddSpecialistRequest().getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                accessLoadingBar(View.GONE);
                if (task.isSuccessful()) {
                    String user_Id = auth.getCurrentUser().getUid();
                    updateProfileImage(user_Id);
                } else {
                    setReturnedMessage(task.getException().getMessage());
                }
            }
        });
    }

    public void updateProfileImage(String userId) {
        if (filePath != null) {

            accessLoadingBar(View.VISIBLE);
            StorageReference ref
                    = storageReference
                    .child(
                            "profile/"
                                    + userId);
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
                                Log.e("then", "then: " + task.getResult().toString());
                                updateUerImage(task.getResult().toString());
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

    private void updateUerImage(String imgUrl) {
        if (!TextUtils.isEmpty(imgUrl)) {
            //create hashMap to store name and image of user
            Map<String, String> doctor_map = new HashMap<>();
            doctor_map.put("user_name", getAddSpecialistRequest().getUser_name());
            doctor_map.put("type", "2");
            doctor_map.put("phone", getAddSpecialistRequest().getPhone());
            doctor_map.put("image", imgUrl);
            //create collection (users table ) and insert user id and Map
            firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).set(doctor_map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        accessLoadingBar(View.GONE);
                        Map<String, Object> doctor_details = new HashMap<>();
                        doctor_details.put("lab_name", getAddSpecialistRequest().getLabName());
                        doctor_details.put("lab_number", getAddSpecialistRequest().getLabNumber());

                        firebaseFirestore.collection("Specialist_details").document(auth.getCurrentUser().getUid()).set(doctor_details).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                setReturnedMessage("Added Successfully");
                                getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_SUCCESS);
                            }
                        });
                    } else {
                        setReturnedMessage(task.getException().getMessage());
                        getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_ERROR);
                    }
                    accessLoadingBar(View.GONE);

                }
            });
        }
    }

    @BindingAdapter({"app:adminSpecialistAdapter"})
    public static void getDoctorsBinding(RecyclerView recyclerView, AdminSpecialistAdapter adminSpecialistAdapter) {
        recyclerView.setAdapter(adminSpecialistAdapter);
    }

    @Bindable
    public AdminSpecialistAdapter getAdminSpecialistAdapter() {
        return this.adminSpecialistAdapter == null ? this.adminSpecialistAdapter = new AdminSpecialistAdapter() : this.adminSpecialistAdapter;
    }


    public void toAddSpecialist() {
        getClicksMutableLiveData().setValue(Codes.ADD_SPECIALIST);
    }

    public void selectImage() {
        getClicksMutableLiveData().setValue(Codes.SELECT_PROFILE_IMAGE);
    }

    public AddSpecialistRequest getAddSpecialistRequest() {
        return addSpecialistRequest;
    }
}
