package org.n52.sos.statistics.sos.resolvers;

import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import org.n52.sos.exception.CodedException;
import org.n52.sos.statistics.api.interfaces.IStatisticsDataHandler;
import org.n52.sos.statistics.api.interfaces.IStatisticsEventResolver;
import org.n52.sos.statistics.impl.ElasticSearchDataHandler;
import org.n52.sos.statistics.sos.handlers.exceptions.SosCodedExceptionEventResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SosExceptionEventResolver implements IStatisticsEventResolver {

    private static final Logger logger = LoggerFactory.getLogger(SosExceptionEventResolver.class);

    private Exception exception;

    // FIXME remove new object in DI environment
    @Inject
    private IStatisticsDataHandler dataHandler = ElasticSearchDataHandler.getInstance();

    private SosCodedExceptionEventResolver codedExceptionEventResolver = new SosCodedExceptionEventResolver();

    @Override
    public void run()
    {
        Objects.requireNonNull(exception);
        resolve();
    }

    private void resolve()
    {
        Map<String, Object> dataMap = null;
        if (exception instanceof CodedException) {
            dataMap = codedExceptionEventResolver.resolveAsMap((CodedException) exception);
        } else {
            logger.warn("No appropriate ExceptionEventResolver for type {}", exception.getClass());
        }
        try {

            dataHandler.persist(dataMap);
        } catch (Throwable e) {
            logger.error("Can't persist request", e);
        }
    }

    public Exception getException()
    {
        return exception;
    }

    public void setException(Exception exception)
    {
        this.exception = exception;
    }

}
