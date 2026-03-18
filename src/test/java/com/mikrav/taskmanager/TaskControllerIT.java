package com.mikrav.taskmanager;

import com.mikrav.taskmanager.model.entity.Task;
import com.mikrav.taskmanager.model.entity.TaskPriority;
import com.mikrav.taskmanager.model.entity.TaskStatus;
import com.mikrav.taskmanager.model.entity.User;
import com.mikrav.taskmanager.model.entity.UserRole;
import com.mikrav.taskmanager.model.repository.TaskRepository;
import com.mikrav.taskmanager.model.repository.UserRepository;
import com.mikrav.taskmanager.service.JwtService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TaskControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();

        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@test.com");
        admin.setPassword("superadmin");
        admin.setRole(UserRole.ADMIN);
        userRepository.save(admin);

        User user = new User();
        user.setUsername("user");
        user.setEmail("user@test.com");
        user.setPassword("superuser");
        user.setRole(UserRole.USER);
        userRepository.save(user);
    }

    @Test
    void testGetAllTasks_shouldNotGetAccess() throws Exception {
        var requestBuilder = get("/tasks");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetAllTasks_shouldGetAccess() throws Exception {
        User admin = userRepository.findByUsername("admin").get();
        String token = jwtService.generateToken(admin);

        var requestBuilder = get("/tasks")
                .header("Authorization", "Bearer " + token);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void testCreateTask_shouldCreateTask() throws Exception {
        User admin = userRepository.findByUsername("admin").get();
        User user = userRepository.findByUsername("user").get();

        JSONObject taskJson = new JSONObject();
        taskJson.put("title", "Test Task");
        taskJson.put("description", "Task Description");
        taskJson.put("assigneeId", admin.getId());

        String token = jwtService.generateToken(user);
        var requestBuilder = post("/tasks")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson.toString());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Task Description"))
                .andExpect(jsonPath("$.status").value(TaskStatus.TODO.toString()))
                .andExpect(jsonPath("$.priority").value(TaskPriority.LOW.toString()))
                .andExpect(jsonPath("$.authorId").value(user.getId()))
                .andExpect(jsonPath("$.assigneeId").value(admin.getId()));
    }

    @Test
    void testUpdateTask_shouldNotUpdateTaskByNotAuthor() throws Exception {
        User user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("user2@test.com");
        user2.setPassword("superuser");
        user2.setRole(UserRole.USER);
        userRepository.save(user2);

        User admin = userRepository.findByUsername("admin").get();
        User user = userRepository.findByUsername("user").get();

        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Task Description");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.LOW);
        task.setAuthor(user);
        task.setAssignee(admin);
        taskRepository.save(task);

        JSONObject taskJson = new JSONObject();
        taskJson.put("title", "New Test Task");
        taskJson.put("description", "New Task Description");
        taskJson.put("status", TaskStatus.IN_PROGRESS.toString());
        taskJson.put("priority", TaskPriority.HIGH.toString());

        String token = jwtService.generateToken(user2);
        var requestBuilder = put("/tasks/" + task.getId())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson.toString());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdateTask_shouldUpdateTask() throws Exception {
        User admin = userRepository.findByUsername("admin").get();
        User user = userRepository.findByUsername("user").get();

        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Task Description");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.LOW);
        task.setAuthor(user);
        task.setAssignee(admin);
        taskRepository.save(task);

        JSONObject taskJson = new JSONObject();
        taskJson.put("title", "New Test Task");
        taskJson.put("description", "New Task Description");
        taskJson.put("status", TaskStatus.IN_PROGRESS.toString());
        taskJson.put("priority", TaskPriority.HIGH.toString());

        String token = jwtService.generateToken(admin);
        var requestBuilder = put("/tasks/" + task.getId())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson.toString());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.title").value("New Test Task"))
                .andExpect(jsonPath("$.description").value("New Task Description"))
                .andExpect(jsonPath("$.status").value(TaskStatus.IN_PROGRESS.toString()))
                .andExpect(jsonPath("$.priority").value(TaskPriority.HIGH.toString()))
                .andExpect(jsonPath("$.authorId").value(user.getId()))
                .andExpect(jsonPath("$.assigneeId").value(admin.getId()));
    }

    @Test
    void testDeleteTask_shouldDeleteTask() throws Exception {
        User admin = userRepository.findByUsername("admin").get();
        User user = userRepository.findByUsername("user").get();

        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Task Description");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.LOW);
        task.setAuthor(user);
        task.setAssignee(admin);
        taskRepository.save(task);

        String token = jwtService.generateToken(admin);
        var requestBuilder = delete("/tasks/" + task.getId())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        Assert.isTrue(!taskRepository.existsById(task.getId()), "Task was not deleted");
    }
}
