package com.github.zusatzprojekt.madn.interfaces;

import com.github.zusatzprojekt.madn.enums.MadnPlayerId;
import com.github.zusatzprojekt.madn.enums.MadnSpecialFieldType;

public interface MadnFieldExtended {

    MadnSpecialFieldType getFieldType();

    MadnPlayerId getFieldAssignment();

}
