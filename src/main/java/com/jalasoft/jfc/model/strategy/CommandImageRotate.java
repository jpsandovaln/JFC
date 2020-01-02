/*
 * Copyright (c) 2019 Jalasoft.
 *
 * This software is the confidential and proprietary information of Jalasoft.
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Jalasoft.
 */

package com.jalasoft.jfc.model.command;

import com.jalasoft.jfc.model.exception.CommandValueException;
import com.jalasoft.jfc.model.pdf.ImageMagickCommand;

/**
 * Rotates an image.
 *
 * @version 0.1 19 Dic 2019
 *
 * @author Enrique Carrizales
 */
public class CommandImageRotate implements ICommandStrategy {

    // Content command value.
    private float commandValue;

    /**
     * It creates a new CommandImageRotate object.
     * @param commandValue contains a value.
     */
    public CommandImageRotate(float commandValue) {
        this.commandValue = commandValue;
    }

    /**
     * It builds a command.
     * @return String of a command.
     * @throws CommandValueException when is a invalid command.
     */
    @Override
    public String command() throws CommandValueException {
        try {
            if (commandValue == 0.0) {
                return "";
            }
            if (commandValue > 0.0) {
                return this.SPACE + ImageMagickCommand.ROTATE.getCommand() + this.SPACE + commandValue;
            }
            throw new CommandValueException("Invalid Image rotate value\n", "command value is invalid\n");
        } catch (NullPointerException nex) {
            throw  new CommandValueException("Command value is NULL ", this.getClass().getName());
        }
    }
}
