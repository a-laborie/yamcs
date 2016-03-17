package org.yamcs.tctm;

import java.io.IOException;

import org.hornetq.api.core.HornetQException;
import org.yamcs.ConfigurationException;
import org.yamcs.api.YamcsApiException;
import org.yamcs.yarch.streamsql.ParseException;
import org.yamcs.yarch.streamsql.StreamSqlException;


/**
 * Loads multiple TmPacketSource and inject all the packets into a defined stream
 * @author nm
 *
 *@deprecated please use {@link DataLinkInitialiser} instead
 */
@Deprecated
public class TmProviderAdapter extends TmDataLinkInitialiser {
    public TmProviderAdapter(String yamcsInstance) throws ConfigurationException, StreamSqlException, ParseException, HornetQException, YamcsApiException, IOException {
        super(yamcsInstance);
    }
}
