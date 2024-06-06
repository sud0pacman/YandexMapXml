package com.example.yandexmaptest.utils

import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.PolylinePosition
import com.yandex.mapkit.geometry.geo.PolylineIndex
import com.yandex.mapkit.geometry.geo.PolylineUtils

fun routeProgress(route: DrivingRoute): Float {
    val startPosition = PolylinePosition(0, 0.0)
    val distanceFull = route.metadataAt(startPosition).weight.distance.value
    val distanceLeft = route.metadata.weight.distance.value
    return (1f - distanceLeft / distanceFull).toFloat()
}

fun distanceBetweenPointsOnRoute(route: DrivingRoute, first: Point, second: Point): Float {
    val polylineIndex = PolylineUtils.createPolylineIndex(route.geometry)
    val firstPosition = polylineIndex.closestPolylinePosition(first, PolylineIndex.Priority.CLOSEST_TO_RAW_POINT, 1.0)!!
    val secondPosition = polylineIndex.closestPolylinePosition(second, PolylineIndex.Priority.CLOSEST_TO_RAW_POINT, 1.0)!!
    return PolylineUtils.distanceBetweenPolylinePositions(route.geometry, firstPosition, secondPosition).toFloat()
}
