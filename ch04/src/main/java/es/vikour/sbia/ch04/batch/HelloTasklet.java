package es.vikour.sbia.ch04.batch;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class HelloTasklet implements Tasklet {

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		
		JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobParameters();
		
		if (jobParameters.isEmpty()) {
			System.out.println("No job parameters!");
		}
		else {
			
			for (Entry<String, JobParameter> param : jobParameters.getParameters().entrySet()) {
				String out = String.format("%s = %s (%s)", param.getKey(), param.getValue().getValue(), param.getValue().getType());
				System.out.println(out);
			}
			
		}

		return RepeatStatus.FINISHED;
	}
	

}
