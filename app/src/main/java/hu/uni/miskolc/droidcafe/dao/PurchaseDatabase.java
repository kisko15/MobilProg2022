package hu.uni.miskolc.droidcafe.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import hu.uni.miskolc.droidcafe.model.PurchaseData;

@Database(entities = {PurchaseData.class}, version = 1)
public abstract class PurchaseDatabase extends RoomDatabase {
    public abstract PurchaseDataDao getPurchaseDataDao();
}
