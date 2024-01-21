package com.codigo.msexamenexp.aggregates.constants;

public class Constants {
    //CODE
    public static final Integer CODE_SUCCESS=1001;
    public static final Integer CODE_ERROR=400;

    public static final Integer CODE_ERROR_DATA_INPUT=1002;
    public static final Integer CODE_ERROR_EXIST=1003;
    public static final Integer CODE_ERROR_DATA_NOT=1004;
    public static final String MESS_NON_DATA="No existe Datos para ID";
    public static final Integer CODE_ERROR_GENERIC=1005;

    //MESSAGES
    public static final String MESS_SUCCESS="Ejecución correcta";
    public static final String MESS_ERROR="Error en la Ejecución";

    public static final String MESS_ERROR_DATA_NOT_VALID="Error: Durante las validaciones de los Datos";
    public static final String MESS_ZERO_ROWS="No existe Registros en BD";
    public static final String MESS_ERROR_NOT_UPDATE ="Error: No se ejecuto la actualización, Empresa no Existe";

    public static final String MESS_ERROR_NOT_UPDATE_PERSON ="Error: No se ejecuto la actualización, Persona no Existe";

    public static final String MESS_NON_DATA_SUNAT="No existe Registros en el API de SUNAT";
    //data
    public static final Integer LENGTH_RUC=11;
    public static final Integer LENGTH_DNI=8;

    //Status
    public static final Integer STATUS_ACTIVE=1;
    public static final Integer STATUS_INACTIVE=0;

    //AUDIT
    public static final String AUDIT_ADMIN="ADMIN";

    //REDISKEY
    public static final String REDIS_KEY_INFO_SUNAT="MS:EXAMEN:SUNAT:";
    public static final String REDIS_KEY_INFO_SUNAT_CLIENT="API:SUNAT:";
    public static final String COD_TYPE_RUC = "06";
}
