package org.n52.sos.statistics.sos.resolvers;

import javax.inject.Named;

import org.n52.sos.request.GetResultTemplateRequest;
import org.n52.sos.statistics.sos.SosDataMapping;

@Named
public class GetResultTemplateRequestResolver extends AbstractSosRequestResolver<GetResultTemplateRequest> {

    @Override
    protected void resolveConcreteRequest()
    {
        put(SosDataMapping.GRT_OBSERVED_PROPERTY, request.getObservedProperty());
        put(SosDataMapping.GRT_OFFERING, request.getOffering());
    }

}
