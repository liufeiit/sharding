package swan;

import org.springframework.util.NumberUtils;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2013年12月30日 下午6:35:48
 */
public class NumberTest {

	public static void main(String[] args) {
		System.out.println(NumberUtils.parseNumber("123", Long.class));
	}
}
