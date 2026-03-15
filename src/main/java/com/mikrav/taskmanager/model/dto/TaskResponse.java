package com.mikrav.taskmanager.model.dto;

import com.mikrav.taskmanager.model.entity.Task;
import com.mikrav.taskmanager.model.entity.TaskPriority;
import com.mikrav.taskmanager.model.entity.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Schema(description = "Task Create/Update response")
public class TaskResponse {

    @Schema(description = "Title", example = "Create a web")
    @NotBlank(message = "Title is required")
    private String title;

    @Schema(description = "Description", example = "Need more web cartridges")
    private String description;

    @Schema(description = "Status", example = "TODO")
    private TaskStatus status;

    @Schema(description = "Priority", example = "MEDIUM")
    private TaskPriority priority;

    @Schema(description = "Id of author", example = "2")
    private Long authorId;

    @Schema(description = "Id of assignee", example = "3")
    private Long assigneeId;

    @Schema(description = "Creation date", example = "2026-03-15T17:35:44")
    private LocalDateTime createdAt;

    @Schema(description = "Update date", example = "2026-03-15T17:35:44")
    private LocalDateTime updatedAt;

    public static TaskResponse from(Task task) {
        TaskResponse response = new TaskResponse();
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setPriority(task.getPriority());
        response.setAuthorId(task.getAuthor().getId());
        if (task.getAssignee() != null) {
            response.setAssigneeId(task.getAssignee().getId());
        }
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());

        return response;
    }
}
