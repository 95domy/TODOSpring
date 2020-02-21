package org.udg.pds.springtodo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.udg.pds.springtodo.controller.exceptions.ServiceException;
import org.udg.pds.springtodo.entity.*;
import org.udg.pds.springtodo.repository.GroupRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Service
public class GroupService {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserService userService;

    public GroupRepository crud() {
        return groupRepository;
    }

    @Transactional
    public void addUserToGroup(Long groupId, Long userId, Long ownerId) {
        Group group = this.getGroup(groupId);
        if (group.getOwner().getId() != ownerId) {
            throw new ServiceException("You aren't the owner of this group");
        }

        User user = userService.getUser(userId);

        group.addMember(user);
        user.addMemberGroup(group);
    }

    @Transactional
    public IdObject addGroup(String name, String description, Long userId) {
        try {
            User user = userService.getUser(userId);

            Group group = new Group(name, description);

            group.setOwner(user);

            user.addGroup(group);

            groupRepository.save(group);
            return new IdObject(group.getId());
        } catch (Exception ex) {
            // Very important: if you want that an exception reaches the EJB caller, you have to throw an ServiceException
            // We catch the normal exception and then transform it in a ServiceException
            throw new ServiceException(ex.getMessage());
        }
    }

    public Collection<Group> getGroups(Long userId) {
        Optional<User> u = userService.crud().findById(userId);
        if (!u.isPresent()) throw new ServiceException("User does not exists");
        return u.get().getGroups();
    }

    public Collection<User> getGroupMembers(Long groupId) {
        Group group = this.getGroup(groupId);
        return group.getMembers();
    }

    public Group getGroup(Long id) {
        Optional<Group> group = groupRepository.findById(id);
        if (group.isPresent())
            return group.get();
        else
            throw new ServiceException(String.format("Group with id = % dos not exists", id));
    }

}
