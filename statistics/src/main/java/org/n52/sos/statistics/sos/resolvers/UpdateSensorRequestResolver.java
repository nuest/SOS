package org.n52.sos.statistics.sos.resolvers;

import javax.inject.Named;

import org.n52.sos.request.UpdateSensorRequest;
import org.n52.sos.statistics.sos.SosDataMapping;

@Named
public class UpdateSensorRequestResolver extends AbstractSosRequestResolver<UpdateSensorRequest> {

    @Override
    protected void resolveConcreteRequest()
    {
        put(SosDataMapping.US_PROCEDURE_IDENTIFIER, request.getProcedureIdentifier());
        put(SosDataMapping.US_PROCEDURE_DESCRIPTION_FORMAT, request.getProcedureDescriptionFormat());
    }

}
