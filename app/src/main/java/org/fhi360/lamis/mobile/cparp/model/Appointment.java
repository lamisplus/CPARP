package org.fhi360.lamis.mobile.cparp.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    private int id;
    private int facilityId;
    private int patientId;
    private Date dateTracked;
    private String typeTracking;
    private String trackingOutcome;
    private Date dateLastVisit;
    private Date dateNextVisit;
    private Date dateAgreed;
    private int communitypharmId;



}
