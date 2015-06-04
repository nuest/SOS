package org.n52.sos.statistics.sos.resolvers;

import javax.inject.Named;

import org.n52.sos.request.GetObservationByIdRequest;
import org.n52.sos.statistics.sos.SosDataMapping;

@Named
public class GetObservationByIdRequestResolver extends AbstractSosRequestResolver<GetObservationByIdRequest> {

    @Override
    protected void resolveConcreteRequest()
    {
        put(SosDataMapping.GOBID_OBSERVATION_IDENTIFIER, request.getObservationIdentifier());
    }

}
