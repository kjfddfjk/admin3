package tech.wetech.admin3.sys.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * @author cjbi
 */
@Entity
@Table(name = "TB_STORED_EVENT")
public class StoredEvent extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "StoredEvent_GEN")
  @SequenceGenerator(name = "StoredEvent_GEN", sequenceName = "SEQ_STORED_EVENT", allocationSize = 1)
  @Column(name = "ID")
  private Long id;

  @Lob
  @Column(length = Integer.MAX_VALUE)
  private String eventBody;
  private LocalDateTime occurredOn;
  private String typeName;
  @ManyToOne
  private User user;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public String getEventBody() {
    return eventBody;
  }

  public void setEventBody(String eventBody) {
    this.eventBody = eventBody;
  }

  public LocalDateTime getOccurredOn() {
    return occurredOn;
  }

  public void setOccurredOn(LocalDateTime occurredOn) {
    this.occurredOn = occurredOn;
  }

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

}
