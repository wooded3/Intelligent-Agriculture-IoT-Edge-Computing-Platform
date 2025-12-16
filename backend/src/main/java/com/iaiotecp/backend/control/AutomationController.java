package com.iaiotecp.backend.control;

import com.iaiotecp.backend.control.model.AutomationRule;
import com.iaiotecp.backend.device.model.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/automation")
public class AutomationController {

    // 临时存储自动化规则（演示用，实际应该使用数据库）
    private final Map<String, AutomationRule> rulesStorage = new ConcurrentHashMap<>();

    public AutomationController() {
        // 初始化示例数据
        AutomationRule rule = new AutomationRule();
        rule.setId("auto_001");
        rule.setName("自动调光");
        rule.setCondition("光照度 < 300");
        rule.setAction("打开灯光");
        rule.setEnabled(true);
        rule.setCreateTime(Instant.now().toString());
        rulesStorage.put("auto_001", rule);
    }

    @GetMapping("/rules")
    public ResponseEntity<Result<List<AutomationRule>>> listRules() {
        return ResponseEntity.ok(Result.success(new ArrayList<>(rulesStorage.values())));
    }

    @PostMapping("/rules")
    public ResponseEntity<Result<AutomationRule>> createRule(@RequestBody AutomationRule rule) {
        try {
            String id = "auto_" + System.currentTimeMillis();
            rule.setId(id);
            rule.setCreateTime(Instant.now().toString());
            rulesStorage.put(id, rule);
            return ResponseEntity.ok(Result.success(rule));
        } catch (Exception e) {
            return ResponseEntity.ok(Result.error("创建规则失败: " + e.getMessage()));
        }
    }

    @PatchMapping("/rules/{id}/toggle")
    public ResponseEntity<Result<Void>> toggleRule(@PathVariable String id) {
        try {
            AutomationRule rule = rulesStorage.get(id);
            if (rule == null) {
                return ResponseEntity.ok(Result.error("规则不存在"));
            }
            rule.setEnabled(!rule.isEnabled());
            rule.setUpdateTime(Instant.now().toString());
            return ResponseEntity.ok(Result.success(null));
        } catch (Exception e) {
            return ResponseEntity.ok(Result.error("操作失败: " + e.getMessage()));
        }
    }

    @DeleteMapping("/rules/{id}")
    public ResponseEntity<Result<Void>> deleteRule(@PathVariable String id) {
        try {
            if (rulesStorage.remove(id) == null) {
                return ResponseEntity.ok(Result.error("规则不存在"));
            }
            return ResponseEntity.ok(Result.success(null));
        } catch (Exception e) {
            return ResponseEntity.ok(Result.error("删除失败: " + e.getMessage()));
        }
    }
}





