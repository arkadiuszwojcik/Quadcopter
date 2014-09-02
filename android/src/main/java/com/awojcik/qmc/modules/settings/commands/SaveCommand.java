package com.awojcik.qmc.modules.settings.commands;

import android.view.View;

import com.awojcik.qmc.modules.common.IntraModuleMessenger;
import com.awojcik.qmc.modules.settings.messages.SaveMessage;
import com.google.inject.Inject;

import gueei.binding.Command;

public class SaveCommand extends Command
{
    @Inject
    public SaveCommand(IntraModuleMessenger intraMessenger)
    {
        this.intraModuleMessenger = intraMessenger;
    }

    @Override
    public void Invoke(View view, Object... objects)
    {
        this.intraModuleMessenger.send(new SaveMessage());
    }

    private final IntraModuleMessenger intraModuleMessenger;
}
