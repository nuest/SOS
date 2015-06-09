package org.n52.sos.statistics.sos.handlers.requests;

import javax.inject.Named;

import org.n52.sos.request.GetObservationByIdRequest;
import org.n52.sos.statistics.sos.SosDataMapping;

@Named
public class GetObservationByIdRequestHandler extends AbstractSosRequestHandler<GetObservationByIdRequest> {

    @Override
    protected void resolveConcreteRequest()
    {
        put(SosDataMapping.GOBID_OBSERVATION_IDENTIFIER, request.getObservationIdentifier());
    }

}
