package com.ray.lib_map.impl.google;

import android.location.Location;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.ray.lib_map.entity.Address;
import com.ray.lib_map.entity.MapPoint;
import com.ray.lib_map.entity.Poi;
import com.ray.lib_map.extern.MapType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author      : leixing
 * Date        : 2017-10-20
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

class GoogleDataConverter {
    private GoogleDataConverter() {
        throw new UnsupportedOperationException();
    }


    static MapPoint toMapPoint(LatLng latLng) {
        return new MapPoint(latLng.latitude, latLng.longitude, MapType.GOOGLE.getCoordinateType());
    }

    static LatLng fromMapPoint(MapPoint mapPoint) {
        MapPoint googleMapPoint = mapPoint.copy(MapType.GOOGLE.getCoordinateType());
        return new LatLng(googleMapPoint.getLatitude(), googleMapPoint.getLongitude());
    }

    static List<LatLng> fromMapPoints(List<MapPoint> points) {
        List<LatLng> latLngList = new ArrayList<>();
        if (points == null) {
            return latLngList;
        }
        for (MapPoint mapPoint : points) {
            latLngList.add(fromMapPoint(mapPoint));
        }
        return latLngList;
    }

    static Address locationToAddress(Location location) {
        return null;
    }

    static Address placeLikelihoodToAddress(Place place) {
        Address address = new Address();
        LatLng latLng = place.getLatLng();
        address.setMapPoint(toMapPoint(latLng));
        address.setFormattedAddress(place.getAddress().toString());
        address.setName(place.getName().toString());

        return address;
    }

    static MapPoint geoResultToMapPoint(String response) {

        try {
            JSONObject result;
            result = new JSONObject(response);
            if (!result.has("status")) {
                return null;
            }
            // TODO: 2017-10-23 汇报错误原因
            if (!("OK").equals(result.getString("status"))) {
                return null;
            }

            if (!result.has("results")) {
                return null;
            }
            JSONArray results = result.getJSONArray("results");
            if (results == null || results.length() < 1) {
                return null;
            }
            JSONObject address = (JSONObject) results.get(0);
            if (!address.has("geometry")) {
                return null;
            }
            JSONObject geometry = address.getJSONObject("geometry");
            if (!geometry.has("location")) {
                return null;
            }
            JSONObject location = geometry.getJSONObject("location");
            double latitude = location.getDouble("lat");
            double longitude = location.getDouble("lng");
            return new MapPoint(latitude, longitude, MapType.GOOGLE.getCoordinateType());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    static Address reGeoResultToAddress(String response) {
        try {
            JSONObject result = new JSONObject(response);

            if (!result.has("status")) {
                return null;
            }
            // TODO: 2017-10-23 汇报错误原因
            if (!("OK").equals(result.getString("status"))) {
                return null;
            }

            if (!result.has("results")) {
                return null;
            }
            JSONArray results = result.getJSONArray("results");
            if (results == null || results.length() < 1) {
                return null;
            }

            JSONObject addressObject = (JSONObject) results.get(0);

            if (!addressObject.has("address_components")) {
                return null;
            }

            Address address = new Address();
            JSONArray components = addressObject.getJSONArray("address_components");
            List<AddressComponent> addressComponents = parseAddressComponents(components);
            StringBuilder nameBuilder = new StringBuilder();

            for (int i = addressComponents.size() - 1; i >= 0; i--) {
                AddressComponent component = addressComponents.get(i);
                String name = component.getLongName();
                if (component.containsType("postal_code")) {
                    continue;
                }

                if (component.containsType("country")) {
                    address.setCountry(name);
                } else if (component.containsType("administrative_area_level_1")) {
                    address.setProvince(name);
                } else if (component.containsType("locality")) {
                    address.setCity(name);
                } else if (component.containsType("sublocality_level_1") || component.containsType("sublocality")) {
                    address.setDistrict(name);
                } else {
                    nameBuilder.append(name);
                }
            }
            address.setName(nameBuilder.toString());

            if (addressObject.has("formatted_address")) {
                address.setFormattedAddress(addressObject.getString("formatted_address"));
            }
            if (addressObject.has("geometry")) {
                JSONObject geometry = addressObject.getJSONObject("geometry");
                if (geometry.has("location")) {
                    JSONObject location = geometry.getJSONObject("location");
                    double latitude = location.getDouble("lat");
                    double longitude = location.getDouble("lng");
                    address.setMapPoint(new MapPoint(latitude, longitude, MapType.GOOGLE.getCoordinateType()));
                }
            }

            return address;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static List<AddressComponent> parseAddressComponents(JSONArray components) {
        List<AddressComponent> addressComponents = new ArrayList<>();

        for (int i = 0, size = components.length(); i < size; i++) {
            try {
                JSONObject jsonObject = (JSONObject) components.get(i);
                if (jsonObject == null) {
                    continue;
                }
                addressComponents.add(parseAddressComponent(jsonObject));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return addressComponents;
    }

    private static AddressComponent parseAddressComponent(JSONObject jsonObject) {
        AddressComponent addressComponent = new AddressComponent();

        try {
            if (jsonObject.has("long_name")) {
                addressComponent.setLongName(jsonObject.getString("long_name"));
            }

            if (jsonObject.has("short_name")) {
                addressComponent.setShortName(jsonObject.getString("short_name"));
            }
            if (jsonObject.has("types")) {
                JSONArray types = jsonObject.getJSONArray("types");
                for (int i = 0, size = types.length(); i < size; i++) {
                    addressComponent.addType(types.getString(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return addressComponent;
    }

    static List<Poi> nearbySearchResponseToPois(String response) {
        List<Poi> pois = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (!jsonObject.has("status")) {
                return null;
            }
            if (!"OK".equals(jsonObject.getString("status"))) {
                return null;
            }
            if (!jsonObject.has("results")) {
                return null;
            }
            JSONArray results = jsonObject.getJSONArray("results");
            for (int i = 0, size = results.length(); i < size; i++) {
                JSONObject result = results.getJSONObject(i);
                Poi poi = searchResultToPoi(result);
                if (poi != null) {
                    pois.add(poi);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pois;
    }

    private static Poi searchResultToPoi(JSONObject result) {
        try {
            Poi poi = new Poi();
            if (result.has("geometry")) {
                JSONObject geometry = result.getJSONObject("geometry");
                if (geometry.has("location")) {
                    JSONObject location = geometry.getJSONObject("location");
                    double latitude = location.getDouble("lat");
                    double longitude = location.getDouble("lng");
                    poi.setMapPoint(new MapPoint(latitude, longitude, MapType.GOOGLE.getCoordinateType()));
                }
            }
            if (result.has("name")) {
                poi.setName(result.getString("name"));
            }
            if (result.has("vicinity")) {
                poi.setAddress(result.getString("vicinity"));
            } else if (result.has("formatted_address")) {
                poi.setAddress(result.getString("formatted_address"));
            }
            return poi;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
