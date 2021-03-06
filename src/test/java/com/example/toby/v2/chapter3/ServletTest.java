package com.example.toby.v2.chapter3;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class ServletTest {

    @Test
    void 서블릿_test() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET","/hello");
        request.addParameter("name","Spring");

        MockHttpServletResponse response = new MockHttpServletResponse();

        SimpleGetServlet servlet = new SimpleGetServlet();
        servlet.service(request,response);

        assertThat(response.getContentAsString()).contains("Hello Spring");
    }

}