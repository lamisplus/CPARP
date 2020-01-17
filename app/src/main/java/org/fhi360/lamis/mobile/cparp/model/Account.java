package org.fhi360.lamis.mobile.cparp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by aalozie on 7/28/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private int id;
    private int communitypharmId;
    private String pharmacy;
    private String address;
    private String phone;
    private String email;
    private String pin;

}
