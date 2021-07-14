package com.example.toby;

import com.example.toby.chapter3.Calculator;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import java.io.IOException;

public class CalcSumTest {

    @Test
    public void sumOfNumbers() throws IOException {
        Calculator calculator = new Calculator();
        Integer integer = calculator.calcSum("src/main/resources/static/number.txt");

        assertThat(10).isEqualTo(integer);
    }
}
