package org.gaaroth.devlib.googleapi;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolygon;

public class GoogleApiUtil {
	
	//public static final String API_KEY = "AIzaSyDMoXX0QH5ALRfU9LPGiMSiNMpN934BynU";
	public static final String API_KEY = "AIzaSyCLgup1mHI_ollHu2qAt2S_KQrXfUqIzSQ";
	public static final String GEOCODER_REQUEST_PREFIX_FOR_XML = "https://maps.google.com/maps/api/geocode/xml?key="+API_KEY;
	public static final String DISTANCEMATRIX_REQUEST_PREFIX_FOR_XML = "https://maps.google.com/maps/api/distancematrix/xml?key="+API_KEY;
	public static final String CREMONAUFFICIO_ADDRESS = "Via+della+Fogarina+8+26100+Cremona+CR+Italia";
	public static final String CREMONAUFFICIO_LATLON = "45.165518,9.9740189";

	public static Map<String, Double> getCordinates(String address) {
		Map<String, Double> coord = new HashMap<String, Double>();
		coord.put("LAT", 0.0);
		coord.put("LON", 0.0);
		try {
			// prepare a URL to the geocoder
			URL url = new URL(GEOCODER_REQUEST_PREFIX_FOR_XML + "&address=" + URLEncoder.encode(address, "UTF-8"));

			// prepare an HTTP connection to the geocoder
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			Document geocoderResultDocument = null;
			try {
				// open the connection and get results as InputSource.
				conn.connect();
				InputSource geocoderResultInputSource = new InputSource(conn.getInputStream());

				// read result and parse into XML Document
				geocoderResultDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(geocoderResultInputSource);
			} finally {
				conn.disconnect();
			}

			// prepare XPath
			XPath xpath = XPathFactory.newInstance().newXPath();

			// extract the result
			NodeList resultNodeList = null;
			resultNodeList = (NodeList) xpath.evaluate("/GeocodeResponse/result[1]/geometry/location/*", geocoderResultDocument,
					XPathConstants.NODESET);
			double lat = 0.0;
			double lng = 0.0;
			for (int i = 0; i < resultNodeList.getLength(); ++i) {
				Node node = resultNodeList.item(i);
				if ("lat".equals(node.getNodeName()))
					lat = Double.valueOf(node.getTextContent());// Float.parseFloat(node.getTextContent());
				if ("lng".equals(node.getNodeName()))
					lng = Double.valueOf(node.getTextContent());// Float.parseFloat(node.getTextContent());
			}

			coord.put("LAT", lat);
			coord.put("LON", lng);
		} catch (Exception e) {

		}
		return coord;

	}
	
