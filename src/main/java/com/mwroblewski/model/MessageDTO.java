package com.mwroblewski.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MESSAGES")
public class MessageDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(columnDefinition = "VARCHAR(3000)", nullable = false)
    private String text;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Europe/Warsaw")
    @Temporal(value = TemporalType.DATE)
    private Date created;

    // relation with other table
    @ManyToOne
    @JoinColumn(name = "to_user_id")
    private UserDTO toUserDTO;
    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private UserDTO fromUserDTO;

    // constructors
    public MessageDTO() {
    }

    // getter/setter methods
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public Date getCreated() {
        return created;
    }
    public void setCreated(Date created) {
        this.created = created;
    }
    public UserDTO getToUserDTO() {
        return toUserDTO;
    }
    public void setToUserDTO(UserDTO toUserDTO) {
        this.toUserDTO = toUserDTO;
    }
    public UserDTO getFromUserDTO() {
        return fromUserDTO;
    }
    public void setFromUserDTO(UserDTO fromUserDTO) {
        this.fromUserDTO = fromUserDTO;
    }
}
