package app.grand.ophthalmicadmin.admin.doctors.viewModels;


import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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
import app.grand.ophthalmicadmin.admin.adminReservations.adapters.AdminReservationsAdapter;
import app.grand.ophthalmicadmin.admin.doctors.adapters.AdminDoctorsAdapter;
import app.grand.ophthalmicadmin.admin.doctors.models.AddDoctorRequest;
import app.grand.ophthalmicadmin.admin.doctors.models.AdminDoctorProfileResponse;
import app.grand.ophthalmicadmin.admin.doctors.models.DepartmentModel;
import app.grand.ophthalmicadmin.auth.model.UserData;
import app.grand.ophthalmicadmin.base.BaseViewModel;
import app.grand.ophthalmicadmin.base.MyApplication;
import app.grand.ophthalmicadmin.base.UserPreferenceHelper;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.doctor.reservation.models.ReservationsResponse;


public class AdminDoctorsViewModels extends BaseViewModel {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    public List<UserData> doctorDataList = new ArrayList<>();
    private AdminDoctorsAdapter adminDoctorsAdapter;
    private AddDoctorRequest addDoctorRequest;
    private List<DepartmentModel> departmentList;
    public Uri filePath;
    private StorageReference storageReference;
    private List<String> workingDays = new ArrayList<>();
    private PassingObject passingObject;
    private AdminDoctorProfileResponse profileResponse;

    public AdminDoctorsViewModels() {
        profileResponse = new AdminDoctorProfileResponse();
        passingObject = new PassingObject();
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        addDoctorRequest = new AddDoctorRequest();
    }


    public void getDoctorProfile() {
        accessLoadingBar(View.VISIBLE);
        firebaseFirestore.collection("Doctor_details").document(passingObject.getObjectClass().getDocId())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        getProfileResponse().setDegree(document.getString("degree"));
                        getProfileResponse().setDepartment_id(document.getString("department_id"));
                        getProfileResponse().setPatient_number(document.getString("patient_number"));
                        getProfileResponse().setWorking_days(document.getString("working_days"));
                        firebaseFirestore.collection("Departments").document(getProfileResponse().getDepartment_id())
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                accessLoadingBar(View.GONE);
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        getProfileResponse().setDepartment(document.getString("name"));

                                    } else {
                                        setReturnedMessage("Department Does not exists");
                                        getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_ERROR);
                                    }
                                } else {
                                    setReturnedMessage(task.getException().getMessage());
                                    getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_ERROR);
                                }
                                notifyChange();
                            }
                        });

                    } else {
                        setReturnedMessage("User Does not exists");
                        getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_ERROR);
                    }
                } else {
                    setReturnedMessage(task.getException().getMessage());
                    getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_ERROR);
                }

            }
        });

    }

    public void getDoctors() {
        accessLoadingBar(View.VISIBLE);
        Query query = firebaseFirestore.collection("Users")
                .whereEqualTo("type", "1");
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
                                doctorDataList.add(object);
                            }
                        }

                        getAdminDoctorsAdapter().updateData(doctorDataList);
                        notifyChange();
                    }
                }
            }
        });

    }


    public void getDepartment() {
        departmentList = new ArrayList<>();
        //get departments
        accessLoadingBar(View.VISIBLE);
        firebaseFirestore.collection("Departments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        accessLoadingBar(View.GONE);
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DepartmentModel departmentmodel = document.toObject(DepartmentModel.class);
                                departmentList.add(departmentmodel);
                            }
                        } else {
                            Log.w("df", "Error getting documents.", task.getException());
                        }
                    }
                });

    }


    //add new doctor actions
    public void checkErrors() {
        getClicksMutableLiveData().setValue(Codes.CHECK_ERRORS);
    }

    public void addNewDoctor() {
        accessLoadingBar(View.VISIBLE);
        auth.createUserWithEmailAndPassword(getAddDoctorRequest().getEmail(), getAddDoctorRequest().getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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
            doctor_map.put("user_name", getAddDoctorRequest().getUser_name());
            doctor_map.put("type", "1");
            doctor_map.put("phone", getAddDoctorRequest().getPhone());
            doctor_map.put("image", imgUrl);
            //create collection (users table ) and insert user id and Map
            firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).set(doctor_map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        accessLoadingBar(View.GONE);
                        Map<String, Object> doctor_details = new HashMap<>();
                        doctor_details.put("department_id", getAddDoctorRequest().getDepartment());
                        doctor_details.put("patient_number", getAddDoctorRequest().getPatientNumber());
                        doctor_details.put("degree", getAddDoctorRequest().getDegree());
                        String days = workingDays.toString();
                        days = days.replace("]", "");
                        days = days.replace("[", "");
                        doctor_details.put("working_days", days);

                        firebaseFirestore.collection("Doctor_details").document(auth.getCurrentUser().getUid()).set(doctor_details).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    @BindingAdapter({"app:adminDoctorsAdapter"})
    public static void getDoctorsBinding(RecyclerView recyclerView, AdminDoctorsAdapter adminDoctorsAdapter) {
        recyclerView.setAdapter(adminDoctorsAdapter);
    }

    @Bindable
    public AdminDoctorsAdapter getAdminDoctorsAdapter() {
        return this.adminDoctorsAdapter == null ? this.adminDoctorsAdapter = new AdminDoctorsAdapter() : this.adminDoctorsAdapter;
    }

    @BindingAdapter({"doctorImage"})
    public static void loadImage(ImageView view, String countryImage) {
        if (!countryImage.equals(""))
            Picasso.get().load(countryImage).placeholder(R.color.overlayBackground).into(view);
    }

    public void toDegree() {
        getClicksMutableLiveData().setValue(Codes.DEPARTMENTS);
    }

    public void toAddDoctor() {
        getClicksMutableLiveData().setValue(Codes.ADD_DOCTOR);
    }

    public void selectImage() {
        getClicksMutableLiveData().setValue(Codes.SELECT_PROFILE_IMAGE);
    }

    public AdminDoctorProfileResponse getProfileResponse() {
        return profileResponse;
    }

    public void setProfileResponse(AdminDoctorProfileResponse profileResponse) {
        this.profileResponse = profileResponse;
    }

    public PassingObject getPassingObject() {
        return passingObject;
    }

    @Bindable
    public void setPassingObject(PassingObject passingObject) {
        notifyPropertyChanged(BR.passingObject);
        this.passingObject = passingObject;
    }

    public List<String> getWorkingDays() {
        return workingDays;
    }

    public AddDoctorRequest getAddDoctorRequest() {
        return addDoctorRequest;
    }

    public List<DepartmentModel> getDepartmentList() {
        return departmentList;
    }
}
