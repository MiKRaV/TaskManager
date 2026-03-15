package com.mikrav.taskmanager.controller;

import com.mikrav.taskmanager.model.dto.CreateTaskRequest;
import com.mikrav.taskmanager.model.dto.TaskResponse;
import com.mikrav.taskmanager.model.dto.UpdateTaskRequest;
import com.mikrav.taskmanager.model.entity.Task;
import com.mikrav.taskmanager.model.entity.TaskStatus;
import com.mikrav.taskmanager.model.entity.User;
import com.mikrav.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@SecurityRequirement(name = "Authorization")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping()
    public List<TaskResponse> getAllTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(required = false) Long authorId
    ) {
        List<Task> tasks = taskService.getAllTasks(status, assigneeId, authorId);
        return tasks.stream()
                .map(TaskResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public TaskResponse getTask(@PathVariable Long id) {
        Task task = taskService.getTask(id);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        return TaskResponse.from(task);
    }

    @PostMapping()
    public TaskResponse createTask(@Valid @RequestBody CreateTaskRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User author = (User) auth.getPrincipal();
        Task task = taskService.createTask(request, author);

        return TaskResponse.from(task);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Long id, @RequestBody UpdateTaskRequest request) throws AccessDeniedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        Task task = taskService.updateTask(id, request, user);

        return TaskResponse.from(task);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) throws AccessDeniedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        taskService.deleteTask(id, user);
    }
}
