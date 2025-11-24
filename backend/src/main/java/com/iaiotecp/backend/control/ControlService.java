package com.iaiotecp.backend.control;

import com.iaiotecp.backend.control.model.ControlCommand;

public interface ControlService {

    void execute(ControlCommand command);
}
