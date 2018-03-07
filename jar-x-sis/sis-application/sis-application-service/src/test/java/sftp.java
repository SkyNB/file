import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.file.remote.session.Session;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by dongruixi on 2017/12/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:com/tasfe/sis/configs/application/service/xml/spring-application-sftp.xml")
public class sftp {
    @Autowired
    DefaultSftpSessionFactory sftpSessionFactory;

    @Test
    public void testsftp() throws Exception{
        Session session = sftpSessionFactory.getSession();
        OutputStream os = new FileOutputStream("/tmp/1.jpg");
        session.read("/home/sftpadmin/170801175935952/20171212/130984199309080317_correct.jpg",os);
        os.flush();
        os.close();
    }
}
