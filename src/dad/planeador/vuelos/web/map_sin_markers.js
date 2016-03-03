function initialize() {
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
	
    //document.geocoder = new google.maps.Geocoder();
    document.map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
	google.maps.event.addListener(document.map, 'idle', actualizarBounds);
	
	function actualizarBounds() {
		var bounds = document.map.getBounds();
		java.actualizarBounds(bounds.getNorthEast().lat(), bounds.getNorthEast().lng(), bounds.getSouthWest().lat(), bounds.getSouthWest().lng());
	}
	
    document.zoomIn = function() {
        var zoomLevel = document.map.getZoom();
        if (zoomLevel <= (maxZoom - 1)) document.map.setZoom(zoomLevel + 1);
	}
	
    document.zoomOut = function() {
        var zoomLevel = document.map.getZoom();
        if (zoomLevel > minZoom) document.map.setZoom(zoomLevel - 1);
	}
	
    document.goToLocation = function(latlng) {
		document.map.setCenter(latlng);
	}
}