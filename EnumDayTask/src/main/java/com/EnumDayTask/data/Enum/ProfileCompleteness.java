package com.EnumDayTask.data.Enum;

public enum ProfileCompleteness {

    ZERO(0),
    FIFTY(50),
    HUNDRED(100);


    private final int value;
    ProfileCompleteness(int value) {
        this.value = value;
    }

        public int getValue() {
        return value;
        }

}



