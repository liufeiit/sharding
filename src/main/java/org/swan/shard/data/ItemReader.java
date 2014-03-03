package org.swan.shard.data;

import java.util.List;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2014年3月3日 下午2:15:08
 */
public interface ItemReader<T> {
	List<T> read(JobParameter parameter) throws DataJobException;
}