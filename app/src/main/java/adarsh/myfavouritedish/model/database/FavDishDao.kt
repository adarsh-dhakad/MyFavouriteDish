package adarsh.myfavouritedish.model.database

import adarsh.myfavouritedish.model.entities.FavDish
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FavDishDao {

    /**
     * All queries must be executed on a separate thread. They cannot be executed from Main Thread or it will cause an crash.
     *
     * Room has Kotlin coroutines support.
     * This allows your queries to be annotated with the suspend modifier and then called from a coroutine
     * or from another suspension function.
     */

    /**
     * A function to insert favorite dish details to the local database using Room.
     *
     * @param favDish - Here we will pass the entity class that we have created.
     */
    @Insert
    suspend fun insertFavDishDetails(favDish: FavDish)

    /**
     * When data changes, you usually want to take some action, such as displaying the updated data in the UI.
     * This means you have to observe the data so when it changes, you can react.
     *
     * To observe data changes we will use Flow from kotlinx-coroutines.
     * Use a return value of type Flow in your method description,
     * and Room generates all necessary code to update the Flow when the database is updated.
     *
     * A Flow is an async sequence of values
     * Flow produces values one at a time (instead of all at once) that can generate values from async operations
     * like network requests, database calls, or other async code.
     * It supports coroutines throughout its API, so you can transform a flow using coroutines as well!
     */
    @Query("SELECT * FROM FAV_DISHES_TABLE ORDER BY ID")
    fun getAllDishesList(): Flow<List<FavDish>>

    @Update
    suspend fun updateFaveDisDetails(favDish: FavDish)

    @Query("SELECT * FROM FAV_DISHES_TABLE WHERE favorite_dish = 1")
    fun getFavoriteDishesList():Flow<List<FavDish>>
}