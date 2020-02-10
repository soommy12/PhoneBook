package pl.bgn.roompoc.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import pl.bgn.roompoc.db.entity.Building

@Dao
interface BuildingDao {

    @Insert
    suspend fun insert(building: Building)

    @Update
    suspend fun update(building: Building)

    @Query("SELECT * FROM building_table WHERE companyId = :companyId")
    suspend fun getCompanyBuilding(companyId: Int) : Building
}