package com.iaiotecp.backend.control;

import com.iaiotecp.backend.control.model.ControlCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ControlServiceImpl implements ControlService {

    private static final Logger log = LoggerFactory.getLogger(ControlServiceImpl.class);

    @Override
    public void execute(ControlCommand command) {
        // TODO: implement real control logic
        log.info("Executing control command: deviceId={}, actionType={}, value={}",
                command.getDeviceId(), command.getActionType(), command.getValue());
    }
}
