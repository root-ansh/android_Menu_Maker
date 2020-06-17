/*
 * Copyright (c) 2020.
 * created on 29/3/20 5:07 AM
 * by  Ansh Sachdeva (www.github.com/root-ansh)
 *
 */

// TODO: 28/3/20  
package in.curioustools.menu_maker.screens.menu_list;

class tmp2{}

//@SuppressLint("SetTextI18n")
//public class MenuEntryListAdapter2 extends RecyclerView.Adapter<MenuEntryListAdapter2.RvHolder> {
//    @Nullable
//    private OnMenuEntryClickListener clickListener;
//
//    private List<MenuEntry> entryList = new ArrayList<>();
//
//    public MenuEntryListAdapter2(List<MenuEntry> entryList) {
//        this.entryList = entryList;
//    }
//
//    public void setEntryList(List<MenuEntry> entryList) {
//        this.entryList = entryList;
//        notifyDataSetChanged();
//    }
//
//
//
//
//    @NonNull
//    @Override
//    public RvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater
//                .from(parent.getContext())
//                .inflate(R.layout.layout_menu_entry, parent, false);
//
//        return new RvHolder(v);
//    }
//
//
//    @Override
//    public void onBindViewHolder(@NonNull RvHolder holder, int position) {
//        holder.bind(entryList.get(position), clickListener);
//    }
//
//    @Override
//    public int getItemCount() {
//        return entryList.size();
//    }
//
//
//    //-------------extra access methods ----------------------------------------
//
//    @Nullable
//    public OnMenuEntryClickListener getClickListener() {
//        return clickListener;
//    }
//
//    public void setClickListener(@Nullable OnMenuEntryClickListener clickListener) {
//        this.clickListener = clickListener;
//        notifyDataSetChanged();
//    }
//
//    //-----------------------Recycler view holder class --------------------------------------------
//
//    static class RvHolder extends RecyclerView.ViewHolder {
//        private LinearLayout llCategory, llItemDetails;
//        private TextView tvCategory, tvItem, tvRateHalf, tvRateFull;
//        private TextView tvEtCat, tvDelCat, tvEtItem, tvDelItem;
//
//        RvHolder(@NonNull View v) {
//            super(v);
//
//            llCategory = v.findViewById(R.id.ll_category);
//            llItemDetails = v.findViewById(R.id.ll_item_dets);
//
//            tvCategory = v.findViewById(R.id.tv_category);
//            tvItem = v.findViewById(R.id.tv_itemname);
//            tvRateHalf = v.findViewById(R.id.tv_p_half);
//            tvRateFull = v.findViewById(R.id.tv_p_full);
//
//            tvEtCat = v.findViewById(R.id.tv_edit_cat);
//            tvDelCat = v.findViewById(R.id.tv_delete_cat);
//            tvEtItem = v.findViewById(R.id.tv_edit_item);
//            tvDelItem = v.findViewById(R.id.tv_delete_item);
//
//            //init-ui
//
//            setupDefaultData();
//        }
//
//
//        void bind(@Nullable MenuEntry entry, @Nullable OnMenuEntryClickListener clickListener) {
//            if (entry != null) {
//                bindData(entry);
//            }
//            else {
//                setupDefaultData();
//            }
//
//            if (clickListener != null) {
//                setupListeners(entry, clickListener);
//            }
//
//        }
//        private void bindData(MenuEntry menuEntry) {
//            if (menuEntry.getType() == MenuEntry.Type.CATEGORY) {
//                llCategory.setVisibility(View.VISIBLE);
//                tvCategory.setText("" + menuEntry.getCategoryName());
//                llItemDetails.setVisibility(View.GONE);
//            }
//            else {
//                llItemDetails.setVisibility(View.VISIBLE);
//                tvItem.setText(""+menuEntry.getItemName());
//
//                int pHalf= menuEntry.getPriceHalf();
//                String half = "[H]: Rs "+pHalf+"/- ";
//                tvRateHalf.setText(pHalf==0?"-":half);
//
//                int pFull = menuEntry.getPriceFull();
//                String full = "[F]: Rs "+pFull+"/-";
//                tvRateFull.setText(full);
//
//                llCategory.setVisibility(View.GONE);
//            }
//        }
//        private void setupDefaultData() {
//            tvCategory.setText("");
//            tvItem.setText("");
//            tvRateHalf.setText("-");
//            tvRateFull.setText("-");
//
//            llCategory.setVisibility(View.GONE);
//
//        }
//
//        private void setupListeners(@Nullable MenuEntry entry, @NonNull OnMenuEntryClickListener clickListener) {
//            if (tvEtItem != null && tvEtItem.getVisibility() == View.VISIBLE) {
//                tvEtItem.setOnClickListener(v -> clickListener.onEditClick(entry));
//            }
//
//            if (tvEtCat != null && tvEtCat.getVisibility() == View.VISIBLE) {
//                tvEtCat.setOnClickListener(v -> clickListener.onEditClick(entry));
//            }
//
//
//            if (tvDelItem != null && tvDelItem.getVisibility() == View.VISIBLE) {
//                tvDelItem.setOnClickListener(v -> clickListener.onDeleteClick(entry));
//            }
//            if (tvDelCat != null && tvDelCat.getVisibility() == View.VISIBLE) {
//                tvDelCat.setOnClickListener(v -> clickListener.onDeleteClick(entry));
//            }
//        }
//
//
//
//    }
//
//    //--------------Rv Item Click Listener Class-------------------------------------------------------
//    public interface OnMenuEntryClickListener {
//        void onEditClick(@Nullable MenuEntry menuEntry);
//
//        void onDeleteClick(@Nullable MenuEntry menuEntry);
//
//    }
//
//}
