package data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2014年3月3日 下午3:02:47
 */
public class DT {

	public static void main(String[] args) throws Exception {
		ExecutorService executor = newInstanceExecutor("dt.nova");
		Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					new DT().dataFlush("线程1", "2014-02-20 23:59:59", "2014-02-22 16:45:59");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread1.setName("线程1");
		executor.execute(thread1);

		Thread thread2 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					new DT().dataFlush("线程2", "2014-02-22 16:45:59", "2014-02-24 16:45:59");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread2.setName("线程2");
		executor.execute(thread2);

		Thread thread3 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					new DT().dataFlush("线程3", "2014-02-24 16:45:59", "2014-02-26 16:45:59");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread3.setName("线程3");
		executor.execute(thread3);

		Thread thread4 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					new DT().dataFlush("线程4", "2014-02-26 16:45:59", "2014-03-01 16:45:59");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread4.setName("线程4");
		executor.execute(thread4);

		Thread thread5 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					new DT().dataFlush("线程5", "2014-03-01 16:45:59", "2014-03-03 16:45:59");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread5.setName("线程5");
		executor.execute(thread5);
	}

	private void dataFlush(String name, String s, String e) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "core-mapper.xml" });
		NamedParameterJdbcTemplate namedJdbcTemplate = context.getBean("namedJdbcTemplate",
				NamedParameterJdbcTemplate.class);
		String querySQL = "SELECT * FROM nova_user_notify "
				+ "WHERE createdAt BETWEEN :startTime and :endTime LIMIT :startIndex, :pageSize";

		String writeSQL = "INSERT INTO nova_user_notify_%s (" + "userId, " + "formUserId, " + "notifyType, "
				+ "notifyTopType, " + "linkedId, " + "notifyStatus, " + "notifyAction, " + "handleFrom, "
				+ "notifyText, " + "createdAt, " + "notifyBackOne, " + "notifyBackTwo, " + "notifyBackThree "
				+ ")VALUES(" + ":userId, " + ":formUserId, " + ":notifyType, " + ":notifyTopType, " + ":linkedId, "
				+ ":notifyStatus, " + ":notifyAction, " + ":handleFrom, " + ":notifyText, " + ":createdAt, "
				+ ":notifyBackOne, " + ":notifyBackTwo, " + ":notifyBackThree)";

		Date startTime = parseDate(s, S);
		Date endTime = parseDate(e, S);
		long pageSize = 20000;
		int pageNum = 1;
		long startIndex = (pageNum - 1) * pageSize;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);
		paramMap.put("startIndex", startIndex);
		paramMap.put("pageSize", pageSize);
		List<Map<String, Object>> items = null;
		long count = 0;
		long start = System.currentTimeMillis();
		do {
			try {
				items = namedJdbcTemplate.queryForList(querySQL, paramMap);
			} catch (DataAccessException e1) {
				e1.printStackTrace(System.err);
			}
			if (items != null && !items.isEmpty()) {
				System.out.println(name + " 第" + pageNum + "页, 取出：" + items.size() + "条数据.");
				for (Map<String, Object> m : items) {
					Object val = m.get("userId");
					if (val == null) {
						continue;
					}
					long userId = 0L;
					try {
						userId = Long.parseLong(String.valueOf(val));
					} catch (NumberFormatException ex) {
						continue;
					}
					if (userId <= 0L) {
						continue;
					}
					long ext = userId % 128;
					String sql = String.format(writeSQL, ext);
					try {
						namedJdbcTemplate.update(sql, m);
						// System.out.println("saved into nova_user_notify_" +
						// ext + " >> " + m);
						count++;
					} catch (DataAccessException ex) {
						ex.printStackTrace(System.err);
					}
				}
			}
			startIndex = (++pageNum - 1) * pageSize;
			paramMap.put("startTime", startTime);
			paramMap.put("endTime", endTime);
			paramMap.put("startIndex", startIndex);
			paramMap.put("pageSize", pageSize);
			System.out.println(name + " 第" + pageNum + "页, 时间：" + formatDate(new Date(), S));
		} while (items != null && !items.isEmpty());
		long end = System.currentTimeMillis();
		long ps = (end - start) / 1000 / 60;
		System.out.println(name + " 一共拷贝：" + count + "条数据, [" + formatDate(startTime, S) + ", "
				+ formatDate(endTime, S) + "], 耗时：" + ps + "分钟.");
	}

	final String S = "yyyy-MM-dd HH:mm:ss";

	String formatDate(Date date, String pattern) {
		if (date == null)
			throw new IllegalArgumentException("date is null");
		if (pattern == null)
			throw new IllegalArgumentException("pattern is null");
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(date);
	}

	Date parseDate(String date, String pattern) throws ParseException {
		if (date == null)
			throw new IllegalArgumentException("date is null");
		if (pattern == null)
			throw new IllegalArgumentException("pattern is null");
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.parse(date);
	}

	static ExecutorService newInstanceExecutor(final String threadName) {
		return Executors.newCachedThreadPool(new ThreadFactory() {
			final AtomicInteger threadNumber = new AtomicInteger(1);

			public Thread newThread(Runnable runnable) {
				Thread thread = new Thread(Thread.currentThread().getThreadGroup(), runnable, "DT-" + threadName + "-"
						+ threadNumber.getAndIncrement(), 0);
				// thread.setDaemon(true);
				if (thread.getPriority() != Thread.NORM_PRIORITY) {
					thread.setPriority(Thread.NORM_PRIORITY);
				}
				return thread;
			}
		});
	}
}