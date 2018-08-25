package com.hdh.lifeup.controller;

import com.hdh.lifeup.dto.TaskDTO;
import com.hdh.lifeup.service.TaskService;
import com.hdh.lifeup.util.Result;
import com.hdh.lifeup.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * TaskController class<br/>
 *
 * @author hdonghong
 * @since 2018/08/18
 */
@Api(description = "个人事项模块，还没写文档。。。")
@RestController
@RequestMapping("/task")
public class TaskController {

    private TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @ApiOperation(value = " 提交待办事项")
    @PostMapping("/new")
    public ResultVO<?> addTask(@RequestBody TaskDTO taskDTO) {
        return Result.success(taskService.insert(taskDTO));
    }

    @ApiOperation(value = "获取任务事项详情")
    @GetMapping("/{taskId}")
    public ResultVO<TaskDTO> getOne(@PathVariable Long taskId) {
        return Result.success(taskService.getOne(taskId));
    }

    @ApiOperation(value = "修改待办事项")
    @PutMapping("/")
    public ResultVO<?> updateTask(@RequestBody TaskDTO taskDTO) {
        taskService.update(taskDTO);
        return Result.success();
    }

    @ApiOperation(value = "完成/放弃任务事项")
    @PutMapping("/status")
    public ResultVO<?> changeTaskStatus(@RequestBody TaskDTO taskDTO) {
        // TODO
        taskService.update(taskDTO);
        return Result.success();
    }

    @ApiOperation(value = "删除任务事项")
    @DeleteMapping("/{taskId}")
    public ResultVO<?> deleteTask(@PathVariable("taskId") Long taskId) {
        taskService.deleteLogically(taskId);
        return Result.success();
    }
}
