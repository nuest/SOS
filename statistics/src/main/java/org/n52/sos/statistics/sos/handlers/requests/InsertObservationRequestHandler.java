package org.n52.sos.statistics.sos.handlers.requests;

import javax.inject.Named;

import org.n52.sos.request.InsertObservationRequest;
import org.n52.sos.statistics.sos.SosDataMapping;

@Named
public class InsertObservationRequestHandler extends AbstractSosRequestHandler<InsertObservationRequest> {

    @Override
    protected void resolveConcreteRequest()
    {
        put(SosDataMapping.IO_ASSIGNED_SENSORID, request.getAssignedSensorId());
        put(SosDataMapping.IO_OFFERINGS, request.getOfferings());
    }

}
