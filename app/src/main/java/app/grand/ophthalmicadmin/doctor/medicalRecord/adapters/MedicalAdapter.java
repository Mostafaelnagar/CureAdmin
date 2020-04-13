package app.grand.ophthalmicadmin.doctor.medicalRecord.adapters;

import android.content.Context;
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
import app.grand.ophthalmicadmin.base.MovementManager;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.databinding.MedicalRecordItemBinding;
import app.grand.ophthalmicadmin.doctor.medicalRecord.itemViewModels.MedicalItemViewModel;
import app.grand.ophthalmicadmin.doctor.reservation.models.ReservationsResponse;

public class MedicalAdapter extends RecyclerView.Adapter<MedicalAdapter.ViewHolder> {
    public List<ReservationsResponse> reservationsResponseList;
    Context context;

    public MedicalAdapter() {
        reservationsResponseList = new ArrayList<>();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.medical_record_item,
                parent, false);
        context = parent.getContext();
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReservationsResponse dataModel = reservationsResponseList.get(position);
        MedicalItemViewModel homeItemViewModels = new MedicalItemViewModel(dataModel);
        homeItemViewModels.getClicksMutableLiveData().observe(((LifecycleOwner) context), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                MovementManager.startActivityWithObject(context, Codes.MEDICAL_RECORD_DETAILS, new PassingObject(dataModel));
            }
        });
        holder.setViewModel(homeItemViewModels);
    }


    @Override
    public int getItemCount() {
        return this.reservationsResponseList.size();
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

    public void updateData(@Nullable List<ReservationsResponse> data) {
        this.reservationsResponseList.clear();
        this.reservationsResponseList.addAll(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MedicalRecordItemBinding itemBinding;

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

        void setViewModel(MedicalItemViewModel itemViewModels) {
            if (itemBinding != null) {
                itemBinding.setMedicalRecordItemViewModels(itemViewModels);
            }
        }
    }
}
