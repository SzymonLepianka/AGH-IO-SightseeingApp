package com.accessingmysql;

import org.springframework.data.repository.CrudRepository;

import com.accessingmysql.FavoritePlaces;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface FavoritePlacesRepository extends CrudRepository<FavoritePlaces, Integer> {
//tutaj chyba można dodać jakieś wyszukiwanie? Coś takiego... kij rozumiem.
}