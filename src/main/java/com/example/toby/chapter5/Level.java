package com.example.toby.chapter5;

public enum Level{
    BASIC(1),SILVER(2),GOLD(3);
    private final int value;

    Level(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Level valueOf(int value){
        switch (value){
            case 1:return BASIC;
            case 2:return SILVER;
            case 3:return GOLD;
            default:throw new AssertionError("알 수 없음"+value);
        }
    }
}