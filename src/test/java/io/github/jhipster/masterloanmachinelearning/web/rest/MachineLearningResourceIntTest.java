package io.github.jhipster.masterloanmachinelearning.web.rest;

import io.github.jhipster.masterloanmachinelearning.MasterLoanMachineLearningApp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Test class for the MachineLearningResource REST controller.
 *
 * @see MachineLearningResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MasterLoanMachineLearningApp.class)
public class MachineLearningResourceIntTest {

    private MockMvc restMockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        MachineLearningResource machineLearningResource = new MachineLearningResource();
        restMockMvc = MockMvcBuilders
            .standaloneSetup(machineLearningResource)
            .build();
    }

    /**
    * Test getScoreAndRiskForLoan
    */
    @Test
    public void testGetScoreAndRiskForLoan() throws Exception {
        restMockMvc.perform(get("/api/machine-learning/get-score-and-risk-for-loan"))
            .andExpect(status().isOk());
    }

}
