package app.grand.ophthalmicadmin.admin.adminSepcialist.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.grand.ophthalmicadmin.PassingObject;
import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.admin.adminSepcialist.itemViewModels.AdminSpecialistItemViewModel;
import app.grand.ophthalmicadmin.admin.doctors.itemViewModels.AdminDoctorsItemViewModel;
import app.grand.ophthalmicadmin.auth.model.UserData;
import app.grand.ophthalmicadmin.base.MovementManager;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.databinding.DoctorItemBinding;
import app.grand.ophthalmicadmin.databinding.SpecialistItemBinding;


public class AdminSpecialistAdapter extends RecyclerView.Adapter<AdminSpecialistAdapter.ViewHolder> {
    public List<UserData> userDataList;
    Context context;

    public AdminSpecialistAdapter() {
        userDataList = new ArrayList<>();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.specialist_item,
                parent, false);
        context = parent.getContext();
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserData dataModel = userDataList.get(position);
        AdminSpecialistItemViewModel homeItemViewModels = new AdminSpecialistItemViewModel(dataModel);
        homeItemViewModels.getClicksMutableLiveData().observe(((LifecycleOwner) context), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == Codes.ADMIN_DOCTORS_PROFILE) {
                    MovementManager.startActivityWithObject(context, integer, new PassingObject(dataModel));
                } else if (integer == Codes.ADMIN_DOCTORS_RESERVATIONS) {
                    MovementManager.startActivityWithObject(context, integer, new PassingObject(dataModel.getDocId()));

                }
            }
        });
        holder.setViewModel(homeItemViewModels);
    }


    @Override
    public int getItemCount() {
        return this.userDataList.size();
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }

    public void updateData(@Nullable List<UserData> data) {
        this.userDataList.clear();
        this.userDataList.addAll(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SpecialistItemBinding itemBinding;

        ViewHolder(View itemView) {
            super(itemView);
            bind();
        }


        void bind() {
            if (itemBinding == null) {
                itemBinding = DataBindingUtil.bind(itemView);
            }
        }

        void unbind() {
            if (itemBinding != null) {
                itemBinding.unbind();
            }
        }

        void setViewModel(AdminSpecialistItemViewModel itemViewModels) {
            if (itemBinding != null) {
                itemBinding.setSpecialItemViewModels(itemViewModels);
            }
        }
    }
}
