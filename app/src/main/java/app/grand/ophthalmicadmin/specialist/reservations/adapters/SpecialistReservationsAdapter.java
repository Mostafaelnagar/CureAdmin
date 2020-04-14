package app.grand.ophthalmicadmin.specialist.reservations.adapters;

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
import app.grand.ophthalmicadmin.base.MovementManager;
import app.grand.ophthalmicadmin.base.constantsutils.Codes;
import app.grand.ophthalmicadmin.databinding.SpecialistReserveItemBinding;
import app.grand.ophthalmicadmin.doctor.models.XRays;
import app.grand.ophthalmicadmin.specialist.reservations.itemViewModels.SpecialistReservationsItemViewModel;


public class SpecialistReservationsAdapter extends RecyclerView.Adapter<SpecialistReservationsAdapter.ViewHolder> {
    public List<XRays> xRaysList;
    Context context;

    public SpecialistReservationsAdapter() {
        xRaysList = new ArrayList<>();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.specialist_reserve_item,
                parent, false);
        context = parent.getContext();
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        XRays dataModel = xRaysList.get(position);
        SpecialistReservationsItemViewModel homeItemViewModels = new SpecialistReservationsItemViewModel(dataModel);
        homeItemViewModels.getClicksMutableLiveData().observe(((LifecycleOwner) context), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == Codes.Specialist_UPDTAE_IMAGE) {
                    MovementManager.startActivityWithObject(context, integer, new PassingObject(dataModel));
                } else if (integer == Codes.MEDICAL_RECORD) {
                    MovementManager.startActivityWithObject(context, integer, new PassingObject(dataModel.getPatient_id()));
                }
            }
        });
        holder.setViewModel(homeItemViewModels);
    }


    @Override
    public int getItemCount() {
        return this.xRaysList.size();
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

    public void updateData(@Nullable List<XRays> data) {
        Log.e("updateData", "updateData: " + data);
        this.xRaysList.clear();
        this.xRaysList.addAll(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SpecialistReserveItemBinding itemBinding;

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

        void setViewModel(SpecialistReservationsItemViewModel itemViewModels) {
            if (itemBinding != null) {
                itemBinding.setReserveItemViewModels(itemViewModels);
            }
        }
    }
}
