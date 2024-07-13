package tech.wetech.admin3.sys.model;

import jakarta.persistence.*;
import tech.wetech.admin3.common.BusinessException;
import tech.wetech.admin3.common.CommonResultStatus;
import tech.wetech.admin3.common.SecurityUtil;

import java.security.NoSuchAlgorithmException;

/**
 * 用户凭证
 *
 * @author cjbi
 */
@Entity
@Table(name = "TB_USER_CREDENTIAL")
public class UserCredential extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserCredential_GEN")
  @SequenceGenerator(name = "UserCredential_GEN", sequenceName = "SEQ_USER_CREDENTIAL", allocationSize = 1)
  @Column(name = "ID")
  private Long id;

  /**
   * 标识（手机号 邮箱 用户名或第三方应用的唯一标识）
   */
  @Column(nullable = false)
  private String identifier;
  /**
   * 密码凭证（站内的保存密码，站外的不保存或保存token）
   */
  @Column(nullable = false)
  private String credential;
  /**
   * 登录类型（手机号 邮箱 用户名）或第三方应用名称（微信 微博等）
   */
  private IdentityType identityType;

  @ManyToOne
  private User user;


  public UserCredential() {
  }

  public boolean doCredentialMatch(String credential) {
      //TODO 未实现其他登录方式
      if (this.getIdentityType() != IdentityType.PASSWORD || !digestCredential(credential).equals(this.getCredential())) {
        return false;
      }
      return true;
  }

  public enum IdentityType {
    PASSWORD
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public String getIdentifier() {
    return identifier;
  }

  public String getCredential() {
    return credential;
  }

  public IdentityType getIdentityType() {
    return identityType;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public void setCredential(String credential) {
    this.credential = credential;
  }

  public void setIdentityType(IdentityType identityType) {
    this.identityType = identityType;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public User getUser() {
    return user;
  }

  public String digestCredential(String credential) {
      try {
          return SecurityUtil.md5(identifier, credential);
      } catch (NoSuchAlgorithmException e) {
          throw new RuntimeException(e);
      }
  }

}
