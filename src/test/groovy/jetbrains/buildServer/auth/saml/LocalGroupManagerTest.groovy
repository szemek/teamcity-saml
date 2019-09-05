package jetbrains.buildServer.auth.saml

import jetbrains.buildServer.groups.UserGroupManager
import jetbrains.buildServer.groups.SUserGroup
import jetbrains.buildServer.users.User
import spock.lang.Specification

class LocalGroupManagerTest extends Specification {

    User user = Mock()
    UserGroupManager userGroupManager = Mock()
    SUserGroup sUserGroup = Mock()
    LocalGroupManager localGroupManager
    String userGroupKey = "MY_AWESOME_GROUP"


    def setup() {
        localGroupManager = new LocalGroupManager(userGroupManager)
    }

    def "Add user if group exists"() {
        given:
        when:
        localGroupManager.addUserByGroupKey(user, userGroupKey)
        then:
        1 * userGroupManager.findUserGroupByKey(userGroupKey) >> sUserGroup
        1 * sUserGroup.addUser(user)
    }

    def "Does not add user if group does not exist"() {
        given:
        when:
        localGroupManager.addUserByGroupKey(user, userGroupKey)
        then:
        1 * userGroupManager.findUserGroupByKey(userGroupKey) >> null
        0 * sUserGroup.addUser(user)
    }
}
