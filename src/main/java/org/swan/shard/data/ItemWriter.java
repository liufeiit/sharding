package org.swan.shard.data;

import java.util.List;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2014年3月3日 下午2:20:18
 */
public interface ItemWriter<T> {
	JobResult write(List<? extends T> items) throws DataJobException;
}