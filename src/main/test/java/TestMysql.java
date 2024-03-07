//
//
//import com.king.dao.QQMessageDao;
//import com.king.model.QQMessage;
//import org.apache.ibatis.io.Resources;
//import org.apache.ibatis.session.SqlSession;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.session.SqlSessionFactoryBuilder;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//public class TestMysql {
//
//    public static void main(String[] args) throws IOException {
//        String resource = "mybatis.xml";
//        InputStream inputStream = Resources.getResourceAsStream(TestMysql.class.getClassLoader(),resource);
//        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
//        SqlSessionFactory factory = builder.build(inputStream);
//        SqlSession sqlSession = factory.openSession();
//        QQMessageDao qqMessageDao = sqlSession.getMapper(QQMessageDao.class);//这种方式Dao是必需的
//        QQMessage row = qqMessageDao.getRow(1);
//        System.out.println(row);
//    }
//
//}
