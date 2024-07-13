package tech.wetech.admin3.sys.service;

import jakarta.annotation.Resource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.wetech.admin3.common.*;
import tech.wetech.admin3.sys.event.UserCreated;
import tech.wetech.admin3.sys.event.UserDeleted;
import tech.wetech.admin3.sys.event.UserUpdated;
import tech.wetech.admin3.sys.exception.UserException;
import tech.wetech.admin3.sys.model.Organization;
import tech.wetech.admin3.sys.model.User;
import tech.wetech.admin3.sys.model.UserCredential;
import tech.wetech.admin3.sys.repository.UserCredentialRepository;
import tech.wetech.admin3.sys.repository.UserRepository;
import tech.wetech.admin3.sys.service.dto.OrgUserDTO;
import tech.wetech.admin3.sys.service.dto.PageDTO;
import tech.wetech.admin3.sys.service.dto.UserinfoDTO;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static tech.wetech.admin3.common.CommonResultStatus.RECORD_NOT_EXIST;

/**
 * @author cjbi
 */
@Service
public class UserService {

  private final UserRepository userRepository;

  private final UserCredentialRepository userCredentialRepository;

  public UserService(UserRepository userRepository, UserCredentialRepository userCredentialRepository) {
    this.userRepository = userRepository;
    this.userCredentialRepository = userCredentialRepository;
  }

  @Transactional
  public User createUser(String username, String avatar, User.Gender gender, User.State state, Organization organization) {
    User user = new User();
    user.setUsername(username);
    user.setAvatar(avatar);
    user.setGender(gender);
    user.setState(state);
    user.setCreatedTime(LocalDateTime.now());
    user.setOrganization(organization);
    user = userRepository.save(user);
    DomainEventPublisher.instance().publish(new UserCreated(user));
    return user;
  }

  public Set<User> findUserByIds(Set<Long> userIds) {
    return userRepository.findByIds(userIds);
  }

  public User findUserById(Long userId) {
    return userRepository.findById(userId)
      .orElseThrow(() -> new BusinessException(RECORD_NOT_EXIST));
  }

  public PageDTO<OrgUserDTO> findOrgUsers(Pageable pageable, String username, User.State state, Organization organization) {
    Page<User> page = userRepository.findOrgUsers(pageable, username, state, organization, organization.makeSelfAsParentIds());
    return new PageDTO<>(page.getContent().stream().map(u ->
        new OrgUserDTO(u.getId(), u.getUsername(), u.getAvatar(), u.getGender(), u.getState(), u.getOrgFullName(), u.getCreatedTime()))
      .collect(Collectors.toList()), page.getTotalElements());
  }

  public boolean existsUsers(Organization organization) {
    String orgParentIds = organization.makeSelfAsParentIds();
    return userRepository.countOrgUsers(organization, orgParentIds) > 0;
  }


  @Transactional
  public User updateUser(Long userId, String avatar, User.Gender gender, User.State state, Organization organization) {
    User user = findUserById(userId);
    user.setAvatar(avatar);
    user.setGender(gender);
    user.setState(state);
    user.setOrganization(organization);
    user = userRepository.save(user);
    DomainEventPublisher.instance().publish(new UserUpdated(user));
    return user;
  }

  @Transactional
  public User disableUser(Long userId) {
    UserinfoDTO userInfo = (UserinfoDTO) SessionItemHolder.getItem(Constants.SESSION_CURRENT_USER);
    if (Objects.equals(userInfo.userId(), userId)) {
      throw new UserException(CommonResultStatus.PARAM_ERROR, "不能禁用自己");
    }
    User user = findUserById(userId);
    user.setState(User.State.LOCKED);
    return userRepository.save(user);
  }

  @Transactional
  public User enableUser(Long userId) {
    User user = findUserById(userId);
    user.setState(User.State.NORMAL);
    return userRepository.save(user);
  }

  public PageDTO<User> findUsers(Pageable pageable, User user) {
    Page<User> page = userRepository.findAll(Example.of(user), pageable);
    return new PageDTO<>(page.getContent(), page.getTotalElements());
  }

  @Transactional
  public void delete(Long userId) {
    User user = findUserById(userId);
    userRepository.delete(user);
    DomainEventPublisher.instance().publish(new UserDeleted(user));
  }
  @Transactional
  public UserCredential reset(Long userId, String identifier, UserCredential.IdentityType type, String credential) {
    Optional<UserCredential> oldCredential = userCredentialRepository.findCredential(identifier, type);
    assert !oldCredential.isPresent() || oldCredential.get().getUser().getId().equals(userId);
    // 随机用户密码
    credential = StringUtils.isBlank(credential) ? StringUtils.getRandomString(10) : credential;
    if(oldCredential.isPresent()) {
        UserCredential userCredential = oldCredential.get();
        userCredential.setCredential(userCredential.digestCredential(credential));
        assert (userCredentialRepository.updateCredential(userCredential.getId(), userCredential.getCredential()) == 1);
        return userCredential;
    } else {
      UserCredential newCredential = new UserCredential();
      newCredential.setIdentifier(identifier);
       newCredential.setCredential(newCredential.digestCredential(credential));
      newCredential.setIdentityType(type);
      newCredential.setUser(userRepository.getReferenceById(userId));
      return userCredentialRepository.save(newCredential);
    }
  }

}
