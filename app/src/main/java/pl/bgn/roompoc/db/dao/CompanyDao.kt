package pl.bgn.roompoc.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import pl.bgn.roompoc.db.entity.Company

@Dao
interface CompanyDao {

    @Insert
    suspend fun insert(company: Company)

    @Update
    suspend fun update(company: Company)

    @Query("SELECT company_table.id, company_table.name FROM company_table INNER JOIN building_table ON company_table.id = building_table.companyId WHERE building_table.id = :buildingId")
    suspend fun getBuildingCompany(buildingId : Int) : Company
}