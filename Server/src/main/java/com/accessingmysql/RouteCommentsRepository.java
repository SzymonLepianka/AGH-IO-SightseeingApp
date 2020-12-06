package com.accessingmysql;

import org.springframework.data.repository.CrudRepository;

import com.accessingmysql.RouteComments;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface RouteCommentsRepository extends CrudRepository<RouteComments, Integer> {

}