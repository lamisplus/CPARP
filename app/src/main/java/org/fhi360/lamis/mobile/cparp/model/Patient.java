package org.fhi360.lamis.mobile.cparp.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by aalozie on 2/22/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient {
    private int id;
    private int facilityId;
    private int patientId;
    private String hospitalNum;
    private String uniqueId;
    private String surname;
    private String otherNames;
    private String gender;
    private Date dateBirth;
    private String address;
    private String phone;
    private Date dateStarted;
    private String lastClinicStage;
    private String regimentype;
    private String regimen;
    private double lastViralLoad;
    private Date dateLastViralLoad;
    private Date viralLoadDueDate;
    private String viralLoadType;
    private double lastCd4;
    private Date dateLastCd4;
    private Date dateLastRefill;
    private Date dateNextRefill;
    private Date dateLastClinic;
    private Date dateNextClinic;
    private String lastRefillSetting;
    private int discontinued;

}
