package com.iaiotecp.backend.control;

import com.iaiotecp.backend.control.model.AutomationRule;
import com.iaiotecp.backend.device.model.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/automation")
public class AutomationController {

    @GetMapping("/rules")
    public ResponseEntity<Result<List<AutomationRule>>> listRules() {
        AutomationRule rule = new AutomationRule();
        rule.setId("auto_001");
        rule.setName("自动调光");
        rule.setCondition("光照度 < 300");
        rule.setAction("打开灯光");
        rule.setEnabled(true);
        rule.setCreateTime(Instant.now().toString());
        return ResponseEntity.ok(Result.success(List.of(rule)));
    }
}



