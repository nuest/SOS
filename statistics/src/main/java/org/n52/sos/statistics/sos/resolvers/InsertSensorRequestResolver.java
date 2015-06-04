package org.n52.sos.statistics.sos.resolvers;

import javax.inject.Named;

import org.n52.sos.request.InsertSensorRequest;
import org.n52.sos.statistics.sos.SosDataMapping;

@Named
public class InsertSensorRequestResolver extends AbstractSosRequestResolver<InsertSensorRequest> {

    @Override
    protected void resolveConcreteRequest()
    {
        put(SosDataMapping.IS_ASSIGNED_OFFERINGS, request.getAssignedOfferings());
        put(SosDataMapping.IS_ASSIGNED_PROCEDURE_IDENTIFIERS, request.getAssignedProcedureIdentifier());
        put(SosDataMapping.IS_OBSERVABLE_PROPERTY, request.getObservableProperty());
        put(SosDataMapping.IS_PROCEDURE_DESCRIPTION, request.getProcedureDescription());
        put(SosDataMapping.IS_PROCEDURE_DESCRIPTION_FORMAT, request.getProcedureDescriptionFormat());
    }

}
