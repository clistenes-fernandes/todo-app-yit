package com.example.todo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_task")
public class Task extends TaskBase implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    @JsonIgnore
    @ManyToOne(targetEntity=TaskList.class, fetch = FetchType.LAZY)
    private TaskList taskList;


    @OneToMany(targetEntity=SubTask.class,cascade =CascadeType.MERGE , fetch = FetchType.LAZY, mappedBy = "task")
    private Set<SubTask> subTasks = new HashSet<>();

    public Task(Long id, String title, String description, Instant dueDate, Boolean completed, UserModel user){
        super(title, description, dueDate, completed);
        this.id = id;
        this.user = user;
    }
}
