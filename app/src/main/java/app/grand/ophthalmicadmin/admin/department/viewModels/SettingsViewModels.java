package app.grand.ophthalmicadmin.admin.department.viewModels;


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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.grand.ophthalmicadmin.BR;
import app.grand.ophthalmicadmin.PassingObject;
import app.grand.ophthalmicadmin.admin.department.adapters.DepartmentAdapter;
import app.grand.ophthalmicadmin.admin.department.models.AddDepartmentRequest;
import app.grand.ophthalmicadmin.admin.doctors.models.DepartmentModel;
import app.grand.ophthalmicadmin.base.BaseViewModel;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;


public class SettingsViewModels extends BaseViewModel {
    private FirebaseFirestore db;
    private StorageReference storageReference;
    private DepartmentAdapter departmentAdapter;
    private List<DepartmentModel> departmentmodelList;
    private PassingObject passingObject;
    public Uri filePath;
    private AddDepartmentRequest addDepartmentRequest;

    public SettingsViewModels() {
        addDepartmentRequest = new AddDepartmentRequest();
        passingObject = new PassingObject();
        db = FirebaseFirestore.getInstance();
        departmentmodelList = new ArrayList<>();
        storageReference = FirebaseStorage.getInstance().getReference();

    }

    public List<DepartmentModel> getDepartmentmodelList() {
        return departmentmodelList;
    }

    public AddDepartmentRequest getAddDepartmentRequest() {
        return addDepartmentRequest;
    }

    public PassingObject getPassingObject() {
        return passingObject;
    }

    @Bindable
    public void setPassingObject(PassingObject passingObject) {
        notifyPropertyChanged(app.grand.ophthalmicadmin.BR.passingObject);
        this.passingObject = passingObject;

    }


    @BindingAdapter({"app:departmentAdapter"})
    public static void getDoctorsBinding(RecyclerView recyclerView, DepartmentAdapter departmentAdapter) {
        recyclerView.setAdapter(departmentAdapter);
    }

    @Bindable
    public DepartmentAdapter getDepartmentAdapter() {
        return this.departmentAdapter == null ? this.departmentAdapter = new DepartmentAdapter() : this.departmentAdapter;
    }

    public void selectImage() {
        getClicksMutableLiveData().setValue(Codes.SELECT_PROFILE_IMAGE);
    }

    public void toAddDepartment() {
        getClicksMutableLiveData().setValue(Codes.ADD_NEW_DEPARTMENT);
    }

    public void addDepartment() {
        if (!TextUtils.isEmpty(getAddDepartmentRequest().getName()) && !TextUtils.isEmpty(getAddDepartmentRequest().getDesc()) && filePath != null) {
            DocumentReference departments = db.collection("Departments").document();
            String docId = departments.getId();
            accessLoadingBar(View.VISIBLE);
            StorageReference ref
                    = storageReference
                    .child(
                            "departments/"
                                    + docId);
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
                                Map<String, String> dep_map = new HashMap<>();
                                dep_map.put("name", getAddDepartmentRequest().getName());
                                dep_map.put("desc", getAddDepartmentRequest().getDesc());
                                dep_map.put("image", task.getResult().toString());
                                dep_map.put("id", docId);
                                departments.set(dep_map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            setReturnedMessage("Add Successfully");
                                            getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_SUCCESS);
                                        }
                                    }
                                });
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


    public void getDepartments() {
        accessLoadingBar(View.VISIBLE);
        db.collection("Departments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        accessLoadingBar(View.GONE);
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("df", document.getId() + " => " + document.getData());
                                DepartmentModel departmentmodel = document.toObject(DepartmentModel.class);
                                departmentmodelList.add(departmentmodel);
                            }
                            getDepartmentAdapter().updateData(departmentmodelList);
                        } else {
                            Log.w("df", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    public void editDepartment() {
        Map<String, Object> dep_map = new HashMap<>();
        dep_map.put("name", getPassingObject().getDepartmentModel().getName());
        dep_map.put("desc", getPassingObject().getDepartmentModel().getDesc());
        db.collection("Departments").document(getPassingObject().getDepartmentModel().getId()).update(dep_map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    setReturnedMessage("Updated Successfully");
                    getClicksMutableLiveData().setValue(Codes.SHOW_MESSAGE_SUCCESS);
                }
            }
        });
    }
}
