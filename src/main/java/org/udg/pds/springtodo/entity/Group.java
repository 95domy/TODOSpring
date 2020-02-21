package org.udg.pds.springtodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity(name="usergroup")
public class Group implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_user")
    private User owner;

    @Column(name = "fk_user", insertable = false, updatable = false)
    private Long userId;

    @ManyToMany(mappedBy="member_groups")
    private Collection<User> users = new ArrayList<>();

    public Group() {

    }

    public Group(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @JsonView(Views.Private.class)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @JsonView(Views.Private.class)
    public String getName() {
        return name;
    }

    @JsonView(Views.Private.class)
    public String getDescription() {
        return description;
    }

    @JsonView(Views.Complete.class)
    public long getUserId() {
        return userId;
    }

    public void addMember(User user) {
        users.add(user);
    }

    @JsonView(Views.Complete.class)
    public Collection<User> getMembers() {
        users.size();
        return users;
    }
}
