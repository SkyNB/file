package com.tasfe.sis.base.exception;

import com.tasfe.sis.base.model.code.ErrorCode;

/**
 * Created by dongruixi on 2016/10/2.
 */
public class BizException extends Exception {
    private ErrorCode err;
    public BizException(ErrorCode err) {
    	super("BizException-code>>>"+err.getCode()+">>>BizException-msg>>>"+err.getMessage());
        this.err = err;
    }

    public ErrorCode getErr() {
        return err;
    }
}
