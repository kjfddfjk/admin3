package tech.wetech.admin3.sys.model;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

import static jakarta.persistence.CascadeType.DETACH;

/**
 * 角色
 *
 * @author cjbi
 */
@Entity
@Table(name = "TB_ROLE")
public class Role extends BaseEntity {

  @Column(unique = true, nullable = false)
  private String name;

  private String description;

  private Boolean available = Boolean.FALSE;

  @ManyToMany(fetch = FetchType.LAZY, cascade = DETACH)
  @JoinTable(name = "TB_ROLE_RESOURCE",
    joinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID"),
    inverseJoinColumns = @JoinColumn(name = "RESOURCE_ID", referencedColumnName = "ID"))
  private Set<Resource> resources = new LinkedHashSet<>();

  @ManyToMany(fetch = FetchType.LAZY, cascade = DETACH)
  @JoinTable(name = "TB_USER_ROLE",
    joinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID"),
    inverseJoinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"))
  private Set<User> users = new LinkedHashSet<>();

  public Role() {

  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public Boolean getAvailable() {
    return available;
  }


  public void setName(String name) {
    this.name = name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setAvailable(Boolean available) {
    this.available = available;
  }

  public Set<Resource> getResources() {
    return resources;
  }

  public void setResources(Set<Resource> resources) {
    this.resources = resources;
  }

  public Set<User> getUsers() {
    return users;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
  }
}
