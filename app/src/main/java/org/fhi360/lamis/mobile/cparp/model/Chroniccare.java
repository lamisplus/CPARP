package org.fhi360.lamis.mobile.cparp.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by aalozie on 3/19/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chroniccare {
    private int id;
    private int facilityId;
    private int patientId;
    private Date dateVisit;
    private String clientType;
    private String currentStatus;
    private String pregnancyStatus;
    private String clinicStage;
    private double lastCd4;
    private Date dateLastCd4;
    private double lastViralLoad;
    private Date dateLastViralLoad;
    private String eligibleCd4;
    private String eligibleViralLoad;
    private String cotrimEligibility;
    private String ipt;
    private String eligibleIpt;
    private String inh;
    private String tbTreatment;
    private Date dateStartedTbTreatment;
    private String tbReferred;
    private String tbValue1;
    private String tbValue2;
    private String tbValue3;
    private String tbValue4;
    private String tbValue5;
    private double bodyWeight;
    private double height;
    private double bmi;
    private String bmiCategory;
    private double muac;
    private String muacCategory;
    private String supplementaryFood;
    private String nutritionalStatusReferred;
    private String gbv1;
    private String gbv1Referred;
    private String gbv2;
    private String gbv2Referred;
    private String hypertensive;
    private String firstHypertensive;
    private String bp;
    private String bpAbove;
    private String bpReferred;
    private String diabetic;
    private String firstDiabetic;
    private String dmValue1;
    private String dmValue2;
    private String dmReferred;
    private String phdp1;
    private String phdp1ServicesProvided;
    private String phdp2;
    private String phdp3;
    private String phdp4;
    private String phdp4ServicesProvided;
    private String phdp5;
    private int phdp6;
    private int phdp7;
    private String phdp7ServicesProvided;
    private String phdp8ServicesProvided;
    private String additionalServicesProvided;
    private String reproductiveIntentions1;
    private String reproductiveIntentions1Referred;
    private String reproductiveIntentions2;
    private String reproductiveIntentions2Referred;
    private String reproductiveIntentions3;
    private String reproductiveIntentions3Referred;
    private String malariaPrevention1;
    private String malariaPrevention1Referred;
    private String malariaPrevention2;
    private String malariaPrevention2Referred;
    private Date timeStamp;

}
