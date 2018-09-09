package com.emove.emove.model

data class Trip(val directions: Directions, val values: Values) {}

data class Directions(val points: List<UserPoint>, val overview_polyline: Polyline?)

data class Leg(val start_location: Position, val end_location: Position, val steps: List<Leg>?, val polyline: Polyline?)

data class Polyline(val points: String)

data class Values(val distance: Long, val duration: Long)