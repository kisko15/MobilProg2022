package hu.uni.miskolc.droidcafe.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import hu.uni.miskolc.droidcafe.model.PurchaseData;

@Dao
public interface PurchaseDataDao {

    @Query("SELECT * FROM purchase")
    List<PurchaseData> getAll();

    @Insert
    void insertAll(PurchaseData... purchaseData);
}
