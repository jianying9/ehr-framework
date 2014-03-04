package com.ehr.sendmail;

import com.ehr.sendmail.beans.MailAttachmentEntity;
import java.io.File;
import org.junit.*;

/**
 *
 * @author zoe
 */
public class SendMailTest {

    public SendMailTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

//    @Test
    public void sendTest() {
        String userEmail = "271411342@qq.com";
        String title = "test1";
        String content = "1111111111111swsssss";
        MailManager.sendMail(userEmail, title, content, true);
    }

    @Test
    public void sendAttachTest() {
        MailAttachmentEntity mm = new MailAttachmentEntity();
        mm.addFileDataSource("/home/zoe/Downloads/lesson你.txt");
        mm.addFileDataSource("/home/zoe/Downloads/lesson.txt");
        File file = new File("/home/zoe/Downloads/privilege.css");
        mm.addFileDataSource(file);
        //邮件
        String userEmail = "271411342@qq.com";
        String title = "我和你";
        String content = "啊啊啊啊啊啊啊啊啊";
        MailManager.sendMail(userEmail, title, content, true, mm);
    }
}
