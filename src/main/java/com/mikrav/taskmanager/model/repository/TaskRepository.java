package com.mikrav.taskmanager.model.repository;

import com.mikrav.taskmanager.model.entity.Task;
import com.mikrav.taskmanager.model.entity.TaskStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {

    @Query("SELECT t FROM Task t" +
            " WHERE (:status IS NULL OR t.status = :status)" +
            " AND (:assigneeId IS NULL OR t.assignee.id = :assigneeId)" +
            " AND (:authorId IS NULL OR t.author.id = :authorId)")
    List<Task> findAllByOptionalParams(
            @Param("status") TaskStatus status,
            @Param("assigneeId") Long assigneeId,
            @Param("authorId") Long authorId
    );
}
