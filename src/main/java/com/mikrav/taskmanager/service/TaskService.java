package com.mikrav.taskmanager.service;

import com.mikrav.taskmanager.model.dto.CreateTaskRequest;
import com.mikrav.taskmanager.model.dto.UpdateTaskRequest;
import com.mikrav.taskmanager.model.entity.Task;
import com.mikrav.taskmanager.model.entity.TaskPriority;
import com.mikrav.taskmanager.model.entity.TaskStatus;
import com.mikrav.taskmanager.model.entity.User;
import com.mikrav.taskmanager.model.entity.UserRole;
import com.mikrav.taskmanager.model.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task getTask(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public List<Task> getAllTasks(
            TaskStatus status,
            Long assigneeId,
            Long authorId
    ) {
        return taskRepository.findAllByOptionalParams(status, assigneeId, authorId);
    }

    public Task createTask(CreateTaskRequest taskData, User author) {
        Task task = new Task();
        task.setTitle(taskData.getTitle());
        task.setDescription(taskData.getDescription());
        task.setStatus(Optional.ofNullable(taskData.getStatus()).orElse(TaskStatus.TODO));
        task.setPriority(Optional.ofNullable(taskData.getPriority()).orElse(TaskPriority.LOW));
        task.setAuthor(author);

        if (taskData.getAssigneeId() != null) {
            Optional<User> assignee = userService.getById(taskData.getAssigneeId());
            task.setAssignee(assignee.orElse(null));
        }

        return taskRepository.save(task);
    }

    public Task updateTask(Long id, UpdateTaskRequest taskData, User user) throws AccessDeniedException {
        Task task = taskRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        if (!task.getAuthor().getId().equals(user.getId()) && user.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Only author or admin can edit task");
        }

        if (taskData != null) {
            task.setTitle(taskData.getTitle());
        }

        if (taskData.getDescription() != null) {
            task.setDescription(taskData.getDescription());
        }

        if (taskData.getStatus() != null) {
            task.setStatus(taskData.getStatus());
        }

        if (taskData.getPriority() != null) {
            task.setPriority(taskData.getPriority());
        }

        if (taskData.getAssigneeId() != null) {
            Optional<User> assignee = userService.getById(taskData.getAssigneeId());
            task.setAssignee(assignee.orElseThrow(() -> new EntityNotFoundException("Assignee Not Found")));
        }

        return taskRepository.save(task);
    }

    public void deleteTask(Long id, User user) throws AccessDeniedException {
        Task task = taskRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        if (!task.getAuthor().getId().equals(user.getId()) && user.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Only author or admin can delete task");
        }

        taskRepository.deleteById(id);
    }
}
