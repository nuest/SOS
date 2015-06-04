package org.n52.sos.statistics.sos.resolvers;

import javax.inject.Named;

import org.n52.sos.request.GetFeatureOfInterestRequest;
import org.n52.sos.statistics.sos.SosDataMapping;

@Named
public class GetFeatureOfInterestRequestResolver extends AbstractSosRequestResolver<GetFeatureOfInterestRequest> {

    @Override
    protected void resolveConcreteRequest()
    {
        put(SosDataMapping.GFOI_FEATURE_IDENTIFIERS, request.getFeatureIdentifiers());
        put(SosDataMapping.GFOI_NAMESPACES, request.getNamespaces());
        put(SosDataMapping.GFOI_OBSERVED_PROPERTIES, request.getObservedProperties());
        put(SosDataMapping.GFOI_PROCEDURES, request.getProcedures());
        // TODO spatial filters
        // TODO temporal filters
    }
}
