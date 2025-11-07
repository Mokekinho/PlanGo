package com.example.plango.database

import com.example.plango.model.Travel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// um repositorio basicamente é uma camada entre a Dao e a UI, isso é util pois, caso eu queira mudar a forma de acessar meus dados, por exemplo, na nuvem, eu so mudo aqui e nao no codigo inteiro.

class TravelRepository(private val travelDao: TravelDao) {

    suspend fun upsertTravel(travelEntity: TravelEntity) {
        travelDao.upsertTravel(travelEntity)
    }

    fun getAllTravels(): Flow<List<Travel>> {
        return travelDao.getAllTravels().map { travels ->
            travels.map {
                it.toDomainModel()
            }
        }
    }

    suspend fun getTravelById(id: Int): Travel? {
        return travelDao.getTravelById(id)?.toDomainModel()
    }

    suspend fun upsertTravelWithList(travel: Travel) {

        val travelWithList = travel.toEntitySet()

        val travelEntity = travelWithList.travelEntity
        val expenses = travelWithList.expenses
        val flights = travelWithList.flights
        val hotels = travelWithList.hotels
        val documentInfo = travelWithList.documentInfoEntity

        travelDao.upsertTravel(travelEntity)

        if (expenses.isNotEmpty()) travelDao.upsertExpenses(expenses)
        if (flights.isNotEmpty()) travelDao.upsertFlights(flights)
        if (hotels.isNotEmpty()) travelDao.upsertHotels(hotels)
        documentInfo?.let { travelDao.upsertDocumentInfo(it) }
    }
}