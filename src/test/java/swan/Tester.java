package swan;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import swan.mapper.UserMapper;
import swan.model.User;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2014年2月24日 下午6:05:02
 */
public class Tester {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "core-mapper.xml" });
		UserMapper mapper = context.getBean(UserMapper.class);
		User user = new User();
		user.setUserId(11);
		user.setNick("liufei===========");
		user.setPassword("lF1229lf");
//		mapper.addUser(user);
		user = mapper.getUserByNickAndPass(11, "liufei1", "lF1229lf");
		System.out.println("user : " + user);
	}
}
