package com.codigo.msexamenexp.service.impl;

import com.codigo.msexamenexp.aggregates.request.RequestEnterprises;
import com.codigo.msexamenexp.aggregates.response.ResponseBase;
import com.codigo.msexamenexp.aggregates.constants.Constants;
import com.codigo.msexamenexp.aggregates.response.ResponseSunat;
import com.codigo.msexamenexp.config.RedisService;
import com.codigo.msexamenexp.entity.DocumentsTypeEntity;
import com.codigo.msexamenexp.entity.EnterprisesEntity;
import com.codigo.msexamenexp.entity.EnterprisesTypeEntity;
import com.codigo.msexamenexp.feignClient.SunatClient;
import com.codigo.msexamenexp.repository.DocumentsTypeRepository;
import com.codigo.msexamenexp.repository.EnterprisesRepository;
import com.codigo.msexamenexp.service.EnterprisesService;
import com.codigo.msexamenexp.util.EnterprisesValidations;
import com.codigo.msexamenexp.util.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class EnterprisesServiceImpl implements EnterprisesService {
    private final SunatClient sunatClient;

    private final EnterprisesRepository enterprisesRepository;
    private final EnterprisesValidations enterprisesValidations;
    private final DocumentsTypeRepository typeRepository;
    private final RedisService redisService;
    private final Util util;

    public EnterprisesServiceImpl(SunatClient sunatClient, EnterprisesRepository enterprisesRepository, EnterprisesValidations enterprisesValidations, DocumentsTypeRepository typeRepository, RedisService redisService, Util util) {
        this.sunatClient = sunatClient;
        this.enterprisesRepository = enterprisesRepository;
        this.enterprisesValidations = enterprisesValidations;
        this.typeRepository = typeRepository;
        this.redisService = redisService;
        this.util = util;
    }

    @Value("${token.api.sunat}")
    private String tokenSunat;

    @Value("${time.expiration.sunat.info}")
    private String timeExpirationSunatInfo;

    @Override
    public ResponseBase getInfoSunat(String numero) {
        ResponseSunat sunat = getExecutionSunat(numero);
        if(sunat != null){
            return new ResponseBase(Constants.CODE_SUCCESS,Constants.MESS_SUCCESS, Optional.of(sunat));
        }else{
            return new ResponseBase(Constants.CODE_ERROR,Constants.MESS_NON_DATA_SUNAT, Optional.empty());
        }
    }

    @Override
    public ResponseBase createEnterprise(RequestEnterprises requestEnterprises) {
        boolean validate = enterprisesValidations.validateInput(requestEnterprises);
        if(validate){
            EnterprisesEntity enterprises = getEntity(requestEnterprises);
            if(enterprises != null){
                enterprisesRepository.save(enterprises);
                return new ResponseBase(Constants.CODE_SUCCESS,Constants.MESS_SUCCESS,Optional.of(enterprises));
            }else {
                return new ResponseBase(Constants.CODE_ERROR,Constants.MESS_NON_DATA_SUNAT,Optional.empty());
            }
            // enterprisesRepository.save(enterprises);
            // return new ResponseBase(Constants.CODE_SUCCESS,Constants.MESS_SUCCESS, Optional.of(enterprises));
        }else{
            return new ResponseBase(Constants.CODE_ERROR_DATA_INPUT,Constants.MESS_ERROR_DATA_NOT_VALID,null);
        }
    }

    @Override
    public ResponseBase findOneEnterprise(String doc) {
        // EnterprisesEntity enterprisesEntity = enterprisesRepository.findByNumDocument(doc);
        // return new ResponseBase(Constants.CODE_SUCCESS,Constants.MESS_SUCCESS, Optional.of(enterprisesEntity));

        String redisCache = redisService.getValueByKey(Constants.REDIS_KEY_INFO_SUNAT+doc);
        Optional<EnterprisesEntity> optional = Optional.ofNullable(enterprisesRepository.findByNumDocument(doc));

        if(redisCache != null){
            Optional<ResponseSunat> sunat = Optional.ofNullable(util.convertFromJson(redisCache, ResponseSunat.class));
            //return sunat;
            return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESS_SUCCESS, sunat);
        }else{
            if(optional.isPresent()){
                return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESS_SUCCESS, optional);
            }else {
                return new ResponseBase(Constants.CODE_ERROR, Constants.MESS_ZERO_ROWS,Optional.empty());
            }
        }
    }

    @Override
    public ResponseBase findAllEnterprises() {
        Optional allEnterprises = Optional.of(enterprisesRepository.findAll());
        if(allEnterprises.isPresent()){
            return new ResponseBase(Constants.CODE_SUCCESS,Constants.MESS_SUCCESS,allEnterprises);
        }
        return new ResponseBase(Constants.CODE_ERROR_DATA_NOT,Constants.MESS_ZERO_ROWS,Optional.empty());
    }

    @Override
    public ResponseBase updateEnterprise(Integer id, RequestEnterprises requestEnterprises) {
        boolean existEnterprise = enterprisesRepository.existsById(id);
        if(existEnterprise){
            Optional<EnterprisesEntity> enterprises = enterprisesRepository.findById(id);
            boolean validationEntity = enterprisesValidations.validateInputUpdate(requestEnterprises);
            if(validationEntity){
                EnterprisesEntity enterprisesUpdate = getEntityUpdate(requestEnterprises, enterprises.get());
                enterprisesRepository.save(enterprisesUpdate);
                return new ResponseBase(Constants.CODE_SUCCESS,Constants.MESS_SUCCESS,Optional.of(enterprisesUpdate));
            }else {
                return new ResponseBase(Constants.CODE_ERROR_DATA_INPUT,Constants.MESS_ERROR_DATA_NOT_VALID,Optional.empty());
            }
        }else {
            return new ResponseBase(Constants.CODE_ERROR,Constants.MESS_ERROR_NOT_UPDATE,Optional.empty());
        }
    }

    @Override
    public ResponseBase delete(Integer id) {
        boolean existsEnterprise = enterprisesRepository.existsById(id);
        if(existsEnterprise){
            Optional<EnterprisesEntity> enterprises = enterprisesRepository.findById(id);
            EnterprisesEntity enterprisesUpdate = getEntityDelete(enterprises.get());
            enterprisesRepository.save(enterprisesUpdate);
            return new ResponseBase(Constants.CODE_SUCCESS,Constants.MESS_SUCCESS,Optional.of(enterprisesUpdate));
        }else{
            return new ResponseBase(Constants.CODE_ERROR,Constants.MESS_ERROR_NOT_UPDATE,Optional.empty());
        }
    }

    private EnterprisesEntity getEntity(RequestEnterprises requestEnterprises){
        EnterprisesEntity entity = new EnterprisesEntity();
        // agregado
        ResponseSunat sunat = getExecutionSunat(requestEnterprises.getNumDocument());
        if(sunat != null){
            entity.setBusinessName(sunat.getRazonSocial());
            entity.setTradeName(enterprisesValidations.isNullOrEmpty(requestEnterprises.getTradeName()) ? requestEnterprises.getBusinessName() : requestEnterprises.getTradeName());

            entity.setNumDocument(requestEnterprises.getNumDocument());
            // entity.setBusinessName(requestEnterprises.getBusinessName());
            // entity.setTradeName(enterprisesValidations.isNullOrEmpty(requestEnterprises.getTradeName()) ? requestEnterprises.getBusinessName() : requestEnterprises.getTradeName());

            entity.setStatus(Constants.STATUS_ACTIVE);
            entity.setEnterprisesTypeEntity(getEnterprisesType(requestEnterprises));
            entity.setDocumentsTypeEntity(getDocumentsType(requestEnterprises));
            entity.setUserCreate(Constants.AUDIT_ADMIN);
            entity.setDateCreate(getTimestamp());
        }else{
            return null;
        }
        return entity;
    }
    private EnterprisesEntity getEntityUpdate(RequestEnterprises requestEnterprises, EnterprisesEntity enterprisesEntity){
        enterprisesEntity.setNumDocument(requestEnterprises.getNumDocument());
        enterprisesEntity.setBusinessName(requestEnterprises.getBusinessName());
        // enterprisesEntity.setEnterprisesTypeEntity(getEnterprisesType(requestEnterprises));
        // agregado
        enterprisesEntity.setTradeName(enterprisesValidations.isNullOrEmpty(requestEnterprises.getTradeName()) ? requestEnterprises.getBusinessName() : requestEnterprises.getTradeName());

        enterprisesEntity.setUserModif(Constants.AUDIT_ADMIN);
        enterprisesEntity.setDateModif(getTimestamp());
        return enterprisesEntity;
    }
    private EnterprisesEntity getEntityDelete(EnterprisesEntity enterprisesEntity){
        enterprisesEntity.setUserDelete(Constants.AUDIT_ADMIN);
        enterprisesEntity.setDateDelete(getTimestamp());
        enterprisesEntity.setStatus(0);
        return enterprisesEntity;
    }

    private EnterprisesTypeEntity getEnterprisesType(RequestEnterprises requestEnterprises){
        EnterprisesTypeEntity typeEntity = new EnterprisesTypeEntity();

        typeEntity.setIdEnterprisesType(requestEnterprises.getEnterprisesTypeEntity());
        return typeEntity;
    }

    private DocumentsTypeEntity getDocumentsType(RequestEnterprises requestEnterprises){
        DocumentsTypeEntity typeEntity = typeRepository.findByCodType(Constants.COD_TYPE_RUC);
        return  typeEntity;
    }

    private Timestamp getTimestamp(){
        long currentTime = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(currentTime);
        return timestamp;
    }

    public ResponseSunat getExecutionSunat(String numero){
        String redisCache = redisService.getValueByKey(Constants.REDIS_KEY_INFO_SUNAT+numero);
        if(redisCache != null){
            ResponseSunat sunat = util.convertFromJson(redisCache, ResponseSunat.class);
            return sunat;
        }else{
            String authorization = "Bearer "+tokenSunat;
            ResponseSunat sunat = sunatClient.getInfoSunat(numero, authorization);
            String redisData = util.convertToJson(sunat);
            redisService.saveKeyValue(Constants.REDIS_KEY_INFO_SUNAT+numero, redisData, Integer.valueOf(timeExpirationSunatInfo));
            return sunat;
        }
    }
}
