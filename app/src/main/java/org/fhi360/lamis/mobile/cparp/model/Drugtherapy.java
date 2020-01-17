package org.fhi360.lamis.mobile.cparp.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by aalozie on 6/4/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Drugtherapy {
    private int id;
    private int facilityId;
    private int patientId;
    private Date dateVisit;
    private String ois;
    private String therapyProblemScreened;
    private String adherenceIssues;
    private String medicationError;
    private String adrs;
    private String severity;
    private String icsrForm;
    private String adrReferred;
    private Date timeStamp;

  }
