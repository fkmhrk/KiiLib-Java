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
    private static final String FIELD_CLAUSES = "clauses";
    
    private static final String TYPE_ALL = "all";
    private static final String TYPE_EQUAL = "eq";
    private static final String TYPE_AND = "and";
    private static final String TYPE_OR = "or";
    private static final String TYPE_IN = "in";
    private static final String TYPE_RANGE = "range";
    

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
