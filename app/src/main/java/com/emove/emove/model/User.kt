package com.emove.emove.model

data class User(val addresses: List<Address>?, val isDriver: Boolean, val car: Car?, val email: String, val name: String, val profile_picture_url: String, val phone: String) {}

data class Address(val id: String?, val lat: String?, val lng: String?, val name: String?, val type: String?)

data class Car(val color: String?, val extra_info: String?, val make: String?, val model: String?, val registration_number: String?, val seats: String?)