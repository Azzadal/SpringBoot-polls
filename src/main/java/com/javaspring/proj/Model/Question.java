package com.javaspring.proj.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.Id;

import javax.persistence.*;
import java.util.Optional;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "poll_id")
//    @JsonManagedReference
    private Poll poll;
    private String content;
    private String displayOrder;

    public Question() {
    }

    public Question(Poll poll, String content, String displayOrder) {
        this.poll = poll;
        this.content = content;
        this.displayOrder = displayOrder;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }
}