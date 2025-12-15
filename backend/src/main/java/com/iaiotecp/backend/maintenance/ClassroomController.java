package com.iaiotecp.backend.maintenance;

import com.iaiotecp.backend.device.model.Result;
import com.iaiotecp.backend.maintenance.model.Classroom;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/classrooms")
public class ClassroomController {

    @GetMapping
    public ResponseEntity<Result<List<Classroom>>> list() {
        List<Classroom> data = List.of(
                new Classroom("classroom_101", "101教室", "教学楼A栋1层", 5),
                new Classroom("classroom_102", "102教室", "教学楼A栋1层", 3)
        );
        return ResponseEntity.ok(Result.success(data));
    }
}



