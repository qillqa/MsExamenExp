package com.codigo.msexamenexp.aggregates.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestEnterprises {
    private String numDocument;
    private String businessName;
    private String tradeName;
    private int enterprisesTypeEntity;
    private int documentsTypeEntity;
}
