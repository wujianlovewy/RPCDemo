package test.cn.edu.wj.demo.mango.ext;

import cn.edu.wj.rpc.mango.ext.ExtensionLoader;

public class TestExtensionLoader {

	public static void main(String[] args) {
		ExtService extService = 
				ExtensionLoader.getExtensionLoader(ExtService.class).getExtension("myExt");
		extService.testExt();
	}
	
}
