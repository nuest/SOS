package org.n52.sos.statistics.sos.resolvers;

import javax.inject.Named;

import org.n52.sos.request.InsertResultRequest;
import org.n52.sos.statistics.sos.SosDataMapping;

@Named
public class InsertResultRequestResolver extends AbstractSosRequestResolver<InsertResultRequest> {

    @Override
    protected void resolveConcreteRequest()
    {
        put(SosDataMapping.IR_TEMPLATE_IDENTIFIER, request.getTemplateIdentifier());
    }

}
