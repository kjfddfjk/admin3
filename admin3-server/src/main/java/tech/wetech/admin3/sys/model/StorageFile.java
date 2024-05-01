package tech.wetech.admin3.sys.model;

import jakarta.persistence.*;
import tech.wetech.admin3.common.Constants;
import tech.wetech.admin3.common.SessionItemHolder;
import tech.wetech.admin3.sys.service.dto.UserinfoDTO;

import java.time.LocalDateTime;

/**
 * @author cjbi
 */
@Entity
@Table(name = "TB_STORAGE_FILE", uniqueConstraints = {@UniqueConstraint(columnNames = {"\"KEY\""})})
public class StorageFile extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "StorageFile_GEN")
  @SequenceGenerator(name = "StorageFile_GEN", sequenceName = "SEQ_STORAGE_FILE", allocationSize = 1)
  @Column(name = "ID")
  private Long id;

  /**
   * 文件的唯一索引
   */
  @Column(name = "\"KEY\"")
  private String key;

  /**
   * 文件名
   */
  private String name;

  /**
   * 文件类型
   */
  @Column(name = "\"TYPE\"")
  private String type;

  /**
   * 文件大小
   */
  @Column(name = "\"SIZE\"")
  private Long size;

  private String createUser;

  private LocalDateTime createTime;

  private String storageId;

  @PrePersist
  protected void onCreate() {
    createTime = LocalDateTime.now();
    UserinfoDTO userInfo = (UserinfoDTO) SessionItemHolder.getItem(Constants.SESSION_CURRENT_USER);
    createUser = userInfo.username();
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Long getSize() {
    return size;
  }

  public void setSize(Long size) {
    this.size = size;
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

  public String getStorageId() {
    return storageId;
  }

  public void setStorageId(String storageId) {
    this.storageId = storageId;
  }
}
