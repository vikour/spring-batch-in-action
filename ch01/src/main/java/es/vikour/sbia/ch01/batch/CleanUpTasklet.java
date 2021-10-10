package es.vikour.sbia.ch01.batch;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import lombok.Setter;

@Setter
public class CleanUpTasklet implements Tasklet {

	private String targetDirectory; 
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		File file = new File(targetDirectory);
		FileUtils.forceDelete(file);
		
		return RepeatStatus.FINISHED;
	}

}
