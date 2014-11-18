package org.beyene.protege.example;

import org.beyene.protege.core.data.DataUnit;

public interface MessageHandler {

    public void handleMessage(DataUnit message);
    
    public void notifyDisconnect();
    
}