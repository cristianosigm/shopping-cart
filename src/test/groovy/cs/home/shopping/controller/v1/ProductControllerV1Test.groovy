package cs.home.shopping.controller.v1

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

@AutoConfigureMockMvc
@WebMvcTest
class ProductControllerV1Test extends Specification {

    @Autowired
    private MockMvc mvc;

    def "should load ProductController successfully"() {
        expect: "Success"

//        mvc.perform(get(""))
//                .andExpect(status().isOK())
//                .andReturn();
    }
}
