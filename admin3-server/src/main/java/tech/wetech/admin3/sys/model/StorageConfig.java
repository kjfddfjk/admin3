package tech.wetech.admin3.sys.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import tech.wetech.admin3.common.Constants;
import tech.wetech.admin3.common.SessionItemHolder;
import tech.wetech.admin3.common.StringUtils;
import tech.wetech.admin3.infra.storage.ExtraConfig;
import tech.wetech.admin3.infra.storage.OSSExtraConfig;
import tech.wetech.admin3.infra.storage.S3ExtraConfig;
import tech.wetech.admin3.sys.service.dto.UserinfoDTO;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cjbi
 */
@Entity
@Table(name = "TB_STORAGE_CONFIG")
public class StorageConfig extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "StorageConfig_GEN")
  @SequenceGenerator(name = "StorageConfig_GEN", sequenceName = "SEQ_STORAGE_CONFIG", allocationSize = 1)
  @Column(name = "ID")
  private Long id;

  private String storageId;

  private String name;

  private Type type;

  private Boolean defaultFlag;

  private String accessKey;

  private String secretKey;

  private String endpoint;

  private String bucketName;

  private String address;

  private String region;

  private String storagePath;

  private String createUser;

  private LocalDateTime createTime;

  private String extraConfigJson;

  @PrePersist
  protected void onCreate() {
    createTime = LocalDateTime.now();
    UserinfoDTO userInfo = (UserinfoDTO) SessionItemHolder.getItem(Constants.SESSION_CURRENT_USER);
    createUser = userInfo.username();
  }

    public enum Type {
    LOCAL, S3, OSS, OBS
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public String getStorageId() {
    return storageId;
  }

  public void setStorageId(String storageId) {
    this.storageId = storageId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getDefaultFlag() {
    return defaultFlag;
  }

  public void setDefaultFlag(Boolean defaultFlag) {
    this.defaultFlag = defaultFlag;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public String getAccessKey() {
    return accessKey;
  }

  public void setAccessKey(String accessKey) {
    this.accessKey = accessKey;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public String getBucketName() {
    return bucketName;
  }

  public void setBucketName(String bucketName) {
    this.bucketName = bucketName;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStoragePath() {
    return storagePath;
  }

  public void setStoragePath(String storagePath) {
    this.storagePath = storagePath;
  }


  public String getCreateUser() {
    return createUser;
  }

  public void setCreateUser(String createUser) {
    this.createUser = createUser;
  }

  public LocalDateTime getCreateTime() {
    return createTime;
  }

  public void setCreateTime(LocalDateTime createTime) {
    this.createTime = createTime;
  }


  public String getExtraConfigJson() {
    return extraConfigJson;
  }

  public void setExtraConfigJson(String extraConfigJson) {
        this.extraConfigJson = extraConfigJson;
    }
  public String getAccessKeyWithEnv() {
    return renderTemplate(accessKey);
  }

  public String getSecretKeyWithEnv() {
    return renderTemplate(secretKey);
  }

  public String getEndpointWithEnv() {
    return renderTemplate(endpoint);
  }

  public String getBucketNameWithEnv() {
    return renderTemplate(bucketName);
  }

  public String getAddressWithEnv() {
    return renderTemplate(address);
  }

  public String getStoragePathWithEnv() {
    return renderTemplate(storagePath);
  }

    public ExtraConfig getExtraConfig() {
        assert type != null;
        if(StringUtils.isBlank(extraConfigJson)) {
            return null;
        }
        switch (type) {
            case S3:
                return parseJsonToClass(extraConfigJson, S3ExtraConfig.class);
            case OSS:
                return parseJsonToClass(extraConfigJson, OSSExtraConfig.class);
            default:
                throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }
    private <T extends ExtraConfig> T parseJsonToClass(String json, Class<T> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON to " + clazz.getSimpleName(), e);
        }
    }

  private String renderTemplate(String template) {
    Map<Object, Object> attributes = new HashMap<>();
    attributes.putAll(System.getenv());
    attributes.putAll(System.getProperties());
    return StringUtils.simpleRenderTemplate(template, attributes);
  }

}
