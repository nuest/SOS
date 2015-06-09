package org.n52.sos.statistics.sos.handlers.exceptions;

import java.util.Map;

import org.n52.sos.exception.CodedException;
import org.n52.sos.statistics.api.AbstractElasticSearchDataHolder;
import org.n52.sos.statistics.api.interfaces.IEventHandler;
import org.n52.sos.statistics.sos.SosDataMapping;

public class SosCodedExceptionEventResolver extends AbstractElasticSearchDataHolder implements IEventHandler<CodedException> {

    @Override
    public Map<String, Object> resolveAsMap(CodedException exception)
    {
        if (exception.getStatus() != null) {
            put(SosDataMapping.CEX_STATUS, exception.getStatus().getCode());
        }
        put(SosDataMapping.CEX_LOCATOR, exception.getLocator());
        put(SosDataMapping.CEX_VERSION, exception.getVersion());
        if (exception.getCode() != null) {
            put(SosDataMapping.CEX_SOAP_FAULT, exception.getCode().getSoapFaultReason());
        }
        put(SosDataMapping.CEX_MESSAGE, exception.getMessage());
        return dataMap;
    }
}
