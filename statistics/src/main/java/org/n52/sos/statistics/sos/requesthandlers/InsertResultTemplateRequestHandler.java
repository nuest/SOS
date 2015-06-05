package org.n52.sos.statistics.sos.requesthandlers;

import javax.inject.Named;

import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.request.InsertResultTemplateRequest;
import org.n52.sos.statistics.sos.SosDataMapping;

@Named
public class InsertResultTemplateRequestHandler extends AbstractSosRequestHandler<InsertResultTemplateRequest> {

    @Override
    protected void resolveConcreteRequest()
    {
        put(SosDataMapping.IRT_IDENTIFIER, request.getIdentifier());
        try {
            put(SosDataMapping.IRT_RESULT_ENCODING, request.getResultEncoding().getXml());
        } catch (OwsExceptionReport e) {
            logger.error("{} cant get encoding {}", getClass(), e);
        }
    }

}
