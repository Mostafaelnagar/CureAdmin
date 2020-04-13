package app.grand.ophthalmicadmin.admin.adminReservations.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.grand.ophthalmicadmin.PassingObject;
import app.grand.ophthalmicadmin.R;
import app.grand.ophthalmicadmin.admin.adminReservations.itemViewModels.AdminReservationsItemViewModel;
import app.grand.ophthalmicadmin.databinding.AdminReserveItemBinding;
import app.grand.ophthalmicadmin.doctor.reservation.models.ReservationsResponse;


public class AdminReservationsAdapter extends RecyclerView.Adapter<AdminReservationsAdapter.ViewHolder> {
    public List<ReservationsResponse> reservationsResponseList;
    Context context;
    private MutableLiveData<PassingObject> adminActions;

    public AdminReservationsAdapter() {
        reservationsResponseList = new ArrayList<>();
        adminActions = new MutableLiveData<>();
    }

    public MutableLiveData<PassingObject> getAdminActions() {
        return adminActions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_reserve_item,
                parent, false);
        context = parent.getContext();
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReservationsResponse dataModel = reservationsResponseList.get(position);
        AdminReservationsItemViewModel homeItemViewModels = new AdminReservationsItemViewModel(dataModel);
        homeItemViewModels.getClicksMutableLiveData().observe(((LifecycleOwner) context), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                adminActions.setValue(new PassingObject(dataModel.getDoc_id(), integer));
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
        AdminReserveItemBinding itemBinding;

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

        void setViewModel(AdminReservationsItemViewModel itemViewModels) {
            if (itemBinding != null) {
                itemBinding.setReserveItemViewModels(itemViewModels);
            }
        }
    }
}
