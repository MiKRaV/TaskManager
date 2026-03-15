package com.mikrav.taskmanager.model.dto;

import com.mikrav.taskmanager.model.entity.TaskPriority;
import com.mikrav.taskmanager.model.entity.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Task update request")
public class UpdateTaskRequest {

    @Schema(description = "Title", example = "Create a web")
    private String title;

    @Schema(description = "Description", example = "Need more web cartridges")
    private String description;

    @Schema(description = "Status", example = "TODO")
    private TaskStatus status;

    @Schema(description = "Priority", example = "MEDIUM")
    private TaskPriority priority;

    @Schema(description = "Id of assignee", example = "3")
    private Long assigneeId;
}
