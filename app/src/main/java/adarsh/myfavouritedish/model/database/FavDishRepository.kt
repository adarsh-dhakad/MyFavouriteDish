package adarsh.myfavouritedish.model.database

import adarsh.myfavouritedish.model.entities.FavDish
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

// TODO Step 1: Create a Kotlin class file name called FavDishRepository.
/**
 * A Repository manages queries and allows you to use multiple backend.
 *
 * The DAO is passed into the repository constructor as opposed to the whole database.
 * This is because it only needs access to the DAO, since the DAO contains all the read/write methods for the database.
 * There's no need to expose the entire database to the repository.
 *
 * @param favDishDao - Pass the FavDishDao as the parameter.
 */
class FavDishRepository(private val favDishDao: FavDishDao) {

    /**
     * By default Room runs suspend queries off the main thread, therefore, we don't need to
     * implement anything else to ensure we're not doing long running database work
     * off the main thread.
     */
    // TODO Step 2: Create a suspend function to insert the data and annotate it with @WorkerThread.
    @WorkerThread
    suspend fun insertFavDishData(favDish: FavDish) {
        favDishDao.insertFavDishDetails(favDish)
    }

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allDishesList: Flow<List<FavDish>> = favDishDao.getAllDishesList()

    @WorkerThread
    suspend fun updateFavDishData(favDish: FavDish){
        favDishDao.updateFaveDisDetails(favDish)
    }

    val favoriteDishes:Flow<List<FavDish>> = favDishDao.getFavoriteDishesList()
}