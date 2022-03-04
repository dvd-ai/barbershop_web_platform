package com.app.barbershopweb.workspace;

import com.app.barbershopweb.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/workspaces")
@Validated
public class WorkspaceController {

    private final WorkspaceService workspaceService;
    private final WorkspaceConverter workspaceConverter;

    public WorkspaceController(WorkspaceService workspaceService, WorkspaceConverter workspaceConverter) {
        this.workspaceService = workspaceService;
        this.workspaceConverter = workspaceConverter;
    }

    @GetMapping
    public ResponseEntity<List<WorkspaceDto>> getWorkspaces() {
        return new ResponseEntity<>(
                workspaceConverter.workspaceEntityListToDtoList(workspaceService.getWorkspaces()), HttpStatus.OK
        );
    }

    @GetMapping("/{workspaceId}")
    public ResponseEntity<WorkspaceDto> getWorkspaceById(@PathVariable @Min(1) Long workspaceId) {
        Workspace workspace = workspaceService.findWorkspaceById(workspaceId)
                .orElseThrow(() -> new NotFoundException("Workspace with id " + workspaceId + " not found."));
        return new ResponseEntity<>(workspaceConverter.mapToDto(workspace), HttpStatus.OK);
    }

    @PostMapping
    //id is obligation due to @Valid
    public ResponseEntity<Long> addWorkspace(@RequestBody @Valid WorkspaceDto workspaceDto) {
        Workspace entity = workspaceConverter.mapToEntity(workspaceDto);
        return new ResponseEntity<>(workspaceService.addWorkspace(entity), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<WorkspaceDto> updateWorkspace(@RequestBody @Valid WorkspaceDto workspaceDto) {
        Workspace entity = workspaceService.updateWorkspace(workspaceConverter.mapToEntity(workspaceDto))
                .orElseThrow(() -> new NotFoundException("Workspace with id '" + workspaceDto.workspaceId() + "' not found."));
        return new ResponseEntity<>(workspaceConverter.mapToDto(entity), HttpStatus.OK);
    }

    @DeleteMapping("/{workspaceId}")
    public ResponseEntity<Object> deleteWorkspaceById(@PathVariable @Min(1) Long workspaceId) {
        workspaceService.deleteWorkspaceById(workspaceId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
