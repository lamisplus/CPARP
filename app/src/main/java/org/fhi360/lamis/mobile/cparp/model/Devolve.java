package org.fhi360.lamis.mobile.cparp.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by aalozie on 6/13/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Devolve {
    private int id;
    private int facilityId;
    private int patientId;
    private Date dateDevolved;
    private String viralLoadAssessed;
    private double lastViralLoad;
    private Date dateLastViralLoad;
    private String cd4Assessed;
    private double lastCd4;
    private Date dateLastCd4;
    private String lastClinicStage;
    private Date dateLastClinicStage;
    private String arvDispensed;
    private String regimentype;
    private String regimen;
    private Date dateLastRefill;
    private Date dateNextRefill;
    private Date dateLastClinic;
    private Date dateNextClinic;
    private String notes;
    private Date dateDiscontinued;
    private String reasonDiscontinued;


}
