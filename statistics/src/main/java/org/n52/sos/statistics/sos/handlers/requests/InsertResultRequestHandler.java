package org.n52.sos.statistics.sos.handlers.requests;

import javax.inject.Named;

import org.n52.sos.request.InsertResultRequest;
import org.n52.sos.statistics.sos.SosDataMapping;

@Named
public class InsertResultRequestHandler extends AbstractSosRequestHandler<InsertResultRequest> {

    @Override
    protected void resolveConcreteRequest()
    {
        put(SosDataMapping.IR_TEMPLATE_IDENTIFIER, request.getTemplateIdentifier());
    }

}
