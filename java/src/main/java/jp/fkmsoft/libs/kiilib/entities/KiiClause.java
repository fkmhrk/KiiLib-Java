package jp.fkmsoft.libs.kiilib.entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class KiiClause {

    public static KiiClause all() {
        return new KiiClause(TYPE_ALL);
    }
    
    public static <T> KiiClause equals(String field, T value) {
        KiiClause clause = new KiiClause(TYPE_EQUAL);
        try {
            clause.json.put(FIELD_FIELD, field);
            clause.json.put(FIELD_VALUE, value);
        } catch (JSONException e) {
            // nop
        }
        
        return clause;
    }
    
    public static <T> KiiClause greaterThan(String field, T value, boolean included) {
        KiiClause clause = new KiiClause(TYPE_RANGE);
        try {
            clause.json.put(FIELD_FIELD, field);
            clause.json.put(FIELD_LOWER_LIMIT, value);
            clause.json.put(FIELD_LOWER_INCLUDED, included);
        } catch (JSONException e) {
            // nop
        }
        
        return clause;
    }
    
    public static <T> KiiClause lessThan(String field, T value, boolean included) {
        KiiClause clause = new KiiClause(TYPE_RANGE);
        try {
            clause.json.put(FIELD_FIELD, field);
            clause.json.put(FIELD_UPPER_LIMIT, value);
            clause.json.put(FIELD_UPPER_INCLUDED, included);
        } catch (JSONException e) {
            // nop
        }
        
        return clause;
    }

    public static KiiClause geoBox(String field, double nwLat, double nwLon, double seLat, double seLon) {
        KiiClause clause = new KiiClause(TYPE_GEO_BOX);
        try {
            clause.json.put(FIELD_FIELD, field);
            clause.json.put(FIELD_BOX, createBox(createPoint(nwLat, nwLon), createPoint(seLat, seLon)));
        } catch (JSONException e) {
            // nop
        }

        return clause;
    }

    public static KiiClause geoDistance(String field, double centerLat, double centerLon, double radius) {
        KiiClause clause = new KiiClause(TYPE_GEO_DISTANCE);
        try {
            clause.json.put(FIELD_FIELD, field);
            clause.json.put(FIELD_CENTER, createPoint(centerLat, centerLon));
            clause.json.put(FIELD_RADIUS, radius);
        } catch (JSONException e) {
            // nop
        }

        return clause;
    }

    private static JSONObject createBox(JSONObject northWest, JSONObject southEast) {
        JSONObject json = new JSONObject();
        try {
            json.put(FIELD_NORTH_WEST, northWest);
            json.put(FIELD_SOUTH_EAST, southEast);
        } catch (JSONException e) {
            // nop
        }

        return json;
    }

    private static JSONObject createPoint(double latitude, double longitude) {
        JSONObject json = new JSONObject();
        try {
            json.put("_type", "point");
            json.put(FIELD_LATITUDE, latitude);
            json.put(FIELD_LONGITUDE, longitude);
        } catch (JSONException e) {
            // nop
        }
        return json;
    }
    
    public static KiiClause and(KiiClause... clauses) {
        KiiClause clause = new KiiClause(TYPE_AND);
        
        try {
            clause.json.put(FIELD_CLAUSES, toClauseArray(clauses));
        } catch (JSONException e) {
            // nop
        }
        
        return clause;
    }
    
    public static KiiClause or(KiiClause... clauses) {
        KiiClause clause = new KiiClause(TYPE_OR);
        
        try {
            clause.json.put(FIELD_CLAUSES, toClauseArray(clauses));
        } catch (JSONException e) {
            // nop
        }
        
        return clause;
    }    
    
    private static JSONArray toClauseArray(KiiClause[] clauses) {
        JSONArray array = new JSONArray();
        for (KiiClause item : clauses) {
            array.put(item.toJson());
        }
        return array;
    }
    
    public static KiiClause in(String field, String... values) {
        KiiClause clause = new KiiClause(TYPE_IN);
        
        try {
            JSONArray array = new JSONArray();
            for (String value : values) {
                array.put(value);
            }
            
            clause.json.put(FIELD_FIELD, field);
            clause.json.put(FIELD_VALUES, array);
        } catch (JSONException e) {
            // nop
        }
        
        return clause;
    }
    
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_FIELD = "field";
    private static final String FIELD_VALUE = "value";
    private static final String FIELD_VALUES = "values";
    private static final String FIELD_LOWER_LIMIT = "lowerLimit";
    private static final String FIELD_LOWER_INCLUDED = "lowerIncluded";
    private static final String FIELD_UPPER_LIMIT = "upperLimit";
    private static final String FIELD_UPPER_INCLUDED = "upperIncluded";
    private static final String FIELD_BOX = "box";
    private static final String FIELD_NORTH_WEST = "nw";
    private static final String FIELD_SOUTH_EAST = "se";
    private static final String FIELD_CENTER = "center";
    private static final String FIELD_RADIUS = "radius";
    private static final String FIELD_LATITUDE = "lat";
    private static final String FIELD_LONGITUDE = "lon";
    private static final String FIELD_CLAUSES = "clauses";
    
    private static final String TYPE_ALL = "all";
    private static final String TYPE_EQUAL = "eq";
    private static final String TYPE_AND = "and";
    private static final String TYPE_OR = "or";
    private static final String TYPE_IN = "in";
    private static final String TYPE_RANGE = "range";
    private static final String TYPE_GEO_BOX = "geobox";
    private static final String TYPE_GEO_DISTANCE = "geodistance";
    

    private final JSONObject json = new JSONObject();
    
    private KiiClause(String type) {
        try {
            json.put(FIELD_TYPE, type);
        } catch (JSONException e) {
            /* nop */
        }
    }
    
    public JSONObject toJson() {
        return json;
    }
}
