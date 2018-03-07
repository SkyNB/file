package com.tasfe.sis.bank.model.transfer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixiaofeng on 2017-8-15.
 */
public class TransferCollection extends ArrayList<TransferInfo> implements List<TransferInfo> {

    @Override
    public String toString(){
        List<String> l = new ArrayList<>();
        this.forEach((s)->{
            l.add(s.toString());
        });
        return String.join("~~",l);
    }

}
