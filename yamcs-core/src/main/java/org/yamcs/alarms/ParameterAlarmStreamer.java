package org.yamcs.alarms;

import java.util.ArrayList;

import org.yamcs.StandardTupleDefinitions;
import org.yamcs.parameter.ParameterValue;
import org.yamcs.yarch.DataType;
import org.yamcs.yarch.Stream;

public class ParameterAlarmStreamer extends AlarmStreamer<ParameterValue> {

    static public final String CNAME_TRIGGER = "triggerPV";
    static public final String CNAME_CLEAR = "clearPV";
    static public final String CNAME_SEVERITY_INCREASED = "severityIncreasedPV";
    
    public ParameterAlarmStreamer(Stream s) {
        super(s, DataType.PARAMETER_VALUE, StandardTupleDefinitions.PARAMETER_ALARM);
    }

    protected ArrayList<Object> getTupleKey(ActiveAlarm<ParameterValue> activeAlarm, AlarmNotificationType e) {
        ArrayList<Object> al = new ArrayList<>(7);

        // triggerTime
        al.add(activeAlarm.getTriggerValue().getGenerationTime());
        // parameter
        al.add(activeAlarm.getTriggerValue().getParameter().getQualifiedName());
        // seqNum
        al.add(activeAlarm.getId());
        // event
        al.add(e.name());

        return al;
    }

    @Override
    protected Object getYarchValue(ParameterValue pv) {
        return pv;
    }
    
    @Override
    protected String getColNameClear() {
        return CNAME_CLEAR;
    }

    @Override
    protected String getColNameTrigger() {
        return CNAME_TRIGGER;
    }

    @Override
    protected String getColNameSeverityIncreased() {
        return CNAME_SEVERITY_INCREASED;
    }

}
