/*
 * Copyright (c) 2020.
 * created on 29/3/20 5:49 AM
 * by  Ansh Sachdeva (www.github.com/root-ansh)
 *
 */

package in.curioustools.menu_maker.modal;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "menu_entry_table")
public class MenuEntry {

    @NonNull @ColumnInfo(name = "id")@PrimaryKey
    private String id;

    @Nullable @ColumnInfo(name = "category_name")
    private String categoryName;


    @Nullable @ColumnInfo(name = "item_name")
    private String itemName;

    @ColumnInfo(name = "p_half")
    private int priceHalf;

    @ColumnInfo(name = "p_full")
    private int priceFull;

    @IntDef( {Type.CATEGORY,Type.ITEM})
    public  @interface  Type{
        int CATEGORY = 0;
        int ITEM = 21;
    }

    @Type @ColumnInfo(name = "type")
    private int type;

    public MenuEntry() {
        this.id = UUID.randomUUID().toString();
    }
    public MenuEntry(@NonNull String categoryName) {
        this.id = UUID.randomUUID().toString();
        this.type = Type.CATEGORY;
        this.categoryName = categoryName;
        this.priceHalf = -1;
        this.priceFull = -1;
    }
    public MenuEntry(@NonNull String categoryName , @NonNull String itemName, int priceHalf, int priceFull) {
        this.id = UUID.randomUUID().toString();
        this.type = Type.ITEM;
        this.categoryName = categoryName;
        this.itemName = itemName;

        this.priceHalf = priceHalf;
        this.priceFull = priceFull;
    }
    public MenuEntry(@NonNull String categoryName , @NonNull String itemName, int priceTotal) {
        this.id = UUID.randomUUID().toString();
        this.type = Type.ITEM;
        this.categoryName = categoryName;
        this.itemName = itemName;
        this.priceHalf = 0;
        this.priceFull = priceTotal;

    }

    @NonNull
    public String getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    @Nullable
    public String getCategoryName() {
        return categoryName;
    }

    @Nullable
    public String getItemName() {
        return itemName;
    }

    public int getPriceHalf() {
        return priceHalf;
    }

    public int getPriceFull() {
        return priceFull;
    }


    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setType(@Type int type) {
        this.type = type;
    }

    public void setCategoryName(@Nullable String categoryName) {
        this.categoryName = categoryName;
    }

    public void setItemName(@Nullable String itemName) {
        this.itemName = itemName;
    }

    public void setPriceHalf(int priceHalf) {
        this.priceHalf = priceHalf;
    }

    public void setPriceFull(int priceFull) {
        this.priceFull = priceFull;
    }


    @Override @NonNull
    public String toString() {
        return "MenuEntries{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", categoryName='" + categoryName + '\'' +
                ", itemName='" + itemName + '\'' +
                ", priceHalf=" + priceHalf +
                ", priceFull=" + priceFull +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }

}
