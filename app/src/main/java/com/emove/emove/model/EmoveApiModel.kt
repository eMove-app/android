package com.emove.emove.model

data class LoginResponse(val data: User, val token: String) {}

data class GetUserResponse(val data: User) {}

data class StartTripBody(val start_point: Position, val end_point: Position, val start_time: String)

data class SearchResultsResponse(val data: List<SearchResult>)

data class GetTripResponse(val data: GetTripData)

data class GetTripData(val directions: Directions, val points: List<UserPoint>)

data class UserPoint(val lat: Double, val lng: Double, val user: User)

data class SearchResult(val initial: Trip, val updated: Trip, val user: User, val points: List<UserPoint>, val eta: Int)