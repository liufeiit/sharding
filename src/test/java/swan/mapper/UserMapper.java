package swan.mapper;

import org.apache.ibatis.annotations.Param;
import org.matrix.swan.mybatis.type.ShardingType;

import swan.model.User;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2014年2月24日 下午5:07:11
 */
@ShardingType(tableName="", shardType="", shardBy="")
public interface UserMapper {

	long addUser(User user);
	
	User getUser(@Param(value = "userId") long userId);
	
	User getUserByNickAndPass(@Param(value = "nick") String nick, @Param(value = "password") String password);
}