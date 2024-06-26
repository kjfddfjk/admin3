package tech.wetech.admin3.sys.model;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 资源
 *
 * @author cjbi
 */
@Entity
@Table(name = "TB_RESOURCE")
public class Resource extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Resource_GEN")
  @SequenceGenerator(name = "Resource_GEN", sequenceName = "SEQ_RESOURCE", allocationSize = 1)
  @Column(name = "ID")
  private Long id;

  private String name;

  private Type type;

  private String permission;

  @ManyToOne(fetch = FetchType.LAZY)
  private Resource parent;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
  private Set<Resource> children = new LinkedHashSet<>();

  private String parentIds; //父编号列表

  private String icon;
  private String url;

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
  @JoinTable(name = "TB_ROLE_RESOURCE",
    joinColumns = @JoinColumn(name = "RESOURCE_ID", referencedColumnName = "ID"),
    inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID"))
  private Set<Role> roles = new LinkedHashSet<>();

  public enum Type {
    MENU, BUTTON
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public String getIcon() {
    return icon;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPermission() {
    return permission;
  }

  public void setPermission(String permission) {
    this.permission = permission;
  }

  public Resource getParent() {
    return parent;
  }

  public void setParent(Resource parent) {
    this.parent = parent;
  }

  public Set<Resource> getChildren() {
    return children;
  }

  public String getParentIds() {
    return parentIds;
  }

  public void setParentIds(String parentIds) {
    this.parentIds = parentIds;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setChildren(Set<Resource> children) {
    this.children = children;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }
}
