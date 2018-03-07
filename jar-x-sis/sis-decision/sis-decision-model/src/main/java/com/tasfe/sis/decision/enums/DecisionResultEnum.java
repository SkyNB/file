package com.tasfe.sis.decision.enums;

/**
 * Created by dongruixi on 2017/12/6.
 */
public enum DecisionResultEnum {
    OK("OK",0),NOK("NG",1);

    String name;
    int value;


    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }



    private DecisionResultEnum(String name, int value){
        this.name=name;
        this.value=value;

    }

}
