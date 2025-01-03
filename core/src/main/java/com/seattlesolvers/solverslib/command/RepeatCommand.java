/*
 * ----------------------------------------------------------------------------
 *  Copyright (c) 2018-2019 FIRST. All Rights Reserved.
 *  Open Source Software - may be modified and shared by FRC teams. The code
 *  must be accompanied by the FIRST BSD license file in the root directory of
 *  the project.
 * ----------------------------------------------------------------------------
 */

package com.seattlesolvers.solverslib.command;

import static com.seattlesolvers.solverslib.command.CommandGroupBase.registerGroupedCommands;
import static com.seattlesolvers.solverslib.command.CommandGroupBase.requireUngrouped;

/**
 * A command that runs another command repeatedly, restarting it when it ends, until this command is
 * interrupted. While this class does not extend {@link CommandGroupBase}, it is still considered a
 * CommandGroup, as it allows one to compose another command within it; the command instances that
 * are passed to it cannot be added to any other groups, or scheduled individually.
 *
 * <p>As a rule, CommandGroups require the union of the requirements of their component commands.
 *
 * @author Ryan
 */
public class RepeatCommand extends CommandBase{

    protected final Command m_command;

    /**
     * Creates a new RepeatCommand. Will run another command repeatedly, restarting it whenever it
     * ends, until this command is interrupted.
     *
     * @param command the command to run repeatedly
     */
    public RepeatCommand(Command command) {
        requireUngrouped(command);
        registerGroupedCommands(command);
        m_command = command;
        m_requirements.addAll(command.getRequirements());
    }


    @Override
    public void initialize() {
        m_command.initialize();
    }


    @Override
    public void execute() {
        m_command.execute();
        if (m_command.isFinished()) {
            // restart command
            m_command.end(false);
            m_command.initialize();
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        m_command.end(interrupted);
    }

    @Override
    public boolean runsWhenDisabled() {
        return m_command.runsWhenDisabled();
    }
}