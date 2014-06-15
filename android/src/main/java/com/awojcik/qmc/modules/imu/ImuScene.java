package com.awojcik.qmc.modules.imu;

import com.awojcik.qmc.modules.imu.models.CuboidModel;
import com.awojcik.qmc.modules.messages.MsgImu;
import com.awojcik.qmc.opengl.GLScene;

import de.greenrobot.event.EventBus;

public class ImuScene extends GLScene
{
    CuboidModel cuboid = new CuboidModel();

    public ImuScene(EventBus eventBus)
    {
        eventBus.register(this);
        this.initScene();
    }

    private void initScene()
    {
        this.cuboid.setPosition(0, -1, -6);
        this.cuboid.setRotationEuler(25, 20, 0);
        this.addSceneObject(this.cuboid);
    }

    public void onEventBackgroundThread(MsgImu msgImu)
    {
        this.cuboid.setRotationEuler(msgImu.getRoll(), msgImu.getPitch(), msgImu.getYaw());
        this.redraw();
    }
}
