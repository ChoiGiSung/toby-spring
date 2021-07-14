package com.example.toby;

import com.example.toby.chapter3.Calculator;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import java.io.IOException;

public class CalcSumTest {

    Calculator calculator = new Calculator();
    String numFilepath = "src/main/resources/static/number.txt";

    @Test
    public void sumOfNumbers() throws IOException {
        Integer integer = calculator.calcSum(numFilepath);

        assertThat(10).isEqualTo(integer);
    }

    @Test
    public void multiplyOfNumbers() throws IOException {
        Integer integer = calculator.calcMultiply(numFilepath);

        assertThat(24).isEqualTo(integer);
    }
}
