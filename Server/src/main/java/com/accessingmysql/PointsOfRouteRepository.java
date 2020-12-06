package com.accessingmysql;

import org.springframework.data.repository.CrudRepository;

import com.accessingmysql.PointsOfRoute;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface PointsOfRouteRepository extends CrudRepository<PointsOfRoute, Integer> {

}