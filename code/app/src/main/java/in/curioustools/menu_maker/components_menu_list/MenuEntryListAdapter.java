/*
 * Copyright (c) 2020.
 * created on 29/3/20 5:07 AM
 * by  Ansh Sachdeva (www.github.com/root-ansh)
 *
 */
package in.curioustools.menu_maker.components_menu_list;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import in.curioustools.menu_maker.R;
import in.curioustools.menu_maker.modal.MenuEntry;

@SuppressLint("SetTextI18n")
public class MenuEntryListAdapter extends PagedListAdapter<MenuEntry, MenuEntryListAdapter.RvHolder> {
    @Nullable
    private OnMenuEntryClickListener clickListener;


    private static final DiffUtil.ItemCallback<MenuEntry> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<MenuEntry>() {
                @Override
                public boolean areItemsTheSame(@NonNull MenuEntry oldItem, @NonNull MenuEntry newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull MenuEntry oldItem, @NonNull MenuEntry newItem) {
                    return oldItem.equals(newItem);
                }
            };

    public MenuEntryListAdapter() {
        super(DIFF_CALLBACK);


    }

    @NonNull
    @Override
    public RvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_menu_entry, parent, false);

        return new RvHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull RvHolder holder, int position) {
        holder.bind(getItem(position), clickListener);
    }


    //-------------extra access methods ----------------------------------------

    @Nullable
    public OnMenuEntryClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(@Nullable OnMenuEntryClickListener clickListener) {
        this.clickListener = clickListener;
        notifyDataSetChanged();
    }

    //-----------------------Recycler view holder class --------------------------------------------

    static class RvHolder extends RecyclerView.ViewHolder {
        private LinearLayout llCategory, llItemDetails;
        private TextView tvCategory, tvItem, tvRateHalf, tvRateFull;
        private TextView tvEtCat, tvDelCat, tvEtItem, tvDelItem;

        RvHolder(@NonNull View v) {
            super(v);

            llCategory = v.findViewById(R.id.ll_category);
            llItemDetails = v.findViewById(R.id.ll_item_dets);

            tvCategory = v.findViewById(R.id.tv_category);
            tvItem = v.findViewById(R.id.tv_itemname);
            tvRateHalf = v.findViewById(R.id.tv_p_half);
            tvRateFull = v.findViewById(R.id.tv_p_full);

            tvEtCat = v.findViewById(R.id.tv_edit_cat);
            tvDelCat = v.findViewById(R.id.tv_delete_cat);
            tvEtItem = v.findViewById(R.id.tv_edit_item);
            tvDelItem = v.findViewById(R.id.tv_delete_item);

            //init-ui

            setupDefaultData();
        }


        void bind(@Nullable MenuEntry entry, @Nullable OnMenuEntryClickListener clickListener) {
            if (entry != null) {
                bindData(entry);
            }
            else {
                setupDefaultData();
            }

            if (clickListener != null) {
                setupListeners(entry, clickListener);
            }

        }
        private void bindData(MenuEntry menuEntry) {
            if (menuEntry.getType() == MenuEntry.Type.CATEGORY) {
                llCategory.setVisibility(View.VISIBLE);
                tvCategory.setText("" + menuEntry.getCategoryName());
                llItemDetails.setVisibility(View.GONE);
            }
            else {
                llItemDetails.setVisibility(View.VISIBLE);
                tvItem.setText(""+menuEntry.getItemName());

                int pHalf= menuEntry.getPriceHalf();
                String half = "[H]: Rs "+pHalf+"/- ";
                tvRateHalf.setText(pHalf==0?"-":half);

                int pFull = menuEntry.getPriceFull();
                String full = "[F]: Rs "+pFull+"/-";
                tvRateFull.setText(full);

                llCategory.setVisibility(View.GONE);
            }
        }
        private void setupDefaultData() {
            tvCategory.setText("");
            tvItem.setText("");
            tvRateHalf.setText("-");
            tvRateFull.setText("-");

            llCategory.setVisibility(View.GONE);

        }

        private void setupListeners(@Nullable MenuEntry entry, @NonNull OnMenuEntryClickListener clickListener) {
            if (tvEtItem != null && tvEtItem.getVisibility() == View.VISIBLE) {
                tvEtItem.setOnClickListener(v -> clickListener.onEditClick(entry));
            }

            if (tvEtCat != null && tvEtCat.getVisibility() == View.VISIBLE) {
                tvEtCat.setOnClickListener(v -> clickListener.onEditClick(entry));
            }

            if (tvDelItem != null && tvDelItem.getVisibility() == View.VISIBLE) {
                tvDelItem.setOnClickListener(v -> clickListener.onDeleteClick(entry));
            }
            if (tvDelCat != null && tvDelCat.getVisibility() == View.VISIBLE) {
                tvDelCat.setOnClickListener(v -> clickListener.onDeleteClick(entry));
            }
        }



    }

    //--------------Rv Item Click Listener Class-------------------------------------------------------
    public interface OnMenuEntryClickListener {
        void onEditClick(@Nullable MenuEntry menuEntry);

        void onDeleteClick(@Nullable MenuEntry menuEntry);

    }

}
