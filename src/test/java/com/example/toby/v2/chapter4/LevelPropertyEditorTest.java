package com.example.toby.v2.chapter4;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LevelPropertyEditorTest {

    @Test
    void propertyTest(){
        LevelPropertyEditor editor = new LevelPropertyEditor();
        editor.setAsText("3");

        assertThat((Level)editor.getValue()).isEqualTo(Level.GOLD);
    }

    //컨트롤러 클래스에 작성해야 DTO안에 있는 값을 바인딩할 수 있음
    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.registerCustomEditor(Level.class,new LevelPropertyEditor());
    }
}