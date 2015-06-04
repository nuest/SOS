package org.n52.sos.statistics.sos.resolvers;

import javax.inject.Named;

import org.n52.sos.gda.GetDataAvailabilityRequest;
import org.n52.sos.statistics.sos.SosDataMapping;

@Named
public class GetDataAvailabilityRequestResolver extends AbstractSosRequestResolver<GetDataAvailabilityRequest> {

    @Override
    protected void resolveConcreteRequest()
    {
        put(SosDataMapping.GDA_FEATURES_OF_INTEREST, request.getFeaturesOfInterest());
        put(SosDataMapping.GDA_NAMESPACE, request.getNamespace());
        put(SosDataMapping.GDA_OBSERVED_PROPERTIES, request.getObservedProperties());
        put(SosDataMapping.GDA_OFFERINGS, request.getOfferings());
        put(SosDataMapping.GDA_PROCEDURES, request.getProcedures());
    }
}
