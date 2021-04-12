package jetbrains.buildServer.auth.saml;

import jetbrains.buildServer.groups.UserGroupManager;
import jetbrains.buildServer.groups.SUserGroup;
import jetbrains.buildServer.users.User;
import org.jetbrains.annotations.NotNull;
import org.apache.log4j.Logger;

class LocalGroupManager {
  private static final Logger LOG = Logger.getLogger(LocalGroupManager.class);
  private final UserGroupManager userGroupManager;

  LocalGroupManager(UserGroupManager userGroupManager) {
    this.userGroupManager = userGroupManager;
  }

  void addUserByGroupKey(@NotNull User user, String groupKey) {
    SUserGroup sUserGroup = this.userGroupManager.findUserGroupByKey(groupKey);
    if (sUserGroup == null) {
      LOG.info("Unable to add user " + user.getUsername() +
          " to group with key '" + groupKey + "' since no such group exists");
      return;
    }
    sUserGroup.addUser(user);
    LOG.info("Added user " + user.getUsername() + " to group '" + sUserGroup.getName() + "'");
  }
}
