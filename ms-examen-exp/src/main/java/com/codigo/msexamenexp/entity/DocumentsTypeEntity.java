package com.codigo.msexamenexp.entity;

import com.codigo.msexamenexp.entity.common.Audit;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@NamedQuery(name = "DocumentsTypeEntity.findByCodType", query = "select a from DocumentsTypeEntity a where a.codType=:codType")
@Entity
@Getter
@Setter
@Table(name = "documents_type")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentsTypeEntity extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documents_type")
    private int idDocumentsType;
    @Column(name = "cod_type", length = 45,nullable = false)
    private String codType;
    @Column(name = "desc_type",length = 45,nullable = false)
    private String descType;
    @Column(name = "status",nullable = false)
    private int status;
}
