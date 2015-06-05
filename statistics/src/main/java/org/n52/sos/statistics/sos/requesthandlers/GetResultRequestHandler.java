package org.n52.sos.statistics.sos.requesthandlers;

import javax.inject.Named;

import org.n52.sos.request.GetResultRequest;
import org.n52.sos.statistics.sos.SosDataMapping;

@Named
public class GetResultRequestHandler extends AbstractSosRequestHandler<GetResultRequest> {

    @Override
    protected void resolveConcreteRequest()
    {
        put(SosDataMapping.GR_FEATURE_IDENTIFIERS, request.getFeatureIdentifiers());
        put(SosDataMapping.GR_NAMSPACES, request.getNamespaces());
        put(SosDataMapping.GR_OBSERVATION_TEMPLATE_IDENTIFIER, request.getObservationTemplateIdentifier());
        put(SosDataMapping.GR_OBSERVATION_PROPERTY, request.getObservedProperty());
        put(SosDataMapping.GR_OFFERING, request.getOffering());
        // TODO add spatial filter
        // TODO add temporal filter
    }
}
