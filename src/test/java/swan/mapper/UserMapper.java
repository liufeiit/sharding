package swan.mapper;

import org.apache.ibatis.annotations.Param;
import org.swan.shard.mybatis.type.MetaShard;

import swan.model.User;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2014年2月24日 下午5:07:11
 */
@MetaShard(name="User", size=2, by="userId")
public interface UserMapper {

	long addUser(User user);
	
	User getUser(@Param(value = "userId") long userId);
	
	User getUserByNickAndPass(@Param(value = "userId") long userId, @Param(value = "nick") String nick, @Param(value = "password") String password);
}