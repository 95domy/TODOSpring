package org.udg.pds.springtodo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.udg.pds.springtodo.entity.*;
import org.udg.pds.springtodo.service.GroupService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RequestMapping(path="/groups")
@RestController
public class GroupController extends BaseController {

    @Autowired
    GroupService groupService;

    @PostMapping(consumes = "application/json")
    public IdObject addGroup(HttpSession session, @Valid @RequestBody GroupController.R_Group group) {

        Long userId = getLoggedUser(session);

        return groupService.addGroup(group.name, group.description, userId);
    }

    @PostMapping(path = "/{gid}/members/{uid}")
    public void addUserToGroup(HttpSession session, @PathVariable("gid") Long groupId, @PathVariable("uid") Long userId) {
        Long selfUserId = getLoggedUser(session);
        groupService.addUserToGroup(groupId, userId, selfUserId);
    }

    @GetMapping(path="/{gid}/members")
    @JsonView(Views.Private.class)
    public Collection<User> getGroupMemebers(HttpSession session, @PathVariable("gid") Long groupId) {
        Long userId = getLoggedUser(session);
        return groupService.getGroupMembers(groupId);
    }

    @GetMapping(path="/self")
    @JsonView(Views.Private.class)
    public Collection<Group> listAllGroups(HttpSession session) {
        Long userId = getLoggedUser(session);

        return groupService.getGroups(userId);
    }

    static class R_Group {

        @NotNull
        public String name;

        @NotNull
        public String description;

    }
}
