/*
 * Copyright (c) 2019 Jalasoft.
 *
 * This software is the confidential and proprietary information of Jalasoft.
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Jalasoft.
 */

package com.jalasoft.jfc.model.command.ffmpeg;

import com.jalasoft.jfc.model.exception.CommandValueException;
import com.jalasoft.jfc.model.command.ICommandStrategy;
import com.jalasoft.jfc.model.exception.ErrorMessageJfc;
import com.jalasoft.jfc.model.video.VideoCommand;

/**
 * Changes Aspect Ratio.
 *
 * @version 0.1 20 Dic 2019.
 *
 * @author Oscar Lopez.
 */
public class CommandVideoAspectRatio implements ICommandStrategy {

    // Contents command value.
    private String commandValue;

    /**
     * Creates a new CommandVideoAspectRatio object.
     * @param commandValue, receive a value.
     */
    public CommandVideoAspectRatio(String commandValue) {
        this.commandValue = commandValue;
    }

    /**
     * Builds a command.
     * @return command concatenated.
     */
    @Override
    public String command() throws CommandValueException {
        try {
            if (commandValue != "") {
                return this.SPACE + VideoCommand.ASPECT_RATIO.getCommand() + this.SPACE + commandValue;
            }
            throw new CommandValueException(ErrorMessageJfc.ASPECTRATIO_NOT_CHANGE.getErrorMessageJfc(), this.
                    getClass().getName());
        } catch (CommandValueException cve) {
            throw new CommandValueException(cve.getMessage(), this.getClass().getName());
        }
    }
}
