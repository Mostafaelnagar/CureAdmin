package app.grand.ophthalmicadmin.doctor.reservation.adapters;

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
import app.grand.ophthalmicadmin.databinding.ReserveDetailItemBinding;
import app.grand.ophthalmicadmin.databinding.ReserveItemBinding;
import app.grand.ophthalmicadmin.doctor.reservation.itemViewModels.ReservationsDetailsItemViewModel;
import app.grand.ophthalmicadmin.doctor.reservation.itemViewModels.ReservationsItemViewModel;
import app.grand.ophthalmicadmin.doctor.reservation.models.ReservationsResponse;


public class ReservationsDetailsAdapter extends RecyclerView.Adapter<ReservationsDetailsAdapter.ViewHolder> {
    public List<ReservationsResponse> reservationsResponseList;
    Context context;

    public ReservationsDetailsAdapter() {
        reservationsResponseList = new ArrayList<>();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reserve_detail_item,
                parent, false);
        context = parent.getContext();
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReservationsResponse dataModel = reservationsResponseList.get(position);
        ReservationsDetailsItemViewModel homeItemViewModels = new ReservationsDetailsItemViewModel(dataModel);
        homeItemViewModels.getClicksMutableLiveData().observe(((LifecycleOwner) context), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                MovementManager.startActivityWithObject(context, integer, new PassingObject(dataModel.getPatient(), dataModel.getDoc_id()));
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
        ReserveDetailItemBinding itemBinding;

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

        void setViewModel(ReservationsDetailsItemViewModel itemViewModels) {
            if (itemBinding != null) {
                itemBinding.setReserveItemViewModels(itemViewModels);
            }
        }
    }
}
