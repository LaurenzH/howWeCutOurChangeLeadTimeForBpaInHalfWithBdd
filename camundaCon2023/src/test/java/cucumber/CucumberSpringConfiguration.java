package cucumber;


import com.camundabpabdd.workflow.Application;
import io.cucumber.spring.CucumberContextConfiguration;
import org.camunda.bpm.extension.process_test_coverage.spring_test.ProcessEngineCoverageConfiguration;
import org.camunda.bpm.extension.process_test_coverage.spring_test.ProcessEngineCoverageTestExecutionListener;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;

@CucumberContextConfiguration
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CoverageTestConfiguration.class, ProcessEngineCoverageConfiguration.class})
@TestExecutionListeners(value = ProcessEngineCoverageTestExecutionListener.class,
       mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class CucumberSpringConfiguration {
}