package org.n52.sos.statistics.sos.resolvers;

import java.util.Map;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.n52.sos.decode.json.JSONDecodingException;
import org.n52.sos.statistics.MockitoBaseTest;
import org.n52.sos.statistics.api.interfaces.IStatisticsDataHandler;
import org.n52.sos.statistics.sos.SosDataMapping;

public class SosExceptionEventResolverTest extends MockitoBaseTest {
    @Mock
    IStatisticsDataHandler dataHandler;

    @Inject
    @InjectMocks
    SosExceptionEventResolver eventResolver;

    @Captor
    ArgumentCaptor<Map<String, Object>> captor;

    @Test
    public void codedExceptionPersist() throws Exception
    {
        JSONDecodingException ex = new JSONDecodingException("i'm jason bourne you can't decode me");
        eventResolver.setException(ex);
        eventResolver.run();

        Mockito.verify(dataHandler).persist(captor.capture());
        Assert.assertTrue(captor.getValue().get(SosDataMapping.CEX_MESSAGE).equals("i'm jason bourne you can't decode me"));
    }
}
