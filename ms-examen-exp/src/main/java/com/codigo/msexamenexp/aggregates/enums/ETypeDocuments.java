package com.codigo.msexamenexp.aggregates.enums;

import lombok.Getter;

@Getter
public enum ETypeDocuments {
    RUC(01);

    private final int value;
    ETypeDocuments(int value) {
        this.value = value;
    }
}
