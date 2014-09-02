package com.awojcik.qmc.modules.settings.commands;

import android.view.View;

import com.awojcik.qmc.modules.common.IntraModuleMessenger;
import com.awojcik.qmc.modules.settings.messages.LoadMessage;
import com.google.inject.Inject;

import gueei.binding.Command;

public class LoadCommand extends Command
{
    @Inject
    public LoadCommand(IntraModuleMessenger intraMessenger)
    {
        this.intraModuleMessenger = intraMessenger;
    }

    @Override
    public void Invoke(View view, Object... objects)
    {
        this.intraModuleMessenger.send(new LoadMessage());
    }

    private final IntraModuleMessenger intraModuleMessenger;
}