	/**
	 * Address come "lat,long" o stringa con spazi separati di "+"
	 * E' possibile mandarne una lista, separata da "|"
	 */
	public static Map<String, Integer> getRealDurationAndDistance(String address) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		result.put("DUR", 0);
		result.put("DIS", 0);
		try {
			// prepare a URL to the geocoder
			URL url = new URL(DISTANCEMATRIX_REQUEST_PREFIX_FOR_XML + "&origins=" + CREMONAUFFICIO_ADDRESS + "&destinations=" + URLEncoder.encode(address, "UTF-8"));

			// prepare an HTTP connection to the geocoder
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			Document geocoderResultDocument = null;
			try {
				// open the connection and get results as InputSource.
				conn.connect();
				InputSource geocoderResultInputSource = new InputSource(conn.getInputStream());

				// read result and parse into XML Document
				geocoderResultDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(geocoderResultInputSource);
			} finally {
				conn.disconnect();
			}

			// prepare XPath
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			//Get status
			Node resultNodeStatus = (Node) xpath.evaluate("/DistanceMatrixResponse/status", geocoderResultDocument, XPathConstants.NODE);
			
			if ("OK".equals(resultNodeStatus.getTextContent())) {
				// extract the result duration
				Node resultNodeDuration = (Node) xpath.evaluate("/DistanceMatrixResponse/row/element/duration/value", geocoderResultDocument, XPathConstants.NODE);
				// extract the result duration
				Node resultNodeDistance = (Node) xpath.evaluate("/DistanceMatrixResponse/row/element/distance/value", geocoderResultDocument, XPathConstants.NODE);
			
				result.put("DUR", new Integer(resultNodeDuration.getTextContent()));
				result.put("DIS", new Integer(resultNodeDistance.getTextContent()));
				//result = resultNodeDuration.getTextContent()+"|"+resultNodeDistance.getTextContent();
			}
		} catch (Exception e) {

		}
		return result;
	}

	public static Double getDistanceFromLatLonInKm(Double lat1, Double lon1, Double lat2, Double lon2) {
		Double heartRadius = 6371.0; /* Radius of the earth in km */
		Double dLat = deg2rad(lat2 - lat1);
		Double dLon = deg2rad(lon2 - lon1);

		Double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon / 2)
				* Math.sin(dLon / 2);
		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		Double d = heartRadius * c; /* Distance in km */
		
		return d;
	}

	// public Double getDistanceFromLatLonInKm(Double lat1, Double lon1, Double
	// lat2, Double lon2) {
	// Double heartRadius = new Double(6371); /*Radius of the earth in km*/
	// Double dLat = deg2rad(lat2.subtract(lat1));
	// Double dLon = deg2rad(lon2.subtract(lon1));
	// Double a =
	// new Double(Math.sin(dLat.divide(new Double(2)).doubleValue()))
	// .multiply(new Double(Math.sin(dLat.divide(new Double(2)).doubleValue())))
	// .add(
	// new Double(Math.cos(deg2rad(lat1).doubleValue()))
	// .multiply(new Double(Math.cos(deg2rad(lat2).doubleValue())))
	// .multiply(new Double(Math.sin(dLon.divide(new Double(2)).doubleValue())))
	// .multiply(new Double(Math.sin(dLon.divide(new Double(2)).doubleValue())))
	// );
	// Double c = new Double(2).multiply(new
	// Double(Math.atan2(Math.sqrt(a.doubleValue()), Math.sqrt(new
	// Double(1).subtract(a).doubleValue()))));
	// Double d = heartRadius.multiply(c); /*Distance in km*/
	// return d;
	// }

	public static Double deg2rad(Double deg) {
		return deg * (Math.PI / 180);
	}
	
	public static  GoogleMapPolygon createSquare(Double baseLatitude, Double baseLongitude, Double side) {
		LinkedList<LatLon> points = new LinkedList<LatLon>();
		
		Double heartRadius = new Double(6371);
		Double x, y;

		Double deltaLatitude, deltaLongitude;

		x = side/2;
		y = Math.sqrt(Math.pow(side,2) - Math.pow(x, 2));
			
		System.out.printf("\n" + x + " " + y);

		deltaLatitude = y / heartRadius;
		deltaLongitude = x / heartRadius / Math.cos(baseLatitude);

		points.add(new LatLon(baseLatitude + deltaLatitude, baseLongitude + deltaLongitude));
		points.add(new LatLon(baseLatitude + deltaLatitude, baseLongitude - deltaLongitude));
		points.add(new LatLon(baseLatitude - deltaLatitude, baseLongitude - deltaLongitude));
		points.add(new LatLon(baseLatitude - deltaLatitude, baseLongitude + deltaLongitude));
		

		GoogleMapPolygon polygon = new GoogleMapPolygon(points, "#ae1f1f", 0.8, "#194915", 0.5, 3);
		return polygon;
	}

	public static GoogleMapPolygon createCircle(Double baseLatitude, Double baseLongitude , Double radius) {
		LinkedList<LatLon> points = new LinkedList<LatLon>();
		LinkedList<LatLon> points2 = new LinkedList<LatLon>();
		
		Double heartRadius = new Double(6371);
		// Double radius = new Double(5.00);
		Double x, y;

		Double deltaLatitude, deltaLongitude;

		for (x = -radius; x <= radius; x += 5.0) {
			y = Math.sqrt(Math.pow(radius, 2) - Math.pow(x, 2));
			System.out.printf("\n" + x + "     " + y);

			deltaLatitude = y / heartRadius;
			deltaLongitude = (x / heartRadius) / Math.cos(baseLatitude);

			points.add(new LatLon(baseLatitude + deltaLatitude, baseLongitude + deltaLongitude));
			points2.add(new LatLon(baseLatitude - deltaLatitude, baseLongitude - deltaLongitude));
		}
		
//		lat = lat0 + (180/pi)*(dy/6378137)
//		lon = lon0 + (180/pi)*(dx/6378137)/cos(lat0)
		points.addAll(points2);
		
//		Iterator<LatLon> itr = points2.descendingIterator();
//		while(itr.hasNext()) {
//			points.add(itr.next());
//		}

		GoogleMapPolygon polygon = new GoogleMapPolygon(points, "#ae1f1f", 0.8, "#194915", 0.5, 3);
		return polygon;
	}
	
	public static String getStringaIndrizzoGoogleMap(String indirizzo, String cap, String citta, String provincia) {//AnagraficaModel anagrafica
		String stringRicerca = pulisciStrigaPerIndirizzoGoogleMap(indirizzo, true) + "+"
				+ pulisciStrigaPerIndirizzoGoogleMap(cap, true) + "+"
				+ pulisciStrigaPerIndirizzoGoogleMap(citta, true) + "+"
				+ pulisciStrigaPerIndirizzoGoogleMap(provincia, true);
		
		if (stringRicerca.endsWith("+")) {
			stringRicerca = stringRicerca.substring(0, stringRicerca.length()-1);
		}
		
		return stringRicerca;
	}

	public static String pulisciStrigaPerIndirizzoGoogleMap(String stringa, boolean replaceSpaces) {
		if (stringa == null) {
			return "";
		}
		String stringaReturn = stringa.trim();
		stringaReturn = stringaReturn.replaceAll("\\.", "").replaceAll("'", " ").replaceAll("-", "").replaceAll(" +", " ");
		if (replaceSpaces) {
			stringaReturn = stringaReturn.replaceAll(" ", "+");
		}
		return stringaReturn;
	}
	
