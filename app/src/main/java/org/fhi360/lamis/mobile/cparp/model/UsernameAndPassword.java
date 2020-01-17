package org.fhi360.lamis.mobile.cparp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsernameAndPassword {
    private String accountUserName;
    private String accountPassword;
}
