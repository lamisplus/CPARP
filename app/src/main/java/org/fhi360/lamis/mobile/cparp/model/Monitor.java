package org.fhi360.lamis.mobile.cparp.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by aalozie on 6/28/2017.
 */
@Data
@AllArgsConstructor
public class Monitor {
    private int facilityId;
    private String entityId;
    private String tableName;
    private int operationId;
    private Date timeStamp;

}
