package swan;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2014年1月24日 上午10:40:18
 */
public class GitManager {
	
	private final static Map<String, String> env = new HashMap<String, String>();
	private final static List<String> envPair = new ArrayList<String>();
	private static String[] envArray = new String[]{};
	
	static {
		env.putAll(System.getenv());
		Properties osProps = System.getProperties();
		for (Map.Entry<Object, Object> e : osProps.entrySet()) {
			env.put(String.valueOf(e.getKey()), String.valueOf(e.getValue()));
		}
		for (Map.Entry<String, String> e : env.entrySet()) {
			envPair.add(e.getKey() + "=" + e.getValue());
		}
		envArray = envPair.toArray(new String[envPair.size()]);
	}

	public static void main(String[] args) {
		StringWriter writer = new StringWriter();
		String[] checkoutNewBranch = new String[] { "git", "checkout", "-b", "b_iwtest01_error_366" };
		String[] checkoutMaster = new String[] { "git", "checkout", "master" };
		String[] branchs = new String[] { "git", "branch" };
		String[] checkoutBranch = new String[] { "git", "checkout", "development" };
		File dir = new File("D://workspace/Web_Nova/");
		String[] ls = new String[]{"ls", "-l"};
		System.out.println("exitValue : " + exec(branchs, envArray, dir, writer));
		System.out.println(writer.toString());
	}

	private static int exec(String[] cmdarray, String[] envp, File dir, Writer writer) {
		try {
			if(cmdarray == null || cmdarray.length <= 0) {
				return -1;
			}
			Process process = null;
			if(cmdarray.length == 1) {
				process = Runtime.getRuntime().exec(cmdarray[0], envp, dir);
			} else {
				process = Runtime.getRuntime().exec(cmdarray, envp, dir);
			}
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String in = null;
			while ((in = stdIn.readLine()) != null) {
				writer.write(in + "\n");
			}
			BufferedReader stdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String err = null;
			while ((err = stdErr.readLine()) != null) {
				writer.write(err + "\n");
			}
			process.getOutputStream().close();
			return 0;
		} catch (Throwable e) {
			e.printStackTrace(new PrintWriter(writer));
		}
		return -1;
	}
	
	public static void main0(String args[]) {
        try {
    		File dir = new File("/home/lf/workspace/Web_Nova/");
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("git branch", new String[]{}, dir);
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String in = null;
			while ((in = stdIn.readLine()) != null) {
				System.out.println(in);
			}
			BufferedReader stdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String err = null;
			while ((err = stdErr.readLine()) != null) {
				System.out.println(err);
			}
			process.getOutputStream().close();
        } catch (Throwable t) {
        	
        }
    }
	
	public static void main1(String[] args) {
		System.out.println(System.getProperty("os.name"));
		System.out.println("env : " + System.getenv());
		System.out.println("properties : " + System.getProperties());
	}
}
