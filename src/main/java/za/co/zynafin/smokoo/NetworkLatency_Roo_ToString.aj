// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package za.co.zynafin.smokoo;

import java.lang.String;

privileged aspect NetworkLatency_Roo_ToString {
    
    public String NetworkLatency.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Version: ").append(getVersion()).append(", ");
        sb.append("Time: ").append(getTime()).append(", ");
        sb.append("Delay: ").append(getDelay());
        return sb.toString();
    }
    
}
