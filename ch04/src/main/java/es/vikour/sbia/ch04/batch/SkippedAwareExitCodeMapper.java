package es.vikour.sbia.ch04.batch;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.launch.support.ExitCodeMapper;

public class SkippedAwareExitCodeMapper implements ExitCodeMapper {

	@Override
	public int intValue(String exitCode) {
		int exitCodeInt = 0;
		
		if (ExitStatus.FAILED.getExitCode().equals(exitCode)) 
			exitCodeInt = 1;
		else if ("COMPLETED WITH SKIPS".equals(exitCode)) 
			exitCodeInt = 3;
		else if (!ExitStatus.COMPLETED.equals(exitCode)) 
			exitCodeInt = 2;
		
		return exitCodeInt;
	}

}
