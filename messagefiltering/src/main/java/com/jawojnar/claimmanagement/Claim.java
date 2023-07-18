package com.jawojnar.claimmanagement;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
public class Claim implements Serializable {

    private int hospitalId;
    private String doctorName;
    private String doctorType;
    private String insuranceProvider;
    private double amount;

    @Override
    public String toString() {
        return "Claim{" +
                "hospitalId=" + hospitalId +
                ", doctorName='" + doctorName + '\'' +
                ", doctorType='" + doctorType + '\'' +
                ", insuranceProvider='" + insuranceProvider + '\'' +
                ", amount=" + amount +
                '}';
    }
}