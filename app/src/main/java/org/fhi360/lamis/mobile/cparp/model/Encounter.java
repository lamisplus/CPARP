package org.fhi360.lamis.mobile.cparp.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by aalozie on 2/25/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Encounter {
    private int id;
    private int facilityId;
    private int patientId;
    private Date dateVisit;
    private String question1;
    private String question2;
    private String question3;
    private String question4;
    private String question5;
    private String question6;
    private String question7;
    private String question8;
    private String question9;
    private String regimentype;
    private String regimen1;
    private String regimen2;
    private String regimen3;
    private String regimen4;
    private int duration1;
    private int duration2;
    private int duration3;
    private int duration4;
    private int prescribed1;
    private int prescribed2;
    private int prescribed3;
    private int prescribed4;
    private int dispensed1;
    private int dispensed2;
    private int dispensed3;
    private int dispensed4;
    private String notes;
    private Date nextRefill;
    private Date timeStamp;



}