//	public void addRandomLocation(double baseLat, double baseLon, int radius) {
//	    Random random = new Random();
//
//	    // Convert radius from meters to degrees
//	    double radiusInDegrees = radius / 111000f;
//
//	    double u = random.nextDouble();
//	    double v = random.nextDouble();
//	    double w = radiusInDegrees * Math.sqrt(u);
//	    double t = 2 * Math.PI * v;
//	    double x = (w * Math.cos(t)) / Math.cos(baseLon);
//	    double y = w * Math.sin(t);
//
//	    double foundLatitude = x + baseLat;
//	    double foundLongitude = y + baseLon;
//	    addMarker("RANDOM!", foundLatitude, foundLongitude);
//	}


	/**
	 * NON USATO. SOLO LEGACY/SUPPORTO
	 */
	@SuppressWarnings("unused")
	private void usefulGeocodeApiParsing() throws XPathExpressionException {
		XPath xpath = XPathFactory.newInstance().newXPath();
		NodeList resultNodeList = null;
		Document geocoderResultDocument = null;

		// a) obtain the formatted_address field for every result
		resultNodeList = (NodeList) xpath.evaluate("/GeocodeResponse/result/formatted_address", geocoderResultDocument, XPathConstants.NODESET);
		for (int i = 0; i < resultNodeList.getLength(); ++i) {
			// System.out.println(resultNodeList.item(i).getTextContent());
		}

		// b) extract the locality for the first result
		resultNodeList = (NodeList) xpath.evaluate("/GeocodeResponse/result[1]/address_component[type/text()='locality']/long_name",
				geocoderResultDocument, XPathConstants.NODESET);
		for (int i = 0; i < resultNodeList.getLength(); ++i) {
			// System.out.println(resultNodeList.item(i).getTextContent());
		}

		// c) extract the coordinates of the first result
		resultNodeList = (NodeList) xpath.evaluate("/GeocodeResponse/result[1]/geometry/location/*", geocoderResultDocument, XPathConstants.NODESET);
		float lat = Float.NaN;
		float lng = Float.NaN;
		for (int i = 0; i < resultNodeList.getLength(); ++i) {
			Node node = resultNodeList.item(i);
			if ("lat".equals(node.getNodeName()))
				lat = Float.parseFloat(node.getTextContent());
			if ("lng".equals(node.getNodeName()))
				lng = Float.parseFloat(node.getTextContent());
		}
		// System.out.println("lat/lng=" + lat + "," + lng);

		// c) extract the coordinates of the first result
		resultNodeList = (NodeList) xpath.evaluate(
				"/GeocodeResponse/result[1]/address_component[type/text() = 'administrative_area_level_1']/country[short_name/text() = 'US']/*",
				geocoderResultDocument, XPathConstants.NODESET);
		lat = Float.NaN;
		lng = Float.NaN;
		for (int i = 0; i < resultNodeList.getLength(); ++i) {
			Node node = resultNodeList.item(i);
			if ("lat".equals(node.getNodeName()))
				lat = Float.parseFloat(node.getTextContent());
			if ("lng".equals(node.getNodeName()))
				lng = Float.parseFloat(node.getTextContent());
		}
		// System.out.println("lat/lng=" + lat + "," + lng);
	}
	
}
