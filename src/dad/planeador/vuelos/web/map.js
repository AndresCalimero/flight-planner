function initialize() {
	var overlay = {
		airports: {
			visible: false,
			icons: {
				xs: 'icons/marker_airport_4_4.png',
				s: 'icons/marker_airport_5_5.png',
				m: 'icons/marker_airport_8_8.png',
				l: 'icons/marker_airport_10_10.png',
				xl: 'icons/marker_airport_15_15.png'
			},
			data: []
		},
		intersections: {
			visible: false,
			icons: {
				xs: 'icons/marker_intersection_4_4.png',
				s: 'icons/marker_intersection_5_5.png',
				m: 'icons/marker_intersection_8_8.png',
				l: 'icons/marker_intersection_10_10.png',
				xl: 'icons/marker_intersection_15_15.png'
			},
			data: []
		},
		navaids: {
			visible: false,
			icons: {
				xs: 'icons/marker_navaid_4_4.png',
				s: 'icons/marker_navaid_5_5.png',
				m: 'icons/marker_navaid_8_8.png',
				l: 'icons/marker_navaid_10_10.png',
				xl: 'icons/marker_navaid_15_15.png'
			},
			data: []
		}
	};
	var flightPath;
    var latlng = new google.maps.LatLng(40.138739, -4.646889);
	var maxZoom = 14, minZoom = 3;
	var styleArray = [{"featureType":"poi","stylers":[{"visibility":"off"}]},{"stylers":[{"saturation":-70},{"lightness":37},{"gamma":1.15}]},{"elementType":"labels","stylers":[{"visibility":"off"}]},{"featureType":"road","stylers":[{"visibility":"off"}]},{"featureType":"administrative.province","stylers":[{"visibility":"off"}]}];
    var myOptions = {
		zoom: 6,
		center: latlng,
		mapTypeId: google.maps.MapTypeId.ROADMAP,
		mapTypeControl: false,
		navigationControl: false,
		streetViewControl: false,
		zoomControl: false,
		backgroundColor: "#666970",
		maxZoom: maxZoom,
		minZoom: minZoom,
		scrollwheel: true,
		styles: styleArray
	};
	
    document.map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
	google.maps.event.addListener(document.map, 'idle', updateMarkers);
	
	function updateOverlayInBounds(type, bounds) {
		var zoomLevel = document.map.getZoom();
		var newIcon;
		if (zoomLevel == 3 || zoomLevel == 4) {
			newIcon = type.icons.xs;
		} else if (zoomLevel == 5 || zoomLevel == 6) {
			newIcon = type.icons.s;
		} else if (zoomLevel == 7) {
			newIcon = type.icons.m;
		} else if (zoomLevel > 7 && zoomLevel < 9) {
			newIcon = type.icons.l;
		} else {
			newIcon = type.icons.xl;
		}
		for (var i = 0; i < type.data.length; i++) {
			if (bounds.contains(type.data[i].marker.getPosition())) {
				if (type.data[i].marker.getIcon() != newIcon) {
					type.data[i].marker.setIcon(newIcon);
				}
				if (type.data[i].marker.getMap() == null) {
					type.data[i].marker.setMap(document.map);
				}
			} else {
				type.data[i].marker.setMap(null);
			}
		}
	}
	
	function updateMarkers() {
		var bounds = document.map.getBounds();
		
		if (overlay.airports.visible) {
			updateOverlayInBounds(overlay.airports, bounds);
		}
		
		if (overlay.intersections.visible) {
			updateOverlayInBounds(overlay.intersections, bounds);
		}
		
		if (overlay.navaids.visible) {
			updateOverlayInBounds(overlay.navaids, bounds);
		}
	}
	
    document.zoomIn = function() {
        var zoomLevel = document.map.getZoom();
        if (zoomLevel <= (maxZoom - 1)) document.map.setZoom(zoomLevel + 1);
	}
	
    document.zoomOut = function() {
        var zoomLevel = document.map.getZoom();
        if (zoomLevel > minZoom) document.map.setZoom(zoomLevel - 1);
	}
	
	document.addAirport = function(icao, name, lat, lng) {
		var latlng = {lat: lat, lng: lng};
		var airport = {
			marker: new google.maps.Marker({position: latlng})
		};
		
		var infoHTML = "<h3>" + icao + "</h3>" +
		(name == 'null' ? "" : "<h4>" + name + "</h4>") +
		"<span>Latitud: " + lat + "</span><br/>" +
		"<span>Longitud: " + lng + "</span><br/><br/>" +
		"<a onclick=\"java.establecerAeropuertoOrigen('" + icao + "')\" href=\"javascript:void(0);\">Establecer aeropuerto de origen</a><br/>" +
		"<a onclick=\"java.establecerAeropuertoDestino('" + icao + "')\" href=\"javascript:void(0);\">Establecer aeropuerto de destino</a>";
		
		var info = new google.maps.InfoWindow({
			content: infoHTML
		});
		
		airport.marker.addListener('click', function() {
			info.open(document.map, airport.marker);
		});
		
		overlay.airports.data.push(airport);
	}
	
	document.addIntersection = function(ident, lat, lng) {
		var latlng = {lat: lat, lng: lng};
		var intersection = {
			marker: new google.maps.Marker({position: latlng})
		};
		
		var infoHTML = "<h3>" + ident + "</h3>" +
		"<span>Latitud: " + lat + "</span><br/>" +
		"<span>Longitud: " + lng + "</span>";
		
		var info = new google.maps.InfoWindow({
			content: infoHTML
		});
		
		intersection.marker.addListener('click', function() {
			info.open(document.map, intersection.marker);
		});
		
		overlay.intersections.data.push(intersection);
	}
	
	document.addNavaid = function(ident, name, type, lat, lng) {
		var latlng = {lat: lat, lng: lng};
		var navaid = {
			marker: new google.maps.Marker({position: latlng})
		};
		
		var infoHTML = "<h3>" + ident + " (" + type + ")" + "</h3>" +
		(name == 'null' ? "" : "<h4>" + name + "</h4>") +
		"<span>Latitud: " + lat + "</span><br/>" +
		"<span>Longitud: " + lng + "</span>";
		
		var info = new google.maps.InfoWindow({
			content: infoHTML
		});
		
		navaid.marker.addListener('click', function() {
			info.open(document.map, navaid.marker);
		});
		
		overlay.navaids.data.push(navaid);
	}
	
	document.setRoute = function(route) {
		if (flightPath) {
			flightPath.setMap(null);
			flightPath = 0;
		}
		
		if (route) {
			var flightPlanCoordinates = [];
			
			for (var i = 0; i < route.length; i++) {
				flightPlanCoordinates.push(route[i]);
			}
			
			flightPath = new google.maps.Polyline({
				path: flightPlanCoordinates,
				geodesic: true,
				strokeColor: '#FF0000',
				strokeOpacity: 1.0,
				strokeWeight: 2
			});
			
			flightPath.setMap(document.map);
		}
	}
	
	document.showAirports = function(show) {
		if (overlay.airports.visible != show) {
			overlay.airports.visible = show;
			if (show) {
				updateMarkers();
			} else {
				for (var i = 0; i < overlay.airports.data.length; i++) {
					overlay.airports.data[i].marker.setMap(null);
				}
			}
		}
	}
	
	document.showIntersections = function(show) {
		if (overlay.intersections.visible != show) {
			overlay.intersections.visible = show;
			if (show) {
				updateMarkers();
			} else {
				for (var i = 0; i < overlay.intersections.data.length; i++) {
					overlay.intersections.data[i].marker.setMap(null);
				}
			}
		}
	}
	
	document.showNavaids = function(show) {
		if (overlay.navaids.visible != show) {
			overlay.navaids.visible = show;
			if (show) {
				updateMarkers();
			} else {
				for (var i = 0; i < overlay.navaids.data.length; i++) {
					overlay.navaids.data[i].marker.setMap(null);
				}
			}
		}
	}
	
    document.goToLocation = function(latlng, zoom) {
		document.map.setZoom(zoom);
		document.map.setCenter(latlng);
	}
}	