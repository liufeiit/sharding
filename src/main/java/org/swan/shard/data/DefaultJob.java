package org.swan.shard.data;

import java.util.List;
import java.util.Map;

import org.springframework.core.task.TaskExecutor;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2014年3月3日 下午2:46:06
 */
public class DefaultJob implements Job<Map<String, Object>> {
	private TaskExecutor taskExecutor;
	private JobParameter parameter;

	public DefaultJob(TaskExecutor taskExecutor, JobParameter parameter) {
		super();
		this.taskExecutor = taskExecutor;
		this.parameter = parameter;
	}

	public DefaultJob(TaskExecutor taskExecutor) {
		super();
		this.taskExecutor = taskExecutor;
	}

	@Override
	public JobParameter getParameter() {
		return parameter;
	}

	@Override
	public JobResult exe(final ItemReader<Map<String, Object>> reader, final ItemWriter<Map<String, Object>> writer)
			throws DataJobException {
		JobResult jobResult = new JobResult();
		try {
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						List<Map<String, Object>> data = reader.read(getParameter());
						if(data == null || data.isEmpty()) {
							return;
						}
						writer.write(data);
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
			});
			jobResult.setSuccess(true);
		} catch (Exception e) {
			jobResult.setMsg(e.getLocalizedMessage());
			throw new DataJobException(e);
		}
		return jobResult;
	}
}