package org.fhi360.lamis.mobile.cparp.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by aalozie on 6/28/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Htc {
    private int id;
    private int month;
    private int year;
    private int numTested;
    private int numPositive;
    private int numReferred;
    private int numOnsiteVisit;
    private Date timeStamp;

}
