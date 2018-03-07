package com.tasfe.sis.application.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by dongruixi on 2017/12/27.
 */
@Data
public class IdentityPhotoDTO implements Serializable {

    String type;
    String photo;
    String extensions="png";
}
