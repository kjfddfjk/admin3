-- 设置自增ID从1000开始
alter sequence TB_ORGANIZATION_SEQ increment by 999; select TB_ORGANIZATION_SEQ.nextval from dual; alter sequence TB_ORGANIZATION_SEQ increment by 50;
alter sequence TB_RESOURCE_SEQ increment by 999; select TB_RESOURCE_SEQ.nextval from dual; alter sequence TB_RESOURCE_SEQ increment by 50;
alter sequence TB_ROLE_SEQ increment by 999; select TB_ROLE_SEQ.nextval from dual; alter sequence TB_ROLE_SEQ increment by 50;
alter sequence TB_SESSION_SEQ increment by 999; select TB_SESSION_SEQ.nextval from dual; alter sequence TB_SESSION_SEQ increment by 50;
alter sequence TB_STORAGE_CONFIG_SEQ increment by 999; select TB_STORAGE_CONFIG_SEQ.nextval from dual; alter sequence TB_STORAGE_CONFIG_SEQ increment by 50;
alter sequence TB_STORAGE_FILE_SEQ increment by 999; select TB_STORAGE_FILE_SEQ.nextval from dual; alter sequence TB_STORAGE_FILE_SEQ increment by 50;
alter sequence TB_STORED_EVENT_SEQ increment by 999; select TB_STORED_EVENT_SEQ.nextval from dual; alter sequence TB_STORED_EVENT_SEQ increment by 50;
alter sequence TB_USER_CREDENTIAL_SEQ increment by 999; select TB_USER_CREDENTIAL_SEQ.nextval from dual; alter sequence TB_USER_CREDENTIAL_SEQ increment by 50;
alter sequence TB_USER_SEQ increment by 999; select TB_USER_SEQ.nextval from dual; alter sequence TB_USER_SEQ increment by 50;
