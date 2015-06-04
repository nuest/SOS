package org.n52.sos.statistics.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractElasticSearchDataHolder {
    protected final Map<String, Object> dataMap;

    public AbstractElasticSearchDataHolder() {
        dataMap = new HashMap<String, Object>();
    }

    public Map<String, Object> put(String key,
            Object value)
    {
        if (key == null || value == null) {
            return dataMap;
        }
        // do not insert empty maps
        if (value instanceof Collection<?> && ((Collection<?>) value).isEmpty()) {
            return dataMap;
        }
        dataMap.put(key, value);
        return dataMap;
    }

}
