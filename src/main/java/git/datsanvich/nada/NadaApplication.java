package git.datsanvich.nada;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;
import java.io.File;

/**
 * @author GitDatSanvich
 */
@SpringBootApplication
public class NadaApplication {

    public static void main(String[] args) {
        SpringApplication.run(NadaApplication.class, args);
    }

    /**
     * 服务配置
     *
     * @return MultipartConfigElement
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //KB,MB 上传下载传输文件大小配置
        factory.setMaxFileSize(DataSize.parse("10240MB"));
        factory.setMaxRequestSize(DataSize.parse("10240MB"));
        //文件上传临时路径
        String location = System.getProperty("user.dir") + "/data/tmp";
        File tmpFile = new File(location);
        if (!tmpFile.exists()) {
            boolean mkdirs = tmpFile.mkdirs();
            if (mkdirs) {
                System.out.println(("创建成功"));
            } else {
                System.out.println(("创建失败"));
            }
        }
        factory.setLocation(location);
        return factory.createMultipartConfig();
    }
}
