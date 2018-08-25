package com.hdh.lifeup.controller;

import com.hdh.lifeup.auth.ApiLimiting;
import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.dto.TaskDTO;
import com.hdh.lifeup.enums.CodeMsgEnum;
import com.hdh.lifeup.service.TaskService;
import com.hdh.lifeup.util.Result;
import com.hdh.lifeup.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * TaskController class<br/>
 *
 * @author hdonghong
 * @since 2018/08/18
 */
@Api(description = "个人事项模块")
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @ApiLimiting
    @ApiOperation(value = " 提交待办事项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "AUTHENTICITY_TOKEN", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "taskDTO json", required = true, paramType = "post", dataType = "json"),
    })
    @PostMapping("/new")
    public ResultVO<?> addTask(@RequestBody TaskDTO taskDTO) {
        return Result.success(taskService.insert(taskDTO));
    }

    @ApiLimiting
    @ApiOperation(value = "获取个人的任务事项详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "AUTHENTICITY_TOKEN", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "taskId", required = true, paramType = "path", dataType = "String"),
    })
    @GetMapping("/{taskId}")
    public ResultVO<TaskDTO> getOne(@PathVariable Long taskId) {
        TaskDTO taskDTO = taskService.getOne(taskId);
        return UserContext.get().getUserId().equals(taskDTO.getUserId()) ?
                Result.success(taskDTO) : Result.error(CodeMsgEnum.ACCESS_ILLEGAL);
    }

    @ApiLimiting
    @ApiOperation(value = "修改待办事项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "AUTHENTICITY_TOKEN", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "taskDTO json", required = true, paramType = "post", dataType = "json"),
    })
    @PutMapping("/")
    public ResultVO<?> updateTask(@RequestBody TaskDTO taskDTO) {
        taskService.update(taskDTO);
        return Result.success();
    }

    @ApiLimiting
    @ApiOperation(value = "完成/放弃任务事项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "AUTHENTICITY_TOKEN", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "taskStatus，只有两种值：success/fail", required = true, paramType = "post", dataType = "json"),
    })
    @PutMapping("/{taskId}/{taskStatus}")
    public ResultVO<?> changeTaskStatus(
            @PathVariable("taskId") Long taskId, @PathVariable("taskStatus") Integer taskStatus) {
        TaskDTO taskDTO = taskService.getOne(taskId);
        taskDTO.setTaskStatus(taskStatus);
        taskService.update(taskDTO);
        return Result.success();
    }

    @ApiLimiting
    @ApiOperation(value = "删除任务事项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "AUTHENTICITY_TOKEN", required = true, paramType = "header", dataType = "String"),
    })
    @DeleteMapping("/{taskId}")
    public ResultVO<?> deleteTask(@PathVariable("taskId") Long taskId) {
        taskService.deleteLogically(taskId);
        return Result.success();
    }
}
