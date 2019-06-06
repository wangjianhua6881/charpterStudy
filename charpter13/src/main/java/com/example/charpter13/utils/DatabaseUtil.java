package com.example.charpter13.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.io.Reader;

@Configuration
public class DatabaseUtil {
    public static SqlSession getSqlSession() throws IOException {
        //获取配置文件
        Reader reader = Resources.getResourceAsReader("databasesConfig.xml");
        //通过sql会话工厂构建配置文件，获得sql会话工厂
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader);
        //从会话工厂中获取可以执行sql语句的会话器session
        SqlSession session = factory.openSession();
        //返回获取到的会话器
        return session;
    }
}

