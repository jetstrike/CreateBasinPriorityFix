package com.jetstrike.createbasinpriority;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod("createbasinpriority")
public class CreateBasinPriorityFix {
    public static final Logger LOGGER = LoggerFactory.getLogger(CreateBasinPriorityFix.class);

    public CreateBasinPriorityFix(IEventBus modEventBus) {
        LOGGER.info("Create Basin Priority Fix initialized!");
    }
}
