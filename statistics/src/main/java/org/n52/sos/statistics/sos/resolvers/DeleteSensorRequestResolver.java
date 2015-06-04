package org.n52.sos.statistics.sos.resolvers;

import javax.inject.Named;

import org.n52.sos.request.DeleteSensorRequest;
import org.n52.sos.statistics.sos.SosDataMapping;

@Named
public class DeleteSensorRequestResolver extends AbstractSosRequestResolver<DeleteSensorRequest> {

    @Override
    protected void resolveConcreteRequest()
    {
        put(SosDataMapping.DELS_PROCEDURE_IDENTIFIER, request.getProcedureIdentifier());
    }

}
