package com.codigo.msexamenexp.repository;

import com.codigo.msexamenexp.entity.DocumentsTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface DocumentsTypeRepository extends JpaRepository<DocumentsTypeEntity, Integer> {
    DocumentsTypeEntity findByCodType(@Param("codType") String codType);
    DocumentsTypeEntity existsByCodType(String code);
}
