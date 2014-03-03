package swan;

import java.io.File;
import java.io.FileWriter;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2014年2月27日 上午11:43:11
 */
public class TableScript {

	public static void main(String[] args) throws Exception {
		FileWriter writer = new FileWriter(new File("/home/lf/notify.sql"), false);
		String _sql = "CREATE TABLE `ruooglenova`.`nova_user_notify_%s` ("
				+ "`id` bigint(20) NOT NULL AUTO_INCREMENT,"
				+ "`userId` bigint(20) NOT NULL,"
				+ "`formUserId` bigint(20) DEFAULT '0',"
				+ "`notifyType` int(10) NOT NULL,"
				+ "`linkedId` bigint(20) DEFAULT '0',"
				+ "`notifyStatus` tinyint(4) NOT NULL DEFAULT '0',"
				+ "`handleFrom` tinyint(4) DEFAULT '0',"
				+ "`notifyText` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT '',"
				+ "`createdAt` datetime NOT NULL,"
				+ "`notifyBackOne` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT '',"
				+ "`notifyBackTwo` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT '',"
				+ "`notifyBackThree` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT '',"
				+ "`notifyAction` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '',"
				+ "`notifyTopType` int(10) DEFAULT NULL,"
				+ "PRIMARY KEY (`id`),"
				+ "KEY `index_needhandle` (`notifyStatus`,`createdAt`),"
				+ "KEY `index_mynotify` (`userId`,`notifyStatus`,`createdAt`),"
				+ "KEY `index_mynotifycount` (`userId`,`notifyStatus`),"
				+ "KEY `index_user_id` (`userId`),"
				+ "KEY `index_mynotifysince` (`userId`,`notifyStatus`,`id`,`createdAt`),"
				+ "KEY `index_createdAt` (`createdAt`)) "
				+ "ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户通知表';";
		
		for (int i = 0; i < 128; i++) {
			String SQL = String.format(_sql, i);
			System.out.println(SQL);
			writer.write(SQL + "\n");
		}
		
		writer.flush();
		writer.close();
		writer = null;
	}
}
