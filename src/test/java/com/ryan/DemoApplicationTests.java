//package com.ryan;
//
//import com.ryan.dao.QuestionDAO;
//import com.ryan.dao.UserDao;
//import com.ryan.model.Question;
//import com.ryan.model.User;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Random;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Sql("/init-schema.sql")
//public class DemoApplicationTests {
//	@Autowired
//	UserDao userDAO;
//	@Autowired
//	QuestionDAO questionDAO;
//
//	@Test
//	public void contextLoads() {
//		Random random = new Random();
//		for (int i = 0; i < 11; ++i) {
//			User user = new User();
//			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
//			user.setName(String.format("USER%d", i));
//			user.setPassword("");
//			user.setSalt("");
//			userDAO.addUser(user);
//			user.setPassword("newPassword");
//			userDAO.updatePassword(user);
//
//
//			Question question=new Question();
//			question.setId(i+1);
//			question.setTitle(String.format("Title%d",i));
//			Date date=new Date();
//			date.setTime(date.getTime()+1000*3600*5*i);
//			question.setCreatedDate(date);
//			question.setContent(String.format("content %d",i));
//			question.setUserId(String.valueOf(i+1));
//			question.setCommentCount(i);
//			questionDAO.addQuestion(question);
//		}
//		Assert.assertEquals("newPassword",userDAO.selectById(1).getPassword());
//		userDAO.deleteById(1);
//		System.out.println(questionDAO.selectLatestQuestions(0));
//
//	}
//}
