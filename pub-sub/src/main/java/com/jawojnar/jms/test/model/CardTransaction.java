package com.jawojnar.jms.test.model;

import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
public class CardTransaction implements Serializable {

    private LocalDateTime time;
    private Double amountOfMoney;

    public CardTransaction(LocalDateTime time, Double amountOfMoney) {
        this.time = time;
        this.amountOfMoney = amountOfMoney;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Double getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(Double amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }

    @Override
    public String toString() {
        return "CardTransaction{" +
                "time=" + time +
                ", amountOfMoney=" + amountOfMoney +
                '}';
    }
}
