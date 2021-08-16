package com.example.toby.v2.chapter4;

import java.beans.PropertyEditorSupport;

public class LevelPropertyEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        //오브젝트를 스트링 타입으로 변환
        return String.valueOf(((Level)this.getValue()).getValue());
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        //스트링 타입의 파라미터를 오브젝트로 변환
        this.setValue(Level.valueOf(Integer.parseInt(text.trim())));
    }
}
