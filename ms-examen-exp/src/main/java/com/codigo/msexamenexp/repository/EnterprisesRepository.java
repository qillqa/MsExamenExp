package com.codigo.msexamenexp.repository;

import com.codigo.msexamenexp.entity.EnterprisesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnterprisesRepository extends JpaRepository<EnterprisesEntity,Integer> {

    boolean existsByNumDocument(String integer);
    EnterprisesEntity findByNumDocument(String doc);
}
