package com.github.zusatzprojekt.madn.interfaces;

import java.util.Map;

public interface FxmlControllerValueReceiver extends FxmlController{
    void receiveValues(Map<String, Object> values);
}
