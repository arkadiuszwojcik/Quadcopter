package com.awojcik.qmc.modules.messages;

import java.util.Locale;
import java.util.Scanner;

public class MessageConverter
{
    public static Object fromString(String message)
    {
        if (message == null || message.isEmpty())
            return null;

        Scanner scanner = new Scanner(message);
        scanner.useLocale(Locale.US);

        String firstToken = scanner.next();

        if ("IMU".equals(firstToken)) return parseImuMessage(scanner);

        scanner.close();
        return null;
    }

    private static MsgImu parseImuMessage(Scanner scanner)
    {
        float roll = scanner.nextFloat();
        float pitch = scanner.nextFloat();
        float yaw = scanner.nextFloat();
        scanner.close();
        return new MsgImu(yaw, pitch, roll);
    }
}
