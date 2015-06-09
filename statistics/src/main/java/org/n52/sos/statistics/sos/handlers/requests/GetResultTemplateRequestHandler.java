package org.n52.sos.statistics.sos.handlers.requests;

import javax.inject.Named;

import org.n52.sos.request.GetResultTemplateRequest;
import org.n52.sos.statistics.sos.SosDataMapping;

@Named
public class GetResultTemplateRequestHandler extends AbstractSosRequestHandler<GetResultTemplateRequest> {

    @Override
    protected void resolveConcreteRequest()
    {
        put(SosDataMapping.GRT_OBSERVED_PROPERTY, request.getObservedProperty());
        put(SosDataMapping.GRT_OFFERING, request.getOffering());
    }

}
