package com.codigo.msexamenexp.service;

import com.codigo.msexamenexp.aggregates.request.RequestEnterprises;
import com.codigo.msexamenexp.aggregates.response.ResponseBase;


public interface EnterprisesService {
    ResponseBase getInfoSunat(String numero);
    ResponseBase createEnterprise(RequestEnterprises requestEnterprises);
    ResponseBase findOneEnterprise(String doc);
    ResponseBase  findAllEnterprises();
    ResponseBase updateEnterprise(Integer id, RequestEnterprises requestEnterprises);
    ResponseBase delete(Integer id);
}
